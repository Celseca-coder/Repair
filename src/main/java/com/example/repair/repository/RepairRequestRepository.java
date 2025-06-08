package com.example.repair.repository;

import com.example.repair.entity.RepairRequest;
import com.example.repair.enums.UserRepairRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepairRequestRepository extends JpaRepository<RepairRequest, Long> {
    List<RepairRequest> findByStatus(UserRepairRequestStatus status);
    List<RepairRequest> findByUserId(Long userId);
}
