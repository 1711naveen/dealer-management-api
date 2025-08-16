package com.dealersautocenter.api.dto;

import com.dealersautocenter.api.entity.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import java.math.BigDecimal;

public class PaymentRequestDTO {
    
    @NotNull(message = "Dealer ID is required")
    private Long dealerId;
    
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than 0")
    private BigDecimal amount;
    
    @NotNull(message = "Payment method is required")
    private PaymentMethod method;
    
    public PaymentRequestDTO() {}
    
    public PaymentRequestDTO(Long dealerId, BigDecimal amount, PaymentMethod method) {
        this.dealerId = dealerId;
        this.amount = amount;
        this.method = method;
    }
    
    // Getters and Setters
    public Long getDealerId() {
        return dealerId;
    }
    
    public void setDealerId(Long dealerId) {
        this.dealerId = dealerId;
    }
    
    public BigDecimal getAmount() {
        return amount;
    }
    
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    
    public PaymentMethod getMethod() {
        return method;
    }
    
    public void setMethod(PaymentMethod method) {
        this.method = method;
    }
}
