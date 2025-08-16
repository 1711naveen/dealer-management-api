package com.dealersautocenter.api.controller;

import com.dealersautocenter.api.dto.DealerDTO;
import com.dealersautocenter.api.entity.SubscriptionType;
import com.dealersautocenter.api.service.DealerService;
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
@RequestMapping("/api/dealers")
@Tag(name = "Dealer Management", description = "APIs for managing dealers")
public class DealerController {
    
    @Autowired
    private DealerService dealerService;
    
    @GetMapping
    @Operation(summary = "Get all dealers", description = "Retrieve a list of all dealers")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved dealers")
    public ResponseEntity<List<DealerDTO>> getAllDealers() {
        List<DealerDTO> dealers = dealerService.getAllDealers();
        return ResponseEntity.ok(dealers);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get dealer by ID", description = "Retrieve a dealer by their ID")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved dealer")
    @ApiResponse(responseCode = "404", description = "Dealer not found")
    public ResponseEntity<DealerDTO> getDealerById(
            @Parameter(description = "Dealer ID") @PathVariable Long id) {
        Optional<DealerDTO> dealer = dealerService.getDealerById(id);
        return dealer.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/email/{email}")
    @Operation(summary = "Get dealer by email", description = "Retrieve a dealer by their email")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved dealer")
    @ApiResponse(responseCode = "404", description = "Dealer not found")
    public ResponseEntity<DealerDTO> getDealerByEmail(
            @Parameter(description = "Dealer email") @PathVariable String email) {
        Optional<DealerDTO> dealer = dealerService.getDealerByEmail(email);
        return dealer.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/subscription/{subscriptionType}")
    @Operation(summary = "Get dealers by subscription type", description = "Retrieve dealers by their subscription type")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved dealers")
    public ResponseEntity<List<DealerDTO>> getDealersBySubscriptionType(
            @Parameter(description = "Subscription type (BASIC or PREMIUM)") 
            @PathVariable SubscriptionType subscriptionType) {
        List<DealerDTO> dealers = dealerService.getDealersBySubscriptionType(subscriptionType);
        return ResponseEntity.ok(dealers);
    }
    
    @PostMapping
    @Operation(summary = "Create a new dealer", description = "Create a new dealer")
    @ApiResponse(responseCode = "201", description = "Dealer created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input data")
    public ResponseEntity<DealerDTO> createDealer(@Valid @RequestBody DealerDTO dealerDTO) {
        try {
            DealerDTO createdDealer = dealerService.createDealer(dealerDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdDealer);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update dealer", description = "Update an existing dealer")
    @ApiResponse(responseCode = "200", description = "Dealer updated successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input data")
    @ApiResponse(responseCode = "404", description = "Dealer not found")
    public ResponseEntity<DealerDTO> updateDealer(
            @Parameter(description = "Dealer ID") @PathVariable Long id,
            @Valid @RequestBody DealerDTO dealerDTO) {
        try {
            DealerDTO updatedDealer = dealerService.updateDealer(id, dealerDTO);
            return ResponseEntity.ok(updatedDealer);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete dealer", description = "Delete a dealer")
    @ApiResponse(responseCode = "204", description = "Dealer deleted successfully")
    @ApiResponse(responseCode = "404", description = "Dealer not found")
    public ResponseEntity<Void> deleteDealer(
            @Parameter(description = "Dealer ID") @PathVariable Long id) {
        try {
            dealerService.deleteDealer(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
