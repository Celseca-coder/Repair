package com.example.repair.dto;

import lombok.Data;

@Data
public class MaterialUsageDTO {
    private Long id;
    private Long repairOrderId;
    private String materialName; // 材料名称
    private Double quantity; // 使用数量
    private Double unitPrice; // 单价
    private Double totalPrice; // 总价
    private String usageDescription; // 使用说明
} 