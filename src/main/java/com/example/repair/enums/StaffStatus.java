package com.example.repair.enums;

public enum StaffStatus {
    IDLE("空闲"),
    BUSY("工作中"),
    ON_LEAVE("休假"),
    OFF_DUTY("下班");

    private final String description;

    StaffStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
} 