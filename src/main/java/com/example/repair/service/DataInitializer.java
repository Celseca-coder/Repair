package com.example.repair.service;

import com.example.repair.entity.Admin;
import com.example.repair.repository.AdminRepository; // 假设您有这个JPA Repository
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    // 使用构造函数注入
    public DataInitializer(AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // 检查数据库中是否已有管理员，避免重复创建
        if (adminRepository.count() == 0) {
            Admin admin = new Admin();
            admin.setUsername("admin");
            // 使用 PasswordEncoder 来加密密码
            admin.setPassword(passwordEncoder.encode("admin123"));

            adminRepository.save(admin);
            System.out.println("初始管理员账户已创建。");
        }
    }
}