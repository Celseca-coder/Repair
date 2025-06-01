package com.example.repair.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "urge_record")
public class UrgeRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 关联订单
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private RepairOrder repairOrder;

    // 关联用户
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime urgeTime;
    private String remark; // 可选说明

}
