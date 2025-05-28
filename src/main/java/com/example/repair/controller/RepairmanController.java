package com.example.repair.controller;

import com.example.repair.dto.*;
import com.example.repair.service.RepairmanService;
import com.example.repair.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/repairman")
public class RepairmanController {
    
    @Autowired
    private RepairmanService repairmanService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @PostMapping("/register")
    public ResponseEntity<RepairmanDTO> register(@RequestBody RepairmanDTO repairmanDTO) {
        RepairmanDTO registeredRepairman = repairmanService.register(repairmanDTO);
        return ResponseEntity.ok(registeredRepairman);
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam(required = false) String username, 
                                 @RequestParam(required = false) String password) {
        if (username == null || password == null) {
            return ResponseEntity.badRequest().body("用户名和密码不能为空");
        }
        
        try {
            RepairmanDTO repairman = repairmanService.login(username, password);
            String token = jwtUtil.generateToken(username);
            
            Map<String, Object> response = new HashMap<>();
            response.put("repairman", repairman);
            response.put("token", token);
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @GetMapping("/{repairmanId}")
    public ResponseEntity<RepairmanDTO> getRepairmanInfo(@PathVariable Long repairmanId) {
        RepairmanDTO repairman = repairmanService.getRepairmanInfo(repairmanId);
        return ResponseEntity.ok(repairman);
    }
    
    @PostMapping("/{repairmanId}/orders/{orderId}/accept")
    public ResponseEntity<RepairOrderDTO> acceptRepairOrder(
            @PathVariable Long repairmanId,
            @PathVariable Long orderId) {
        RepairOrderDTO order = repairmanService.acceptRepairOrder(repairmanId, orderId);
        return ResponseEntity.ok(order);
    }
    
    @PostMapping("/{repairmanId}/orders/{orderId}/reject")
    public ResponseEntity<RepairOrderDTO> rejectRepairOrder(
            @PathVariable Long repairmanId,
            @PathVariable Long orderId) {
        RepairOrderDTO order = repairmanService.rejectRepairOrder(repairmanId, orderId);
        return ResponseEntity.ok(order);
    }
    
    @PostMapping("/orders/{orderId}/materials")
    public ResponseEntity<MaterialUsageDTO> recordMaterialUsage(
            @PathVariable Long orderId,
            @RequestBody MaterialUsageDTO materialUsage) {
        materialUsage.setRepairOrderId(orderId);
        MaterialUsageDTO usage = repairmanService.recordMaterialUsage(materialUsage);
        return ResponseEntity.ok(usage);
    }
    
    @PostMapping("/orders/{orderId}/progress")
    public ResponseEntity<RepairProgressDTO> updateRepairProgress(
            @PathVariable Long orderId,
            @RequestBody RepairProgressDTO progress) {
        progress.setRepairOrderId(orderId);
        RepairProgressDTO updatedProgress = repairmanService.updateRepairProgress(progress);
        return ResponseEntity.ok(updatedProgress);
    }
    
    @PutMapping("/{repairmanId}/orders/{orderId}/result")
    public ResponseEntity<RepairOrderDTO> updateRepairResult(
            @PathVariable Long repairmanId,
            @PathVariable Long orderId,
            @RequestParam String result) {
        RepairOrderDTO order = repairmanService.updateRepairResult(repairmanId, orderId, result);
        return ResponseEntity.ok(order);
    }
    
    @GetMapping("/{repairmanId}/history")
    public ResponseEntity<List<RepairOrderDTO>> getRepairHistory(@PathVariable Long repairmanId) {
        List<RepairOrderDTO> history = repairmanService.getRepairHistory(repairmanId);
        return ResponseEntity.ok(history);
    }
    
    @GetMapping("/{repairmanId}/income")
    public ResponseEntity<Double> calculateLaborIncome(@PathVariable Long repairmanId) {
        Double income = repairmanService.calculateLaborIncome(repairmanId);
        return ResponseEntity.ok(income);
    }
    
    @GetMapping("/current")
    public ResponseEntity<RepairmanDTO> getCurrentUserInfo() {
        RepairmanDTO repairman = repairmanService.getCurrentUserInfo();
        return ResponseEntity.ok(repairman);
    }
} 