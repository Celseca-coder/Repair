package com.example.repair.enums;

public enum WorkType {
    PAINTER("漆工"),
    WELDER("焊工"),
    MECHANIC("机修"),
    ELECTRICIAN("电工"),
    PLUMBER("管道工"),
    CARPENTER("木工"),
    GENERAL("综合维修");

    private final String description;

    WorkType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
} 