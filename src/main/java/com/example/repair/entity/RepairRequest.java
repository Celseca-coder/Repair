package com.example.repair.entity;

import com.example.repair.enums.OrderStatus;
import com.example.repair.enums.UserRepairRequestStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "repair_requests")
public class RepairRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long vehicleId;

    @Column(nullable = true)
    private String description;

    @Enumerated(EnumType.STRING)
    private UserRepairRequestStatus status = UserRepairRequestStatus.UNREVIEWED;

    @Column(nullable = false)
    private LocalDateTime createdAt;

}
