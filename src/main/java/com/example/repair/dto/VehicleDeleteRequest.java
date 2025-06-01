package com.example.repair.dto;

import lombok.Data;

@Data
public class VehicleDeleteRequest {
    private String username;
    private Long vehicleId;
}
