package com.example.repair.repository;

import com.example.repair.entity.MaintenanceStaff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MaintenanceStaffRepository extends JpaRepository<MaintenanceStaff, Long> {
    boolean existsByUsername(String username);
    MaintenanceStaff findByUsername(String username);
} 