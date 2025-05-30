package com.example.repair.dto;

import lombok.Data;

@Data
public class UserRegisterResponseDTO {
    private String username;
    private String password;
    private UserProfileDTO profile;

}
