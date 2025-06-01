package com.example.repair.service.impl;

import com.example.repair.entity.MaintenanceStaff;
import com.example.repair.entity.User;
import com.example.repair.repository.MaintenanceStaffRepository;
import com.example.repair.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private MaintenanceStaffRepository maintenanceStaffRepository;
    
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 首先尝试查找维修人员
        MaintenanceStaff staff = maintenanceStaffRepository.findByUsername(username);
        if (staff != null) {
            return new org.springframework.security.core.userdetails.User(
                staff.getUsername(),
                staff.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_REPAIRMAN"))
            );
        }
        
        // 如果找不到维修人员，尝试查找普通用户
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("用户不存在: " + username));
            
        return new org.springframework.security.core.userdetails.User(
            user.getUsername(),
            user.getPassword(),
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }
} 