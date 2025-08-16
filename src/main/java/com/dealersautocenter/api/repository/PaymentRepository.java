package com.dealersautocenter.api.repository;

import com.dealersautocenter.api.entity.Payment;
import com.dealersautocenter.api.entity.PaymentStatus;
import com.dealersautocenter.api.entity.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    
    List<Payment> findByDealerId(Long dealerId);
    
    List<Payment> findByStatus(PaymentStatus status);
    
    List<Payment> findByPaymentMethod(PaymentMethod paymentMethod);
    
    Optional<Payment> findByTransactionId(String transactionId);
    
    List<Payment> findByDealerIdAndStatus(Long dealerId, PaymentStatus status);
    
    @Query("SELECT p FROM Payment p WHERE p.status = 'PENDING' AND p.createdAt <= :fiveSecondsAgo")
    List<Payment> findPendingPaymentsOlderThanFiveSeconds(@Param("fiveSecondsAgo") LocalDateTime fiveSecondsAgo);
}
