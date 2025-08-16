package com.dealersautocenter.api.service;

import com.dealersautocenter.api.dto.PaymentRequestDTO;
import com.dealersautocenter.api.dto.PaymentResponseDTO;
import com.dealersautocenter.api.entity.Payment;
import com.dealersautocenter.api.entity.PaymentStatus;
import com.dealersautocenter.api.repository.DealerRepository;
import com.dealersautocenter.api.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class PaymentService {
    
    @Autowired
    private PaymentRepository paymentRepository;
    
    @Autowired
    private DealerRepository dealerRepository;
    
    public List<PaymentResponseDTO> getAllPayments() {
        return paymentRepository.findAll().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }
    
    public Optional<PaymentResponseDTO> getPaymentById(Long id) {
        return paymentRepository.findById(id)
                .map(this::convertToResponseDTO);
    }
    
    public List<PaymentResponseDTO> getPaymentsByDealerId(Long dealerId) {
        return paymentRepository.findByDealerId(dealerId).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }
    
    public List<PaymentResponseDTO> getPaymentsByStatus(PaymentStatus status) {
        return paymentRepository.findByStatus(status).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }
    
    public Optional<PaymentResponseDTO> getPaymentByTransactionId(String transactionId) {
        return paymentRepository.findByTransactionId(transactionId)
                .map(this::convertToResponseDTO);
    }
    
    public PaymentResponseDTO initiatePayment(PaymentRequestDTO paymentRequest) {
        // Validate dealer exists
        if (!dealerRepository.existsById(paymentRequest.getDealerId())) {
            throw new RuntimeException("Dealer with id " + paymentRequest.getDealerId() + " not found");
        }
        
        Payment payment = new Payment(
            paymentRequest.getDealerId(),
            paymentRequest.getAmount(),
            paymentRequest.getMethod(),
            PaymentStatus.PENDING
        );
        
        Payment savedPayment = paymentRepository.save(payment);
        
        // Schedule async payment processing after 5 seconds
        processPaymentAfterDelay(savedPayment.getId());
        
        return convertToResponseDTO(savedPayment);
    }
    
    @Async
    public CompletableFuture<Void> processPaymentAfterDelay(Long paymentId) {
        try {
            // Wait for 5 seconds to simulate payment processing
            Thread.sleep(5000);
            
            Optional<Payment> paymentOpt = paymentRepository.findById(paymentId);
            if (paymentOpt.isPresent()) {
                Payment payment = paymentOpt.get();
                if (payment.getStatus() == PaymentStatus.PENDING) {
                    // Simulate payment success (90% success rate)
                    PaymentStatus newStatus = Math.random() < 0.9 ? PaymentStatus.SUCCESS : PaymentStatus.FAILED;
                    payment.setStatus(newStatus);
                    paymentRepository.save(payment);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return CompletableFuture.completedFuture(null);
    }
    
    // Scheduled task to process pending payments (backup mechanism)
    @Scheduled(fixedRate = 30000) // Run every 30 seconds
    public void processPendingPayments() {
        LocalDateTime fiveSecondsAgo = LocalDateTime.now().minusSeconds(10);
        List<Payment> pendingPayments = paymentRepository.findPendingPaymentsOlderThanFiveSeconds(fiveSecondsAgo);
        
        for (Payment payment : pendingPayments) {
            // Simulate payment processing result
            PaymentStatus newStatus = Math.random() < 0.9 ? PaymentStatus.SUCCESS : PaymentStatus.FAILED;
            payment.setStatus(newStatus);
            paymentRepository.save(payment);
        }
    }
    
    public PaymentResponseDTO updatePaymentStatus(Long id, PaymentStatus status) {
        Optional<Payment> paymentOpt = paymentRepository.findById(id);
        if (!paymentOpt.isPresent()) {
            throw new RuntimeException("Payment with id " + id + " not found");
        }
        
        Payment payment = paymentOpt.get();
        payment.setStatus(status);
        Payment savedPayment = paymentRepository.save(payment);
        return convertToResponseDTO(savedPayment);
    }
    
    private PaymentResponseDTO convertToResponseDTO(Payment payment) {
        return new PaymentResponseDTO(
            payment.getId(),
            payment.getDealerId(),
            payment.getAmount(),
            payment.getPaymentMethod(),
            payment.getStatus(),
            payment.getTransactionId(),
            payment.getCreatedAt(),
            payment.getUpdatedAt()
        );
    }
}
