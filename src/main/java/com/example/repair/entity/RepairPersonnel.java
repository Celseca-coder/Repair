package com.example.repair.entity;

import jakarta.persistence.*; // 或 javax.persistence for older Spring Boot versions
import lombok.Data; // 如果使用 Lombok

@Entity
@Data // Lombok
public class RepairPersonnel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password; // 需要加密存储
    private String fullName;
    private String skillSet; // 工种
    private Double hourlyRate; // 时薪
    // ... 其他字段
    // 考虑添加角色字段，如 "ROLE_REPAIR_PERSONNEL"
}
