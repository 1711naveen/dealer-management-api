package com.dealersautocenter.api.controller;

import com.dealersautocenter.api.dto.PaymentRequestDTO;
import com.dealersautocenter.api.dto.PaymentResponseDTO;
import com.dealersautocenter.api.entity.PaymentStatus;
import com.dealersautocenter.api.service.PaymentService;
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
@RequestMapping("/api/payment")
@Tag(name = "Payment Gateway", description = "APIs for payment processing")
public class PaymentController {
    
    @Autowired
    private PaymentService paymentService;
    
    @PostMapping("/initiate")
    @Operation(summary = "Initiate payment", description = "Initiate a payment for dealer subscription")
    @ApiResponse(responseCode = "201", description = "Payment initiated successfully")
    @ApiResponse(responseCode = "400", description = "Invalid payment request")
    public ResponseEntity<PaymentResponseDTO> initiatePayment(@Valid @RequestBody PaymentRequestDTO paymentRequest) {
        try {
            PaymentResponseDTO payment = paymentService.initiatePayment(paymentRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(payment);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping
    @Operation(summary = "Get all payments", description = "Retrieve a list of all payments")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved payments")
    public ResponseEntity<List<PaymentResponseDTO>> getAllPayments() {
        List<PaymentResponseDTO> payments = paymentService.getAllPayments();
        return ResponseEntity.ok(payments);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get payment by ID", description = "Retrieve a payment by its ID")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved payment")
    @ApiResponse(responseCode = "404", description = "Payment not found")
    public ResponseEntity<PaymentResponseDTO> getPaymentById(
            @Parameter(description = "Payment ID") @PathVariable Long id) {
        Optional<PaymentResponseDTO> payment = paymentService.getPaymentById(id);
        return payment.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/dealer/{dealerId}")
    @Operation(summary = "Get payments by dealer", description = "Retrieve all payments for a specific dealer")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved payments")
    public ResponseEntity<List<PaymentResponseDTO>> getPaymentsByDealerId(
            @Parameter(description = "Dealer ID") @PathVariable Long dealerId) {
        List<PaymentResponseDTO> payments = paymentService.getPaymentsByDealerId(dealerId);
        return ResponseEntity.ok(payments);
    }
    
    @GetMapping("/status/{status}")
    @Operation(summary = "Get payments by status", description = "Retrieve payments by their status")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved payments")
    public ResponseEntity<List<PaymentResponseDTO>> getPaymentsByStatus(
            @Parameter(description = "Payment status (PENDING, SUCCESS, FAILED)") 
            @PathVariable PaymentStatus status) {
        List<PaymentResponseDTO> payments = paymentService.getPaymentsByStatus(status);
        return ResponseEntity.ok(payments);
    }
    
    @GetMapping("/transaction/{transactionId}")
    @Operation(summary = "Get payment by transaction ID", description = "Retrieve a payment by its transaction ID")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved payment")
    @ApiResponse(responseCode = "404", description = "Payment not found")
    public ResponseEntity<PaymentResponseDTO> getPaymentByTransactionId(
            @Parameter(description = "Transaction ID") @PathVariable String transactionId) {
        Optional<PaymentResponseDTO> payment = paymentService.getPaymentByTransactionId(transactionId);
        return payment.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }
    
    @PutMapping("/{id}/status")
    @Operation(summary = "Update payment status", description = "Update the status of a payment (for testing purposes)")
    @ApiResponse(responseCode = "200", description = "Payment status updated successfully")
    @ApiResponse(responseCode = "404", description = "Payment not found")
    public ResponseEntity<PaymentResponseDTO> updatePaymentStatus(
            @Parameter(description = "Payment ID") @PathVariable Long id,
            @Parameter(description = "New payment status") @RequestParam PaymentStatus status) {
        try {
            PaymentResponseDTO payment = paymentService.updatePaymentStatus(id, status);
            return ResponseEntity.ok(payment);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
