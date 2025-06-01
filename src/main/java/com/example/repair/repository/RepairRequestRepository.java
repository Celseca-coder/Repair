package com.example.repair.repository;

import com.example.repair.entity.RepairProgress;
import com.example.repair.entity.RepairRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepairRequestRepository extends JpaRepository<RepairRequest, Long> {
    List<RepairRequest> findByUserId(Long id);
}
