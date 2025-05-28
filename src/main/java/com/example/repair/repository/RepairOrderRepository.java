package com.example.repair.repository;

import com.example.repair.entity.RepairOrder;
import com.example.repair.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RepairOrderRepository extends JpaRepository<RepairOrder, Long> {
    List<RepairOrder> findByRepairmanId(Long repairmanId);
    List<RepairOrder> findByRepairmanIdAndStatus(Long repairmanId, OrderStatus status);
    List<RepairOrder> findByStatus(OrderStatus status);
    List<RepairOrder> findByCompleteTimeBetween(LocalDateTime start, LocalDateTime end);
    long countByCreateTimeBetween(LocalDateTime start, LocalDateTime end);
    long countByStatusAndCompleteTimeBetween(OrderStatus status, LocalDateTime start, LocalDateTime end);
    long countByRepairmanIdAndCreateTimeBetween(Long repairmanId, LocalDateTime start, LocalDateTime end);
    long countByStatus(OrderStatus status);
    boolean existsByRepairmanIdAndStatus(Long repairmanId, OrderStatus status);
} 