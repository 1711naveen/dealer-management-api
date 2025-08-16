package com.dealersautocenter.api.dto;

import com.dealersautocenter.api.entity.VehicleStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import java.math.BigDecimal;

public class VehicleDTO {
    
    private Long id;
    
    @NotNull(message = "Dealer ID is required")
    private Long dealerId;
    
    @NotBlank(message = "Model is required")
    private String model;
    
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal price;
    
    @NotNull(message = "Status is required")
    private VehicleStatus status;
    
    private String dealerName;
    private String dealerEmail;
    
    public VehicleDTO() {}
    
    public VehicleDTO(Long id, Long dealerId, String model, BigDecimal price, VehicleStatus status) {
        this.id = id;
        this.dealerId = dealerId;
        this.model = model;
        this.price = price;
        this.status = status;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getDealerId() {
        return dealerId;
    }
    
    public void setDealerId(Long dealerId) {
        this.dealerId = dealerId;
    }
    
    public String getModel() {
        return model;
    }
    
    public void setModel(String model) {
        this.model = model;
    }
    
    public BigDecimal getPrice() {
        return price;
    }
    
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    
    public VehicleStatus getStatus() {
        return status;
    }
    
    public void setStatus(VehicleStatus status) {
        this.status = status;
    }
    
    public String getDealerName() {
        return dealerName;
    }
    
    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }
    
    public String getDealerEmail() {
        return dealerEmail;
    }
    
    public void setDealerEmail(String dealerEmail) {
        this.dealerEmail = dealerEmail;
    }
}
