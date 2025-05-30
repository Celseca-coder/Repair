package com.example.repair.dto;

import lombok.Data;

@Data
public class UserProfileDTO {
    private Long id;
    private String phone;
    private String name;
    private String email;
    private String address;
}