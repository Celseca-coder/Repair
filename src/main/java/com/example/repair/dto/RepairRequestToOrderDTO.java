package com.example.repair.dto;

import lombok.Data;

@Data
public class RepairRequestToOrderDTO {
    private Long requestId;        // 维修请求ID
    private Long repairmanId;      // 分配的维修人员ID
    private String description;    // 维修描述（可选，如果不提供则使用请求中的描述）
} 