package com.example.repair.controller;

import com.example.repair.dto.*;
import com.example.repair.service.AdminService;
import com.example.repair.util.JwtUtil;
import com.example.repair.util.ExcelExportUtil;
import com.example.repair.util.SystemMonitorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.LinkedHashMap;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    
    @Autowired
    private AdminService adminService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private ExcelExportUtil excelExportUtil;
    
    @Autowired
    private SystemMonitorUtil systemMonitorUtil;
    
    // 管理员登录
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password) {
        try {
            AdminDTO admin = adminService.login(username, password);
            String token = jwtUtil.generateToken(username);
            
            Map<String, Object> response = Map.of(
                "admin", admin,
                "token", token
            );
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    // 用户管理
    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }
    
    @GetMapping("/users/{userId}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long userId) {
        return ResponseEntity.ok(adminService.getUserById(userId));
    }
    
    @PutMapping("/users/{userId}")
    public ResponseEntity<UserDTO> updateUser(
            @PathVariable Long userId,
            @RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(adminService.updateUser(userId, userDTO));
    }
    
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        adminService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }
    
    // 维修人员管理
    @GetMapping("/repairmen")
    public ResponseEntity<List<RepairmanDTO>> getAllRepairmen() {
        return ResponseEntity.ok(adminService.getAllRepairmen());
    }
    
    @GetMapping("/repairmen/{repairmanId}")
    public ResponseEntity<RepairmanDTO> getRepairmanById(@PathVariable Long repairmanId) {
        return ResponseEntity.ok(adminService.getRepairmanById(repairmanId));
    }
    
    @PutMapping("/repairmen/{repairmanId}")
    public ResponseEntity<RepairmanDTO> updateRepairman(
            @PathVariable Long repairmanId,
            @RequestBody RepairmanDTO repairmanDTO) {
        return ResponseEntity.ok(adminService.updateRepairman(repairmanId, repairmanDTO));
    }
    
    @DeleteMapping("/repairmen/{repairmanId}")
    public ResponseEntity<Void> deleteRepairman(@PathVariable Long repairmanId) {
        adminService.deleteRepairman(repairmanId);
        return ResponseEntity.ok().build();
    }
    
    // 车辆管理
    @GetMapping("/vehicles")
    public ResponseEntity<List<VehicleDTO>> getAllVehicles() {
        return ResponseEntity.ok(adminService.getAllVehicles());
    }
    
    @GetMapping("/vehicles/{vehicleId}")
    public ResponseEntity<VehicleDTO> getVehicleById(@PathVariable Long vehicleId) {
        return ResponseEntity.ok(adminService.getVehicleById(vehicleId));
    }
    
    @PutMapping("/vehicles/{vehicleId}")
    public ResponseEntity<VehicleDTO> updateVehicle(
            @PathVariable Long vehicleId,
            @RequestBody VehicleDTO vehicleDTO) {
        return ResponseEntity.ok(adminService.updateVehicle(vehicleId, vehicleDTO));
    }
    
    @DeleteMapping("/vehicles/{vehicleId}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable Long vehicleId) {
        adminService.deleteVehicle(vehicleId);
        return ResponseEntity.ok().build();
    }
    
    // 维修工单管理
    @GetMapping("/orders")
    public ResponseEntity<List<RepairOrderDTO>> getAllRepairOrders() {
        return ResponseEntity.ok(adminService.getAllRepairOrders());
    }
    
    @GetMapping("/orders/{orderId}")
    public ResponseEntity<RepairOrderDTO> getRepairOrderById(@PathVariable Long orderId) {
        return ResponseEntity.ok(adminService.getRepairOrderById(orderId));
    }
    
    @PutMapping("/orders/{orderId}")
    public ResponseEntity<RepairOrderDTO> updateRepairOrder(
            @PathVariable Long orderId,
            @RequestBody RepairOrderDTO orderDTO) {
        return ResponseEntity.ok(adminService.updateRepairOrder(orderId, orderDTO));
    }
    
    @DeleteMapping("/orders/{orderId}")
    public ResponseEntity<Void> deleteRepairOrder(@PathVariable Long orderId) {
        adminService.deleteRepairOrder(orderId);
        return ResponseEntity.ok().build();
    }
    
    // 数据统计
    @GetMapping("/statistics/system")
    public ResponseEntity<Map<String, Object>> getSystemStatistics() {
        return ResponseEntity.ok(adminService.getSystemStatistics());
    }
    
    @GetMapping("/statistics/repair")
    public ResponseEntity<Map<String, Object>> getRepairStatistics() {
        return ResponseEntity.ok(adminService.getRepairStatistics());
    }
    
    @GetMapping("/statistics/financial")
    public ResponseEntity<Map<String, Object>> getFinancialStatistics() {
        return ResponseEntity.ok(adminService.getFinancialStatistics());
    }
    
    // 数据一致性检查
    @GetMapping("/consistency/check")
    public ResponseEntity<Map<String, Object>> checkDataConsistency() {
        return ResponseEntity.ok(adminService.checkDataConsistency());
    }
    
    @PostMapping("/consistency/repair")
    public ResponseEntity<Void> repairDataInconsistency(
            @RequestParam String type,
            @RequestParam Long id) {
        adminService.repairDataInconsistency(type, id);
        return ResponseEntity.ok().build();
    }
    
    // 导出数据
    @GetMapping("/export/users")
    public ResponseEntity<byte[]> exportUsers() {
        List<UserDTO> users = adminService.getAllUsers();
        List<String> headers = Arrays.asList("ID", "用户名", "姓名", "电话", "邮箱", "地址");
        
        List<Map<String, Object>> data = users.stream()
            .map(user -> {
                Map<String, Object> row = new LinkedHashMap<>();
                row.put("ID", user.getId());
                row.put("用户名", user.getUsername());
                row.put("姓名", user.getProfile() != null ? user.getProfile().getName() : "");
                row.put("电话", user.getProfile() != null ? user.getProfile().getPhone() : "");
                row.put("邮箱", user.getProfile() != null ? user.getProfile().getEmail() : "");
                row.put("地址", user.getProfile() != null ? user.getProfile().getAddress() : "");
                return row;
            })
            .collect(Collectors.toList());
            
        byte[] excelFile = excelExportUtil.exportToExcel(data, headers, "用户数据");
        
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        responseHeaders.setContentDispositionFormData("attachment", "users.xlsx");
        
        return ResponseEntity.ok()
            .headers(responseHeaders)
            .body(excelFile);
    }
    
    @GetMapping("/export/repairmen")
    public ResponseEntity<byte[]> exportRepairmen() {
        List<RepairmanDTO> repairmen = adminService.getAllRepairmen();
        List<String> headers = Arrays.asList("ID", "用户名", "工种", "时薪", "状态");
        
        List<Map<String, Object>> data = repairmen.stream()
            .map(repairman -> {
                Map<String, Object> row = new LinkedHashMap<>();
                row.put("ID", repairman.getId());
                row.put("用户名", repairman.getUsername());
                row.put("工种", repairman.getWorkType() != null ? repairman.getWorkType().getDescription() : "");
                row.put("时薪", repairman.getHourlyRate());
                row.put("状态", repairman.getStatus() != null ? repairman.getStatus().getDescription() : "");
                return row;
            })
            .collect(Collectors.toList());
            
        byte[] excelFile = excelExportUtil.exportToExcel(data, headers, "维修人员数据");
        
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        responseHeaders.setContentDispositionFormData("attachment", "repairmen.xlsx");
        
        return ResponseEntity.ok()
            .headers(responseHeaders)
            .body(excelFile);
    }
    
    @GetMapping("/export/vehicles")
    public ResponseEntity<byte[]> exportVehicles() {
        List<VehicleDTO> vehicles = adminService.getAllVehicles();
        List<String> headers = Arrays.asList("ID", "车牌号", "品牌", "型号", "年份", "颜色", "VIN", "最后维护时间", "车主ID", "车主姓名");
        
        List<Map<String, Object>> data = vehicles.stream()
            .map(vehicle -> {
                Map<String, Object> row = new LinkedHashMap<>();
                row.put("ID", vehicle.getId());
                row.put("车牌号", vehicle.getLicensePlate());
                row.put("品牌", vehicle.getBrand());
                row.put("型号", vehicle.getModel());
                row.put("年份", vehicle.getYear());
                row.put("颜色", vehicle.getColor());
                row.put("VIN", vehicle.getVin());
                row.put("最后维护时间", vehicle.getLastMaintenanceDate());
                row.put("车主ID", vehicle.getOwnerId());
                row.put("车主姓名", vehicle.getOwnerName());
                return row;
            })
            .collect(Collectors.toList());
            
        byte[] excelFile = excelExportUtil.exportToExcel(data, headers, "车辆数据");
        
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        responseHeaders.setContentDispositionFormData("attachment", "vehicles.xlsx");
        
        return ResponseEntity.ok()
            .headers(responseHeaders)
            .body(excelFile);
    }
    
    @GetMapping("/export/orders")
    public ResponseEntity<byte[]> exportOrders() {
        List<RepairOrderDTO> orders = adminService.getAllRepairOrders();
        List<String> headers = Arrays.asList("ID", "维修人员ID", "状态", "描述", "创建时间", "接受时间", 
            "完成时间", "总工时", "人工费用", "材料费用", "维修结果");
        
        List<Map<String, Object>> data = orders.stream()
            .map(order -> {
                Map<String, Object> row = new LinkedHashMap<>();
                row.put("ID", order.getId());
                row.put("维修人员ID", order.getRepairmanId());
                row.put("状态", order.getStatus());
                row.put("描述", order.getDescription());
                row.put("创建时间", order.getCreateTime());
                row.put("接受时间", order.getAcceptTime());
                row.put("完成时间", order.getCompleteTime());
                row.put("总工时", order.getTotalHours());
                row.put("人工费用", order.getLaborCost());
                row.put("材料费用", order.getMaterialCost());
                row.put("维修结果", order.getRepairResult());
                return row;
            })
            .collect(Collectors.toList());
            
        byte[] excelFile = excelExportUtil.exportToExcel(data, headers, "维修工单数据");
        
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        responseHeaders.setContentDispositionFormData("attachment", "repair_orders.xlsx");
        
        return ResponseEntity.ok()
            .headers(responseHeaders)
            .body(excelFile);
    }
    
    // 系统监控
    @GetMapping("/monitor/performance")
    public ResponseEntity<Map<String, Object>> getSystemPerformance() {
        Map<String, Object> performance = new HashMap<>();
        performance.put("systemInfo", systemMonitorUtil.getSystemPerformance());
        performance.put("jvmInfo", systemMonitorUtil.getJvmInfo());
        return ResponseEntity.ok(performance);
    }
    
    @GetMapping("/monitor/logs")
    public ResponseEntity<List<Map<String, Object>>> getSystemLogs(
            @RequestParam(required = false) String level,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime) {
        // TODO: 实现日志查询功能，可以使用 ELK 或其他日志系统
        return ResponseEntity.ok(new ArrayList<>());
    }

    // 将维修请求转换为维修工单
    @PostMapping("/repair-requests/{requestId}/convert-to-order")
    public ResponseEntity<?> convertRequestToOrder(
            @PathVariable Long requestId,
            @RequestBody RepairRequestToOrderDTO request) {
        try {
            RepairOrderDTO order = adminService.convertRequestToOrder(requestId, request);
            return ResponseEntity.ok(order);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("转换维修请求到工单时发生错误");
        }
    }

    // 获取所有待处理的维修请求
    @GetMapping("/repair-requests/pending")
    public ResponseEntity<?> getPendingRepairRequests() {
        try {
            List<RepairRequestDTO> requests = adminService.getPendingRepairRequests();
            return ResponseEntity.ok(requests);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("获取待处理维修请求时发生错误");
        }
    }
} 