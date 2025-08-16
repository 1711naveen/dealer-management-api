package com.dealersautocenter.api.repository;

import com.dealersautocenter.api.entity.Vehicle;
import com.dealersautocenter.api.entity.VehicleStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    
    List<Vehicle> findByDealerId(Long dealerId);
    
    List<Vehicle> findByStatus(VehicleStatus status);
    
    List<Vehicle> findByDealerIdAndStatus(Long dealerId, VehicleStatus status);
    
    @Query("SELECT v FROM Vehicle v JOIN v.dealer d WHERE d.subscriptionType = 'PREMIUM'")
    List<Vehicle> findVehiclesByPremiumDealers();
    
    @Query("SELECT v FROM Vehicle v WHERE v.dealerId IN " +
           "(SELECT d.id FROM Dealer d WHERE d.subscriptionType = 'PREMIUM')")
    List<Vehicle> findAllVehiclesFromPremiumDealers();
    
    @Query("SELECT v FROM Vehicle v WHERE v.model LIKE %:model%")
    List<Vehicle> findByModelContaining(@Param("model") String model);
}
