package com.example.repair.repository;

import com.example.repair.entity.RepairRequest;
import com.example.repair.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
