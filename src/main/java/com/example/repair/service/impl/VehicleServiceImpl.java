package com.example.repair.service.impl;

import com.example.repair.dto.VehicleAddResponse;
import com.example.repair.dto.VehicleDeleteRequest;
import com.example.repair.dto.VehicleEditRequest;
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
    public VehicleAddResponse addVehicle(VehicleRequestDTO request) {
        Vehicle vehicle = new Vehicle();
        Optional<User> ownerOpt = userRepository.findByUsername(request.getUsername());
        User owner = ownerOpt.orElseThrow(() -> new RuntimeException("用户不存在"));
        vehicle.setOwner(owner);
        if (request.getLicensePlate() == null ){
            throw new RuntimeException("车牌号不能为空");
        }
        vehicle.setLicensePlate(request.getLicensePlate());
        if (request.getBrand() == null) {
            throw new RuntimeException("品牌不能为空");
        }
        vehicle.setBrand(request.getBrand());
        if (request.getModel() == null) {
            throw new RuntimeException("型号不能为空");
        }
        vehicle.setModel(request.getModel());
        if (request.getYear() == null) {
            throw new RuntimeException("年份不能为空");
        }
        vehicle.setYear(request.getYear());
        if (request.getColor() == null) {
            throw new RuntimeException("颜色不能为空");
        }
        vehicle.setColor(request.getColor());
        if (request.getVin() == null) {
            throw new RuntimeException("VIN不能为空");
        }
        vehicle.setVin(request.getVin());
        vehicleRepository.save(vehicle);

        VehicleAddResponse response = new VehicleAddResponse();
        response.setVehicle(vehicle);
        return response;
    }

    @Override
    public List<Vehicle> getVehiclesByOwner(String username) {
        User owner = userRepository.findByUsername(username).orElse(null);
        if (owner == null) {
            throw new RuntimeException("用户不存在");
        }
        return vehicleRepository.findByOwner(owner);
    }

    @Override
    public Vehicle updateVehicle(VehicleEditRequest request) {
        User owner = userRepository.findByUsername(request.getUsername()).orElse(null);
        Vehicle existing = getVehicleById(request.getVehicleId(), owner);
        // 只允许 owner 修改自己的车辆
        if (request.getBrand() == null) {
            throw new RuntimeException("车型不能为空");
        }
        existing.setBrand(request.getBrand());
        if (request.getModel() == null) {
            throw new RuntimeException("车系不能为空");
        }
        existing.setModel(request.getModel());
        if (request.getYear() == null) {
            throw new RuntimeException("年份不能为空");
        }
        existing.setYear(request.getYear());
        if (request.getColor() == null) {
            throw new RuntimeException("颜色不能为空");
        }
        existing.setColor(request.getColor());
        if (request.getLicencePlate() == null) {
            throw new RuntimeException("车牌号不能为空");
        }
        existing.setLicensePlate(request.getLicencePlate());
        if (request.getVin() == null) {
            throw new RuntimeException("VIN码不能为空");
        }
        existing.setVin(request.getVin());
        return vehicleRepository.save(existing);
    }

    @Override
    public void deleteVehicle(VehicleDeleteRequest request) {
        User owner = userRepository.findByUsername(request.getUsername()).orElse(null);
        Vehicle existing = getVehicleById(request.getVehicleId(), owner);
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