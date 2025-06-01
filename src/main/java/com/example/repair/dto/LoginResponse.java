package com.example.repair.dto;

public record LoginResponse(
        boolean success,
        String message,
        String token) {
}
