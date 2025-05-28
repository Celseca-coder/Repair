package com.example.repair.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class VehicleDTO {
    private Long id;
    private String licensePlate;
    private String brand;
    private String model;
    private Integer year;
    private String color;
    private String vin;
    private LocalDateTime lastMaintenanceDate;
    private Long ownerId;
    private String ownerName;
} 