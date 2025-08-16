package com.dealersautocenter.api.config;

import com.dealersautocenter.api.entity.Dealer;
import com.dealersautocenter.api.entity.SubscriptionType;
import com.dealersautocenter.api.entity.Vehicle;
import com.dealersautocenter.api.entity.VehicleStatus;
import com.dealersautocenter.api.repository.DealerRepository;
import com.dealersautocenter.api.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private DealerRepository dealerRepository;
    
    @Autowired
    private VehicleRepository vehicleRepository;
    
    @Override
    public void run(String... args) throws Exception {
        // Check if data already exists
        if (dealerRepository.count() == 0) {
            initializeSampleData();
        }
    }
    
    private void initializeSampleData() {
        // Create sample dealers
        Dealer dealer1 = new Dealer("Premium Motors", "premium@example.com", SubscriptionType.PREMIUM);
        Dealer dealer2 = new Dealer("Basic Cars", "basic@example.com", SubscriptionType.BASIC);
        Dealer dealer3 = new Dealer("Elite Vehicles", "elite@example.com", SubscriptionType.PREMIUM);
        Dealer dealer4 = new Dealer("Standard Auto", "standard@example.com", SubscriptionType.BASIC);
        
        dealerRepository.save(dealer1);
        dealerRepository.save(dealer2);
        dealerRepository.save(dealer3);
        dealerRepository.save(dealer4);
        
        // Create sample vehicles for Premium dealers
        Vehicle vehicle1 = new Vehicle(dealer1.getId(), "BMW X5", new BigDecimal("55000.00"), VehicleStatus.AVAILABLE);
        Vehicle vehicle2 = new Vehicle(dealer1.getId(), "Mercedes C-Class", new BigDecimal("45000.00"), VehicleStatus.AVAILABLE);
        Vehicle vehicle3 = new Vehicle(dealer1.getId(), "Audi A4", new BigDecimal("42000.00"), VehicleStatus.SOLD);
        
        Vehicle vehicle4 = new Vehicle(dealer3.getId(), "Porsche 911", new BigDecimal("85000.00"), VehicleStatus.AVAILABLE);
        Vehicle vehicle5 = new Vehicle(dealer3.getId(), "Jaguar F-Type", new BigDecimal("65000.00"), VehicleStatus.AVAILABLE);
        
        // Create sample vehicles for Basic dealers
        Vehicle vehicle6 = new Vehicle(dealer2.getId(), "Honda Civic", new BigDecimal("25000.00"), VehicleStatus.AVAILABLE);
        Vehicle vehicle7 = new Vehicle(dealer2.getId(), "Toyota Camry", new BigDecimal("28000.00"), VehicleStatus.SOLD);
        
        Vehicle vehicle8 = new Vehicle(dealer4.getId(), "Ford Focus", new BigDecimal("22000.00"), VehicleStatus.AVAILABLE);
        Vehicle vehicle9 = new Vehicle(dealer4.getId(), "Nissan Altima", new BigDecimal("24000.00"), VehicleStatus.AVAILABLE);
        
        vehicleRepository.save(vehicle1);
        vehicleRepository.save(vehicle2);
        vehicleRepository.save(vehicle3);
        vehicleRepository.save(vehicle4);
        vehicleRepository.save(vehicle5);
        vehicleRepository.save(vehicle6);
        vehicleRepository.save(vehicle7);
        vehicleRepository.save(vehicle8);
        vehicleRepository.save(vehicle9);
        
        System.out.println("Sample data initialized successfully!");
        System.out.println("Premium Dealers: " + dealerRepository.findBySubscriptionType(SubscriptionType.PREMIUM).size());
        System.out.println("Basic Dealers: " + dealerRepository.findBySubscriptionType(SubscriptionType.BASIC).size());
        System.out.println("Total Vehicles: " + vehicleRepository.count());
        System.out.println("Vehicles from Premium Dealers: " + vehicleRepository.findAllVehiclesFromPremiumDealers().size());
    }
}
