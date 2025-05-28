package com.example.repair.service;

import com.example.repair.dto.*;
import java.util.List;
import java.util.Map;

public interface AdminService {
    // 管理员认证
    AdminDTO login(String username, String password);
    
    // 用户管理
    List<UserDTO> getAllUsers();
    UserDTO getUserById(Long userId);
    UserDTO updateUser(Long userId, UserDTO userDTO);
    void deleteUser(Long userId);
    
    // 维修人员管理
    List<RepairmanDTO> getAllRepairmen();
    RepairmanDTO getRepairmanById(Long repairmanId);
    RepairmanDTO updateRepairman(Long repairmanId, RepairmanDTO repairmanDTO);
    void deleteRepairman(Long repairmanId);
    
    // 车辆管理
    List<VehicleDTO> getAllVehicles();
    VehicleDTO getVehicleById(Long vehicleId);
    VehicleDTO updateVehicle(Long vehicleId, VehicleDTO vehicleDTO);
    void deleteVehicle(Long vehicleId);
    
    // 维修工单管理
    List<RepairOrderDTO> getAllRepairOrders();
    RepairOrderDTO getRepairOrderById(Long orderId);
    RepairOrderDTO updateRepairOrder(Long orderId, RepairOrderDTO orderDTO);
    void deleteRepairOrder(Long orderId);
    
    // 数据统计
    Map<String, Object> getSystemStatistics();
    Map<String, Object> getRepairStatistics();
    Map<String, Object> getFinancialStatistics();
    
    // 数据一致性检查
    Map<String, Object> checkDataConsistency();
    void repairDataInconsistency(String type, Long id);
} 