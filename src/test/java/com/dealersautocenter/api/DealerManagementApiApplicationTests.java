package com.dealersautocenter.api;

import com.dealersautocenter.api.entity.Dealer;
import com.dealersautocenter.api.entity.Payment;
import com.dealersautocenter.api.entity.PaymentMethod;
import com.dealersautocenter.api.entity.PaymentStatus;
import com.dealersautocenter.api.entity.SubscriptionType;
import com.dealersautocenter.api.repository.DealerRepository;
import com.dealersautocenter.api.repository.PaymentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class DealerManagementApiApplicationTests {

	@Autowired
	private DealerRepository dealerRepository;
	
	@Autowired
	private PaymentRepository paymentRepository;

	@Test
	void contextLoads() {
		assertNotNull(dealerRepository);
		assertNotNull(paymentRepository);
	}
	
	@Test
	void testPaymentRepositoryQuery() {
		// Create a test dealer
		Dealer dealer = new Dealer("Test Dealer", "test@example.com", SubscriptionType.PREMIUM);
		dealer = dealerRepository.save(dealer);
		
		// Create a test payment that's older than 5 seconds
		Payment payment = new Payment(dealer.getId(), new BigDecimal("100.00"), PaymentMethod.UPI, PaymentStatus.PENDING);
		payment.setCreatedAt(LocalDateTime.now().minusSeconds(10));
		paymentRepository.save(payment);
		
		// Test the query that was causing issues
		LocalDateTime fiveSecondsAgo = LocalDateTime.now().minusSeconds(5);
		List<Payment> oldPayments = paymentRepository.findPendingPaymentsOlderThanFiveSeconds(fiveSecondsAgo);
		
		// Should find the payment we created
		assertFalse(oldPayments.isEmpty());
		assertEquals(PaymentStatus.PENDING, oldPayments.get(0).getStatus());
	}
}
