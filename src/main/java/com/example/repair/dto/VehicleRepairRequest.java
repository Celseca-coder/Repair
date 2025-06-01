package com.example.repair.dto;

import lombok.Data;

@Data
public class VehicleRepairRequest {
    private Long vehicleId;
    private String username;
    private String description;
}
