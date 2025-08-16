package com.dealersautocenter.api.service;

import com.dealersautocenter.api.dto.VehicleDTO;
import com.dealersautocenter.api.entity.Dealer;
import com.dealersautocenter.api.entity.Vehicle;
import com.dealersautocenter.api.entity.VehicleStatus;
import com.dealersautocenter.api.repository.DealerRepository;
import com.dealersautocenter.api.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VehicleService {
    
    @Autowired
    private VehicleRepository vehicleRepository;
    
    @Autowired
    private DealerRepository dealerRepository;
    
    public List<VehicleDTO> getAllVehicles() {
        return vehicleRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public Optional<VehicleDTO> getVehicleById(Long id) {
        return vehicleRepository.findById(id)
                .map(this::convertToDTO);
    }
    
    public List<VehicleDTO> getVehiclesByDealerId(Long dealerId) {
        return vehicleRepository.findByDealerId(dealerId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<VehicleDTO> getVehiclesByStatus(VehicleStatus status) {
        return vehicleRepository.findByStatus(status).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<VehicleDTO> getVehiclesByPremiumDealers() {
        return vehicleRepository.findAllVehiclesFromPremiumDealers().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<VehicleDTO> searchVehiclesByModel(String model) {
        return vehicleRepository.findByModelContaining(model).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public VehicleDTO createVehicle(VehicleDTO vehicleDTO) {
        // Validate dealer exists
        if (!dealerRepository.existsById(vehicleDTO.getDealerId())) {
            throw new RuntimeException("Dealer with id " + vehicleDTO.getDealerId() + " not found");
        }
        
        Vehicle vehicle = convertToEntity(vehicleDTO);
        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        return convertToDTO(savedVehicle);
    }
    
    public VehicleDTO updateVehicle(Long id, VehicleDTO vehicleDTO) {
        Optional<Vehicle> existingVehicle = vehicleRepository.findById(id);
        if (!existingVehicle.isPresent()) {
            throw new RuntimeException("Vehicle with id " + id + " not found");
        }
        
        // Validate dealer exists if dealer is being changed
        if (!existingVehicle.get().getDealerId().equals(vehicleDTO.getDealerId()) &&
            !dealerRepository.existsById(vehicleDTO.getDealerId())) {
            throw new RuntimeException("Dealer with id " + vehicleDTO.getDealerId() + " not found");
        }
        
        Vehicle vehicle = existingVehicle.get();
        vehicle.setDealerId(vehicleDTO.getDealerId());
        vehicle.setModel(vehicleDTO.getModel());
        vehicle.setPrice(vehicleDTO.getPrice());
        vehicle.setStatus(vehicleDTO.getStatus());
        
        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        return convertToDTO(savedVehicle);
    }
    
    public void deleteVehicle(Long id) {
        if (!vehicleRepository.existsById(id)) {
            throw new RuntimeException("Vehicle with id " + id + " not found");
        }
        vehicleRepository.deleteById(id);
    }
    
    private VehicleDTO convertToDTO(Vehicle vehicle) {
        VehicleDTO dto = new VehicleDTO(
            vehicle.getId(),
            vehicle.getDealerId(),
            vehicle.getModel(),
            vehicle.getPrice(),
            vehicle.getStatus()
        );
        
        // Add dealer information if available
        dealerRepository.findById(vehicle.getDealerId()).ifPresent(dealer -> {
            dto.setDealerName(dealer.getName());
            dto.setDealerEmail(dealer.getEmail());
        });
        
        return dto;
    }
    
    private Vehicle convertToEntity(VehicleDTO vehicleDTO) {
        Vehicle vehicle = new Vehicle();
        vehicle.setDealerId(vehicleDTO.getDealerId());
        vehicle.setModel(vehicleDTO.getModel());
        vehicle.setPrice(vehicleDTO.getPrice());
        vehicle.setStatus(vehicleDTO.getStatus());
        return vehicle;
    }
}
