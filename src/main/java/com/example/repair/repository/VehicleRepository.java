package com.example.repair.repository;

import com.example.repair.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    Optional<Vehicle> findByLicensePlate(String licensePlate);
    Optional<Vehicle> findByVin(String vin);
    List<Vehicle> findByOwnerId(Long ownerId);
    boolean existsByLicensePlate(String licensePlate);
    boolean existsByVin(String vin);
} 