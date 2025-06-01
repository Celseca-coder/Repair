package com.example.repair.dto;

import lombok.Data;

@Data
public class VehicleEditRequest {
    private String username;
    private Long vehicleId;
    private String licencePlate;
    private String brand;
    private String model;
    private Integer year;
    private String color;
    private String vin;
}
