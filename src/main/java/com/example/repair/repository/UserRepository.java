package com.example.repair.repository;

import com.example.repair.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    long countByCreateTimeBetween(LocalDateTime start, LocalDateTime end);
    long countByLastLoginTimeBetween(LocalDateTime start, LocalDateTime end);
} 