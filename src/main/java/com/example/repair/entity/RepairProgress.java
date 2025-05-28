package com.example.repair.entity;

import com.example.repair.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "repair_progress")
public class RepairProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "repair_order_id")
    private RepairOrder repairOrder;

    private String progress;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(name = "update_time")
    private LocalDateTime updateTime = LocalDateTime.now();

    private String remark;
} 