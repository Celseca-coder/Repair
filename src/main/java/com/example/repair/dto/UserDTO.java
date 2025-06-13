package com.example.repair.dto;

import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private String userType; // 用户类型：USER 或 MAINTENANCE
    private UserProfileDTO profile;

    @Data
    public static class UserProfileDTO {
        private Long id;
        private String phone;
        private String name;
        private String email;
        private String address;
    }
}
