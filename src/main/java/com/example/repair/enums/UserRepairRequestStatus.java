package com.example.repair.enums;

public enum UserRepairRequestStatus {
    UNREVIEWED("未审核"),
    REJECTED("已拒绝"),
    ACCEPTED("已接受"),
    APPROVED("已批准");

    private final String description;

    UserRepairRequestStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
