package com.example.repair.dto;

import com.example.repair.enums.UserRepairRequestStatus;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class RepairRequestDTO {
    private Long id;
    private Long userId;
    private String username;
    private Long vehicleId;
    private String vehicleInfo;  // 车辆信息（车牌号等）
    private String description;
    private UserRepairRequestStatus status;
    private LocalDateTime createdAt;
} 