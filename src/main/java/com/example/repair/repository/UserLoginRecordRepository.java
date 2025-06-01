package com.example.repair.repository;

import com.example.repair.entity.UserLoginRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserLoginRecordRepository extends JpaRepository<UserLoginRecord, Long> {

    // 查询指定的 Token 的登录记录
    Optional<UserLoginRecord> findByToken(String token);

    // 查询所有未注销的用户登录记录（logoutTime 为 null）
    List<UserLoginRecord> findByLogoutTimeIsNull();

    // 检查指定 Token 是否未注销
    boolean existsByTokenAndLogoutTimeIsNull(String token);
}
