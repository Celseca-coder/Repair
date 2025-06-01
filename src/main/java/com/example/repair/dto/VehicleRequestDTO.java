package com.example.repair.dto;

import lombok.Data;

@Data
public class VehicleRequestDTO {
    private String username;
    private String licensePlate;
    private String brand;
    private String model;
    private Integer year;
    private String color;
    private String vin;
}
