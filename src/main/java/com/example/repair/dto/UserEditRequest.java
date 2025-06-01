package com.example.repair.dto;

import lombok.Data;

@Data
public class UserEditRequest {
    private Long id;
    private String username;
    private String password;
    private UserDTO.UserProfileDTO profile;
}
