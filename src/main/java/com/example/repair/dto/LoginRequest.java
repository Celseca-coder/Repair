package com.example.repair.dto;

import lombok.Data;

public record LoginRequest (
        String username,
        String password
){
}
