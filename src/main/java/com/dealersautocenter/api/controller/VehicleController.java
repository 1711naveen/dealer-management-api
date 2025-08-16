package com.dealersautocenter.api.controller;

import com.dealersautocenter.api.dto.VehicleDTO;
import com.dealersautocenter.api.entity.VehicleStatus;
import com.dealersautocenter.api.service.VehicleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/vehicles")
@Tag(name = "Vehicle Management", description = "APIs for managing vehicles")
public class VehicleController {
    
    @Autowired
    private VehicleService vehicleService;
    
    @GetMapping
    @Operation(summary = "Get all vehicles", description = "Retrieve a list of all vehicles")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved vehicles")
    public ResponseEntity<List<VehicleDTO>> getAllVehicles() {
        List<VehicleDTO> vehicles = vehicleService.getAllVehicles();
        return ResponseEntity.ok(vehicles);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get vehicle by ID", description = "Retrieve a vehicle by its ID")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved vehicle")
    @ApiResponse(responseCode = "404", description = "Vehicle not found")
    public ResponseEntity<VehicleDTO> getVehicleById(
            @Parameter(description = "Vehicle ID") @PathVariable Long id) {
        Optional<VehicleDTO> vehicle = vehicleService.getVehicleById(id);
        return vehicle.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/dealer/{dealerId}")
    @Operation(summary = "Get vehicles by dealer", description = "Retrieve all vehicles belonging to a specific dealer")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved vehicles")
    public ResponseEntity<List<VehicleDTO>> getVehiclesByDealerId(
            @Parameter(description = "Dealer ID") @PathVariable Long dealerId) {
        List<VehicleDTO> vehicles = vehicleService.getVehiclesByDealerId(dealerId);
        return ResponseEntity.ok(vehicles);
    }
    
    @GetMapping("/status/{status}")
    @Operation(summary = "Get vehicles by status", description = "Retrieve vehicles by their status")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved vehicles")
    public ResponseEntity<List<VehicleDTO>> getVehiclesByStatus(
            @Parameter(description = "Vehicle status (AVAILABLE or SOLD)") 
            @PathVariable VehicleStatus status) {
        List<VehicleDTO> vehicles = vehicleService.getVehiclesByStatus(status);
        return ResponseEntity.ok(vehicles);
    }
    
    @GetMapping("/premium-dealers")
    @Operation(summary = "Get vehicles from premium dealers", description = "Retrieve all vehicles belonging to PREMIUM dealers only")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved vehicles from premium dealers")
    public ResponseEntity<List<VehicleDTO>> getVehiclesByPremiumDealers() {
        List<VehicleDTO> vehicles = vehicleService.getVehiclesByPremiumDealers();
        return ResponseEntity.ok(vehicles);
    }
    
    @GetMapping("/search")
    @Operation(summary = "Search vehicles by model", description = "Search vehicles by model name")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved matching vehicles")
    public ResponseEntity<List<VehicleDTO>> searchVehiclesByModel(
            @Parameter(description = "Model name to search for") 
            @RequestParam String model) {
        List<VehicleDTO> vehicles = vehicleService.searchVehiclesByModel(model);
        return ResponseEntity.ok(vehicles);
    }
    
    @PostMapping
    @Operation(summary = "Create a new vehicle", description = "Create a new vehicle")
    @ApiResponse(responseCode = "201", description = "Vehicle created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input data")
    public ResponseEntity<VehicleDTO> createVehicle(@Valid @RequestBody VehicleDTO vehicleDTO) {
        try {
            VehicleDTO createdVehicle = vehicleService.createVehicle(vehicleDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdVehicle);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update vehicle", description = "Update an existing vehicle")
    @ApiResponse(responseCode = "200", description = "Vehicle updated successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input data")
    @ApiResponse(responseCode = "404", description = "Vehicle not found")
    public ResponseEntity<VehicleDTO> updateVehicle(
            @Parameter(description = "Vehicle ID") @PathVariable Long id,
            @Valid @RequestBody VehicleDTO vehicleDTO) {
        try {
            VehicleDTO updatedVehicle = vehicleService.updateVehicle(id, vehicleDTO);
            return ResponseEntity.ok(updatedVehicle);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete vehicle", description = "Delete a vehicle")
    @ApiResponse(responseCode = "204", description = "Vehicle deleted successfully")
    @ApiResponse(responseCode = "404", description = "Vehicle not found")
    public ResponseEntity<Void> deleteVehicle(
            @Parameter(description = "Vehicle ID") @PathVariable Long id) {
        try {
            vehicleService.deleteVehicle(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
