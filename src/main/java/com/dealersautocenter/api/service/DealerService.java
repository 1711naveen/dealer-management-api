package com.dealersautocenter.api.service;

import com.dealersautocenter.api.dto.DealerDTO;
import com.dealersautocenter.api.entity.Dealer;
import com.dealersautocenter.api.entity.SubscriptionType;
import com.dealersautocenter.api.repository.DealerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DealerService {
    
    @Autowired
    private DealerRepository dealerRepository;
    
    public List<DealerDTO> getAllDealers() {
        return dealerRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public Optional<DealerDTO> getDealerById(Long id) {
        return dealerRepository.findById(id)
                .map(this::convertToDTO);
    }
    
    public Optional<DealerDTO> getDealerByEmail(String email) {
        return dealerRepository.findByEmail(email)
                .map(this::convertToDTO);
    }
    
    public List<DealerDTO> getDealersBySubscriptionType(SubscriptionType subscriptionType) {
        return dealerRepository.findBySubscriptionType(subscriptionType).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public DealerDTO createDealer(DealerDTO dealerDTO) {
        if (dealerRepository.existsByEmail(dealerDTO.getEmail())) {
            throw new RuntimeException("Dealer with email " + dealerDTO.getEmail() + " already exists");
        }
        
        Dealer dealer = convertToEntity(dealerDTO);
        Dealer savedDealer = dealerRepository.save(dealer);
        return convertToDTO(savedDealer);
    }
    
    public DealerDTO updateDealer(Long id, DealerDTO dealerDTO) {
        Optional<Dealer> existingDealer = dealerRepository.findById(id);
        if (!existingDealer.isPresent()) {
            throw new RuntimeException("Dealer with id " + id + " not found");
        }
        
        // Check if email is being changed and if it already exists
        if (!existingDealer.get().getEmail().equals(dealerDTO.getEmail()) && 
            dealerRepository.existsByEmail(dealerDTO.getEmail())) {
            throw new RuntimeException("Dealer with email " + dealerDTO.getEmail() + " already exists");
        }
        
        Dealer dealer = existingDealer.get();
        dealer.setName(dealerDTO.getName());
        dealer.setEmail(dealerDTO.getEmail());
        dealer.setSubscriptionType(dealerDTO.getSubscriptionType());
        
        Dealer savedDealer = dealerRepository.save(dealer);
        return convertToDTO(savedDealer);
    }
    
    public void deleteDealer(Long id) {
        if (!dealerRepository.existsById(id)) {
            throw new RuntimeException("Dealer with id " + id + " not found");
        }
        dealerRepository.deleteById(id);
    }
    
    private DealerDTO convertToDTO(Dealer dealer) {
        return new DealerDTO(
            dealer.getId(),
            dealer.getName(),
            dealer.getEmail(),
            dealer.getSubscriptionType()
        );
    }
    
    private Dealer convertToEntity(DealerDTO dealerDTO) {
        Dealer dealer = new Dealer();
        dealer.setName(dealerDTO.getName());
        dealer.setEmail(dealerDTO.getEmail());
        dealer.setSubscriptionType(dealerDTO.getSubscriptionType());
        return dealer;
    }
}
