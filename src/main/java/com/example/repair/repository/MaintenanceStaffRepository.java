package com.example.repair.repository;

import com.example.repair.entity.MaintenanceStaff;
import com.example.repair.enums.StaffStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MaintenanceStaffRepository extends JpaRepository<MaintenanceStaff, Long> {
    boolean existsByUsername(String username);
    MaintenanceStaff findByUsername(String username);
    List<MaintenanceStaff> findByStatus(StaffStatus status);
} 