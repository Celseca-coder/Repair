package com.example.repair.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class RepairProgressDTO {
    private Long id;
    private Long repairOrderId;
    private String progress; // 进度描述
    private String status; // 当前状态
    private LocalDateTime updateTime;
    private String remark; // 备注
} 