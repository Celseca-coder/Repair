package com.example.repair.service;

import com.example.repair.dto.*;
import java.util.List;

public interface RepairmanService {
    // 注册
    RepairmanDTO register(RepairmanDTO repairmanDTO);
    
    // 登录
    RepairmanDTO login(String username, String password);
    
    // 获取维修人员信息
    RepairmanDTO getRepairmanInfo(Long repairmanId);
    
    // 接收维修工单
    RepairOrderDTO acceptRepairOrder(Long repairmanId, Long orderId);
    
    // 拒绝维修工单
    RepairOrderDTO rejectRepairOrder(Long repairmanId, Long orderId);
    
    // 记录材料使用
    MaterialUsageDTO recordMaterialUsage(MaterialUsageDTO materialUsage);
    
    // 更新维修进度
    RepairProgressDTO updateRepairProgress(RepairProgressDTO progress);
    
    // 更新维修结果
    RepairOrderDTO updateRepairResult(Long repairmanId, Long orderId, String result);
    
    // 查询历史维修记录
    List<RepairOrderDTO> getRepairHistory(Long repairmanId);
    
    // 计算工时费收入
    Double calculateLaborIncome(Long repairmanId);
    
    // 获取当前登录用户信息
    RepairmanDTO getCurrentUserInfo();
} 