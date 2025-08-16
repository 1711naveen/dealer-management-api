package com.dealersautocenter.api.repository;

import com.dealersautocenter.api.entity.Dealer;
import com.dealersautocenter.api.entity.SubscriptionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface DealerRepository extends JpaRepository<Dealer, Long> {
    
    Optional<Dealer> findByEmail(String email);
    
    List<Dealer> findBySubscriptionType(SubscriptionType subscriptionType);
    
    @Query("SELECT d FROM Dealer d WHERE d.subscriptionType = :subscriptionType")
    List<Dealer> findDealersBySubscriptionType(SubscriptionType subscriptionType);
    
    boolean existsByEmail(String email);
}
