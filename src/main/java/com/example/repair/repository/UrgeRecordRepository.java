package com.example.repair.repository;

import com.example.repair.entity.RepairOrder;
import com.example.repair.entity.UrgeRecord;
import com.example.repair.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UrgeRecordRepository extends JpaRepository<UrgeRecord, Long> {
    List<UrgeRecord> findByRepairOrderOrderByUrgeTimeDesc(RepairOrder repairOrder);
    UrgeRecord findTopByRepairOrderAndUserOrderByUrgeTimeDesc(RepairOrder repairOrder, User user);
}
