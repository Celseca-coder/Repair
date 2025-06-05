package com.example.repair.service.impl;

import com.example.repair.dto.VehicleRepairRequest;
import com.example.repair.entity.RepairRequest;
import com.example.repair.entity.User;
import com.example.repair.enums.UserRepairRequestStatus;
import com.example.repair.repository.RepairRequestRepository;
import com.example.repair.repository.UserRepository;
import com.example.repair.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
public class RepairRequestService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private VehicleService vehicleService;
    
    @Autowired
    private RepairRequestRepository repairRequestRepository;

    public void createRepairRequest(VehicleRepairRequest request) {
        RepairRequest repairRequest = new RepairRequest();

        if (request.getUsername() == null) {
            throw new RuntimeException("用户名不能为空");
        }
        if (request.getVehicleId() == null) {
            throw new RuntimeException("车辆ID不能为空");
        }
        User user = userRepository.findByUsername(request.getUsername()).orElse(null);
        if (user != null) {
            // 检测权限
            vehicleService.getVehicleById(request.getVehicleId(), user);
            repairRequest.setUserId(user.getId());
            repairRequest.setVehicleId(request.getVehicleId());
            repairRequest.setDescription(request.getDescription());
            repairRequest.setStatus(UserRepairRequestStatus.UNREVIEWED);
            repairRequest.setCreatedAt(LocalDateTime.now());
            repairRequestRepository.save(repairRequest);
        }
        else {
            throw new RuntimeException("用户不存在");
        }
    }

    public List<RepairRequest> getRepairRequests(String username) {
        User owner = userRepository.findByUsername(username).orElse(null);
        if (owner == null) {
            throw new RuntimeException("用户不存在");
        }
        return repairRequestRepository.findByUserId(owner.getId());
    }
}
