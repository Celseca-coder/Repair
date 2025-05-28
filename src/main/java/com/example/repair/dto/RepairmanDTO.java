package com.example.repair.dto;

import com.example.repair.enums.StaffStatus;
import com.example.repair.enums.WorkType;
import lombok.Data;

@Data
public class RepairmanDTO {
    private Long id;
    private String username;
    private String password;
    private WorkType workType; // 工种
    private Double hourlyRate; // 时薪
    private StaffStatus status; // 状态
} 