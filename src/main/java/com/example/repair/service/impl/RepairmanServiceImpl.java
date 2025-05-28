package com.example.repair.service.impl;

import com.example.repair.dto.*;
import com.example.repair.entity.*;
import com.example.repair.enums.OrderStatus;
import com.example.repair.enums.StaffStatus;
import com.example.repair.repository.*;
import com.example.repair.service.RepairmanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class RepairmanServiceImpl implements RepairmanService {
    
    @Autowired
    private MaintenanceStaffRepository maintenanceStaffRepository;
    
    @Autowired
    private RepairOrderRepository repairOrderRepository;
    
    @Autowired
    private MaterialUsageRepository materialUsageRepository;
    
    @Autowired
    private RepairProgressRepository repairProgressRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    @Transactional
    public RepairmanDTO register(RepairmanDTO repairmanDTO) {
        // 检查用户名是否已存在
        if (maintenanceStaffRepository.existsByUsername(repairmanDTO.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }
        
        // 创建维修人员实体
        MaintenanceStaff staff = new MaintenanceStaff();
        staff.setUsername(repairmanDTO.getUsername());
        staff.setPassword(passwordEncoder.encode(repairmanDTO.getPassword()));
        staff.setWorkType(repairmanDTO.getWorkType());
        staff.setHourlyRate(BigDecimal.valueOf(repairmanDTO.getHourlyRate()));
        staff.setStatus(StaffStatus.IDLE); // 初始状态为空闲
        
        // 保存到数据库
        staff = maintenanceStaffRepository.save(staff);
        
        // 转换为DTO返回
        repairmanDTO.setId(staff.getId());
        repairmanDTO.setPassword(null); // 不返回密码
        repairmanDTO.setStatus(staff.getStatus());
        return repairmanDTO;
    }
    
    @Override
    public RepairmanDTO login(String username, String password) {
        MaintenanceStaff staff = maintenanceStaffRepository.findByUsername(username);
        if (staff == null || !passwordEncoder.matches(password, staff.getPassword())) {
            throw new RuntimeException("用户名或密码错误");
        }
        
        RepairmanDTO dto = new RepairmanDTO();
        dto.setId(staff.getId());
        dto.setUsername(staff.getUsername());
        dto.setWorkType(staff.getWorkType());
        dto.setHourlyRate(staff.getHourlyRate().doubleValue());
        dto.setStatus(staff.getStatus());
        return dto;
    }
    
    @Override
    public RepairmanDTO getRepairmanInfo(Long repairmanId) {
        MaintenanceStaff staff = maintenanceStaffRepository.findById(repairmanId)
            .orElseThrow(() -> new RuntimeException("维修人员不存在"));
            
        RepairmanDTO dto = new RepairmanDTO();
        dto.setId(staff.getId());
        dto.setUsername(staff.getUsername());
        dto.setWorkType(staff.getWorkType());
        dto.setHourlyRate(staff.getHourlyRate().doubleValue());
        dto.setStatus(staff.getStatus());
        return dto;
    }
    
    @Override
    @Transactional
    public RepairOrderDTO acceptRepairOrder(Long repairmanId, Long orderId) {
        MaintenanceStaff staff = maintenanceStaffRepository.findById(repairmanId)
            .orElseThrow(() -> new RuntimeException("维修人员不存在"));
            
        RepairOrder order = repairOrderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("维修工单不存在"));
            
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new RuntimeException("工单状态不正确，无法接收");
        }
        
        if (staff.getStatus() != StaffStatus.IDLE) {
            throw new RuntimeException("维修人员当前状态无法接收新工单");
        }
        
        order.setRepairman(staff);
        order.setStatus(OrderStatus.ACCEPTED);
        order.setAcceptTime(LocalDateTime.now());
        staff.setStatus(StaffStatus.BUSY);
        
        order = repairOrderRepository.save(order);
        maintenanceStaffRepository.save(staff);
        
        return convertToRepairOrderDTO(order);
    }
    
    @Override
    @Transactional
    public RepairOrderDTO rejectRepairOrder(Long repairmanId, Long orderId) {
        MaintenanceStaff staff = maintenanceStaffRepository.findById(repairmanId)
            .orElseThrow(() -> new RuntimeException("维修人员不存在"));
            
        RepairOrder order = repairOrderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("维修工单不存在"));
            
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new RuntimeException("工单状态不正确，无法拒绝");
        }
        
        order.setStatus(OrderStatus.REJECTED);
        order = repairOrderRepository.save(order);
        
        return convertToRepairOrderDTO(order);
    }
    
    @Override
    @Transactional
    public MaterialUsageDTO recordMaterialUsage(MaterialUsageDTO materialUsageDTO) {
        RepairOrder order = repairOrderRepository.findById(materialUsageDTO.getRepairOrderId())
            .orElseThrow(() -> new RuntimeException("维修工单不存在"));
            
        if (order.getStatus() != OrderStatus.IN_PROGRESS) {
            throw new RuntimeException("工单状态不正确，无法记录材料使用");
        }
        
        MaterialUsage usage = new MaterialUsage();
        usage.setRepairOrder(order);
        usage.setMaterialName(materialUsageDTO.getMaterialName());
        usage.setQuantity(BigDecimal.valueOf(materialUsageDTO.getQuantity()));
        usage.setUnitPrice(BigDecimal.valueOf(materialUsageDTO.getUnitPrice()));
        usage.setTotalPrice(BigDecimal.valueOf(materialUsageDTO.getTotalPrice()));
        usage.setUsageDescription(materialUsageDTO.getUsageDescription());
        
        usage = materialUsageRepository.save(usage);
        
        // 更新工单的材料费用
        order.setMaterialCost(order.getMaterialCost() + materialUsageDTO.getTotalPrice());
        repairOrderRepository.save(order);
        
        return convertToMaterialUsageDTO(usage);
    }
    
    @Override
    @Transactional
    public RepairProgressDTO updateRepairProgress(RepairProgressDTO progressDTO) {
        RepairOrder order = repairOrderRepository.findById(progressDTO.getRepairOrderId())
            .orElseThrow(() -> new RuntimeException("维修工单不存在"));
            
        if (order.getStatus() != OrderStatus.ACCEPTED && order.getStatus() != OrderStatus.IN_PROGRESS) {
            throw new RuntimeException("工单状态不正确，无法更新进度");
        }
        
        RepairProgress progress = new RepairProgress();
        progress.setRepairOrder(order);
        progress.setProgress(progressDTO.getProgress());
        progress.setStatus(OrderStatus.IN_PROGRESS);
        progress.setRemark(progressDTO.getRemark());
        
        progress = repairProgressRepository.save(progress);
        
        // 更新工单状态
        order.setStatus(OrderStatus.IN_PROGRESS);
        repairOrderRepository.save(order);
        
        return convertToRepairProgressDTO(progress);
    }
    
    @Override
    @Transactional
    public RepairOrderDTO updateRepairResult(Long repairmanId, Long orderId, String result) {
        MaintenanceStaff staff = maintenanceStaffRepository.findById(repairmanId)
            .orElseThrow(() -> new RuntimeException("维修人员不存在"));
            
        RepairOrder order = repairOrderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("维修工单不存在"));
            
        if (order.getStatus() != OrderStatus.IN_PROGRESS) {
            throw new RuntimeException("工单状态不正确，无法更新结果");
        }
        
        if (!order.getRepairman().getId().equals(repairmanId)) {
            throw new RuntimeException("无权更新此工单");
        }
        
        order.setStatus(OrderStatus.COMPLETED);
        order.setCompleteTime(LocalDateTime.now());
        order.setRepairResult(result);
        
        // 计算工时费
        double hours = java.time.Duration.between(order.getAcceptTime(), order.getCompleteTime()).toHours();
        order.setTotalHours(hours);
        order.setLaborCost(hours * staff.getHourlyRate().doubleValue());
        
        order = repairOrderRepository.save(order);
        
        // 更新维修人员状态
        staff.setStatus(StaffStatus.IDLE);
        maintenanceStaffRepository.save(staff);
        
        return convertToRepairOrderDTO(order);
    }
    
    @Override
    public List<RepairOrderDTO> getRepairHistory(Long repairmanId) {
        List<RepairOrder> orders = repairOrderRepository.findByRepairmanId(repairmanId);
        return orders.stream()
            .map(this::convertToRepairOrderDTO)
            .collect(Collectors.toList());
    }
    
    @Override
    public Double calculateLaborIncome(Long repairmanId) {
        List<RepairOrder> completedOrders = repairOrderRepository.findByRepairmanIdAndStatus(repairmanId, OrderStatus.valueOf(OrderStatus.COMPLETED.name()));
        return completedOrders.stream()
            .mapToDouble(RepairOrder::getLaborCost)
            .sum();
    }
    
    @Override
    public RepairmanDTO getCurrentUserInfo() {
        // 从Spring Security上下文中获取当前登录用户
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("用户未登录");
        }
        
        String username = authentication.getName();
        MaintenanceStaff staff = maintenanceStaffRepository.findByUsername(username);
        if (staff == null) {
            throw new RuntimeException("用户不存在");
        }
        
        RepairmanDTO dto = new RepairmanDTO();
        dto.setId(staff.getId());
        dto.setUsername(staff.getUsername());
        dto.setWorkType(staff.getWorkType());
        dto.setHourlyRate(staff.getHourlyRate().doubleValue());
        dto.setStatus(staff.getStatus());
        return dto;
    }
    
    // 辅助方法：转换实体到DTO
    private RepairOrderDTO convertToRepairOrderDTO(RepairOrder order) {
        RepairOrderDTO dto = new RepairOrderDTO();
        dto.setId(order.getId());
        dto.setRepairmanId(order.getRepairman().getId());
        dto.setStatus(order.getStatus().name());
        dto.setDescription(order.getDescription());
        dto.setCreateTime(order.getCreateTime());
        dto.setAcceptTime(order.getAcceptTime());
        dto.setCompleteTime(order.getCompleteTime());
        dto.setTotalHours(order.getTotalHours());
        dto.setLaborCost(order.getLaborCost());
        dto.setMaterialCost(order.getMaterialCost());
        dto.setRepairResult(order.getRepairResult());
        
        // 转换材料使用记录
        dto.setMaterialUsages(order.getMaterialUsages().stream()
            .map(this::convertToMaterialUsageDTO)
            .collect(Collectors.toList()));
            
        return dto;
    }
    
    private MaterialUsageDTO convertToMaterialUsageDTO(MaterialUsage usage) {
        MaterialUsageDTO dto = new MaterialUsageDTO();
        dto.setId(usage.getId());
        dto.setRepairOrderId(usage.getRepairOrder().getId());
        dto.setMaterialName(usage.getMaterialName());
        dto.setQuantity(usage.getQuantity().doubleValue());
        dto.setUnitPrice(usage.getUnitPrice().doubleValue());
        dto.setTotalPrice(usage.getTotalPrice().doubleValue());
        dto.setUsageDescription(usage.getUsageDescription());
        return dto;
    }
    
    private RepairProgressDTO convertToRepairProgressDTO(RepairProgress progress) {
        RepairProgressDTO dto = new RepairProgressDTO();
        dto.setId(progress.getId());
        dto.setRepairOrderId(progress.getRepairOrder().getId());
        dto.setProgress(progress.getProgress());
        dto.setStatus(progress.getStatus().name());
        dto.setUpdateTime(progress.getUpdateTime());
        dto.setRemark(progress.getRemark());
        return dto;
    }
} 