package com.example.repair.dto;

import lombok.Data;

@Data
public class ReviewAddDTO {
    private Long userId;
    private Long repairOrderId;
    private Integer rating;
    private String comment;
}
