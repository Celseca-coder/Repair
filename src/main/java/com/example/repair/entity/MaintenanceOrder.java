package com.example.repair.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class MaintenanceOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;
    private String status; // ASSIGNED, ACCEPTED, COMPLETED, etc.

    @ManyToOne
    @JoinColumn(name = "assigned_to")
    private MaintenanceStaff assignedTo;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String result;

    @OneToMany(mappedBy = "order")
    private List<OrderMaterial> materials = new ArrayList<>();
}
