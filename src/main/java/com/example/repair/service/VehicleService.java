package com.example.repair.service;

import com.example.repair.dto.VehicleRequestDTO;
import com.example.repair.entity.Vehicle;
import com.example.repair.entity.User;

import java.util.List;

public interface VehicleService {
    Vehicle addVehicle(VehicleRequestDTO request);
    List<Vehicle> getVehiclesByOwner(User owner);
    Vehicle updateVehicle(Long vehicleId, Vehicle vehicle, User owner);
    void deleteVehicle(Long vehicleId, User owner);
    Vehicle getVehicleById(Long vehicleId, User owner);
}
