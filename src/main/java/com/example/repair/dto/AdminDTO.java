package com.example.repair.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.Map;

@Data
public class AdminDTO {
    private Long id;
    private String username;
    private String name;
    private String email;
    private LocalDateTime lastLoginTime;
    private String role = "ROLE_ADMIN";
    
    // 统计信息
    private SystemStatisticsDTO systemStatistics;
    private RepairStatisticsDTO repairStatistics;
    private FinancialStatisticsDTO financialStatistics;
    
    @Data
    public static class SystemStatisticsDTO {
        private Long totalUsers;
        private Long totalRepairmen;
        private Long totalVehicles;
        private Long totalOrders;
        private Long activeOrders;
        private Long completedOrders;
    }
    
    @Data
    public static class RepairStatisticsDTO {
        private Long ordersLastMonth;
        private Long completedOrdersLastMonth;
        private Double averageRepairTime;
        private Map<Long, Integer> repairmanWorkload;
        private Map<String, Long> repairTypeDistribution;
    }
    
    @Data
    public static class FinancialStatisticsDTO {
        private Double totalIncome;
        private Double totalLaborCost;
        private Double totalMaterialCost;
        private Double netIncome;
        private Map<String, Double> monthlyIncome;
    }
} 