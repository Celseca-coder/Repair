package com.example.repair.controller;

import com.example.repair.dto.*;
import com.example.repair.entity.Vehicle;
import com.example.repair.service.VehicleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/vehicles")
public class VehicleController {
    @Autowired
    private VehicleService vehicleService;

    @PostMapping("/addVehicles")
    public ResponseEntity<?> addVehicle(@Valid @RequestBody VehicleRequestDTO request) {
        try {
            VehicleAddResponse response = vehicleService.addVehicle(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("添加车辆过程中发生错误");
        }
    }

    @PostMapping("/getVehicles")
    public ResponseEntity<?> getVehicle(@Valid @RequestBody VehicleGetRequest request) {
        try {
            List<Vehicle> vehicles = vehicleService.getVehiclesByOwner(request.getUsername());
            return ResponseEntity.ok(vehicles);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("查询车辆过程中发生错误");
        }
    }

    @PostMapping("/editVehicles")
    public ResponseEntity<?> editVehicle(@Valid @RequestBody VehicleEditRequest request) {
        try {
            Vehicle vehicle = vehicleService.updateVehicle(request);
            return ResponseEntity.ok(vehicle);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("修改车辆信息过程中发生错误");
        }
    }

    @PostMapping("/deleteVehicles")
    public ResponseEntity<?> deleteVehicle(@Valid @RequestBody VehicleDeleteRequest request) {
        try {
            vehicleService.deleteVehicle(request);
            return ResponseEntity.ok("删除成功");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("删除车辆过程中发生错误");
        }
    }
}
