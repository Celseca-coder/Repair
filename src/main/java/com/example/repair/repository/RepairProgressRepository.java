package com.example.repair.repository;

import com.example.repair.entity.RepairProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RepairProgressRepository extends JpaRepository<RepairProgress, Long> {
    List<RepairProgress> findByRepairOrderIdOrderByUpdateTimeDesc(Long repairOrderId);
} 