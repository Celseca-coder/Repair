package com.example.repair.service;

import com.example.repair.dto.VehicleAddResponse;
import com.example.repair.dto.VehicleDeleteRequest;
import com.example.repair.dto.VehicleEditRequest;
import com.example.repair.dto.VehicleRequestDTO;
import com.example.repair.entity.Vehicle;
import com.example.repair.entity.User;

import java.util.List;

public interface VehicleService {
    VehicleAddResponse addVehicle(VehicleRequestDTO request);
    List<Vehicle> getVehiclesByOwner(String username);
    Vehicle updateVehicle(VehicleEditRequest request);
    void deleteVehicle(VehicleDeleteRequest request);
    Vehicle getVehicleById(Long vehicleId, User owner);
}
