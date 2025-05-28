package com.example.repair.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class RepairOrderDTO {
    private Long id;
    private Long repairmanId;
    private String status; // 状态：待接收、已接收、已拒绝、维修中、已完成
    private String description; // 维修描述
    private LocalDateTime createTime;
    private LocalDateTime acceptTime;
    private LocalDateTime completeTime;
    private Double totalHours; // 总工时
    private Double laborCost; // 工时费
    private Double materialCost; // 材料费
    private String repairResult; // 维修结果
    private List<MaterialUsageDTO> materialUsages; // 使用的材料列表
} 