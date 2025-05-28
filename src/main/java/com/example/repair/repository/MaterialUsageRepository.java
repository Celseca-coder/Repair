package com.example.repair.repository;

import com.example.repair.entity.MaterialUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MaterialUsageRepository extends JpaRepository<MaterialUsage, Long> {
    List<MaterialUsage> findByRepairOrderId(Long repairOrderId);
} 