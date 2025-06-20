package com.example.repair.entity;

import com.example.repair.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "repair_orders")
public class RepairOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "repairman_id")
    private MaintenanceStaff repairman;

    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.PENDING;

    private String description;

    @Column(name = "create_time")
    private LocalDateTime createTime = LocalDateTime.now();

    @Column(name = "accept_time")
    private LocalDateTime acceptTime;

    @Column(name = "complete_time")
    private LocalDateTime completeTime;

    @Column(name = "total_hours")
    private Double totalHours;

    @Column(name = "labor_cost")
    private Double laborCost;

    @Column(name = "material_cost")
    private Double materialCost;

    @Column(name = "repair_result")
    private String repairResult;

    @OneToMany(mappedBy = "repairOrder", cascade = CascadeType.ALL)
    private List<MaterialUsage> materialUsages = new ArrayList<>();

    @OneToMany(mappedBy = "repairOrder", cascade = CascadeType.ALL)
    private List<RepairProgress> progressRecords = new ArrayList<>();

    @OneToMany(mappedBy = "repairOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UrgeRecord> urgeRecords = new ArrayList<>();
} 