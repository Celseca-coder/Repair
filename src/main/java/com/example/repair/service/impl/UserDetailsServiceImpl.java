package com.example.repair.service.impl;

import com.example.repair.entity.MaintenanceStaff;
import com.example.repair.repository.MaintenanceStaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private MaintenanceStaffRepository maintenanceStaffRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MaintenanceStaff staff = maintenanceStaffRepository.findByUsername(username);
        if (staff == null) {
            throw new UsernameNotFoundException("用户不存在: " + username);
        }
        
        // 创建用户详情，这里我们给所有维修人员一个"ROLE_REPAIRMAN"角色
        return new User(
            staff.getUsername(),
            staff.getPassword(),
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_REPAIRMAN"))
        );
    }
} 