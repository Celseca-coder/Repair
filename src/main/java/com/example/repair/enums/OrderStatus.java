package com.example.repair.enums;

public enum OrderStatus {
    PENDING("待接收"),
    ACCEPTED("已接收"),
    REJECTED("已拒绝"),
    IN_PROGRESS("维修中"),
    COMPLETED("已完成"),
    CANCELLED("已取消");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
} 