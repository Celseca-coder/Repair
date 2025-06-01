package com.example.repair.service.impl;

import com.example.repair.dto.VehicleRequestDTO;
import com.example.repair.entity.Vehicle;
import com.example.repair.entity.User;
import com.example.repair.repository.UserRepository;
import com.example.repair.repository.VehicleRepository;
import com.example.repair.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VehicleServiceImpl implements VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;
    private UserRepository userRepository;

    @Override
    public Vehicle addVehicle(VehicleRequestDTO request) {
        Vehicle vehicle = new Vehicle();
        Optional<User> ownerOpt = userRepository.findByUsername(request.getUsername());
        User owner = ownerOpt.orElseThrow(() -> new RuntimeException("用户不存在"));
        vehicle.setOwner(owner);
        vehicle.setLicensePlate(request.getLicensePlate());
        vehicle.setBrand(request.getBrand());
        vehicle.setModel(request.getModel());
        vehicle.setYear(request.getYear());
        vehicle.setColor(request.getColor());
        vehicle.setVin(request.getVin());
        return vehicleRepository.save(vehicle);
    }

    @Override
    public List<Vehicle> getVehiclesByOwner(User owner) {
        return vehicleRepository.findByOwner(owner);
    }

    @Override
    public Vehicle updateVehicle(Long vehicleId, Vehicle vehicle, User owner) {
        Vehicle existing = getVehicleById(vehicleId, owner);
        // 只允许 owner 修改自己的车辆
        existing.setBrand(vehicle.getBrand());
        existing.setModel(vehicle.getModel());
        existing.setYear(vehicle.getYear());
        existing.setColor(vehicle.getColor());
        existing.setLicensePlate(vehicle.getLicensePlate());
        existing.setVin(vehicle.getVin());
        existing.setLastMaintenanceDate(vehicle.getLastMaintenanceDate());
        return vehicleRepository.save(existing);
    }

    @Override
    public void deleteVehicle(Long vehicleId, User owner) {
        Vehicle existing = getVehicleById(vehicleId, owner);
        vehicleRepository.delete(existing);
    }

    @Override
    public Vehicle getVehicleById(Long vehicleId, User owner) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new RuntimeException("车辆不存在"));
        if (!vehicle.getOwner().getId().equals(owner.getId())) {
            throw new RuntimeException("无权限访问该车辆");
        }
        return vehicle;
    }
}