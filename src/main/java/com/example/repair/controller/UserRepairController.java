package com.example.repair.controller;

import com.example.repair.dto.*;
import com.example.repair.entity.RepairOrder;
import com.example.repair.entity.RepairRequest;
import com.example.repair.entity.Vehicle;
import com.example.repair.repository.RepairOrderRepository;
import com.example.repair.repository.VehicleRepository;
import com.example.repair.service.VehicleService;
import com.example.repair.service.impl.RepairRequestService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/repairs")
public class UserRepairController {
    @Autowired
    private VehicleService vehicleService;
    
    @Autowired
    private RepairRequestService repairRequestService;
    
    @Autowired
    private RepairOrderRepository repairOrderRepository;
    
    @Autowired
    private VehicleRepository vehicleRepository;

    @PostMapping("/addRequest")
    public ResponseEntity<?> addRequest(@Valid @RequestBody VehicleRepairRequest request) {
        try {
            repairRequestService.createRepairRequest(request);
            return ResponseEntity.ok("车辆报修请求已成功提交");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("未提交成功");
        }
    }

    @PostMapping("/getRequest")
    public ResponseEntity<?> getRequest(@Valid @RequestBody GetRequest request) {
        try {
            List<RepairRequest> repairRequests = repairRequestService.getRepairRequests(request.getUsername());
            return ResponseEntity.ok(repairRequests);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("出了一点错误");
        }
    }

    @PostMapping("/getOrders")
    public ResponseEntity<?> getOrders(@Valid @RequestBody OrderGetRequest request) {
        try {
            Vehicle vehicle = vehicleRepository.findById(request.getVehicleId()).orElse(null);
            List<RepairOrder> repairOrders = repairOrderRepository.findByVehicle(vehicle);
            return ResponseEntity.ok(repairOrders);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("出了一点错误");
        }
    }
}
