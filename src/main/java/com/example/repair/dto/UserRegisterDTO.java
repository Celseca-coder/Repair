package com.example.repair.dto;

import lombok.Data;

@Data
public class UserRegisterDTO {
    private String username;
    private String password;
    private UserProfileDTO profile;

    @Data
    public static class UserProfileDTO {
        private String phone;
        private String name;
        private String email;
        private String address;
    }
} 