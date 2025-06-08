package com.example.repair.service.impl;

import com.example.repair.dto.UserDTO;
import com.example.repair.dto.UserEditRequest;
import com.example.repair.dto.UserProfileDTO;
import com.example.repair.dto.UserRegisterResponseDTO;
import com.example.repair.entity.User;
import com.example.repair.entity.UserProfile;
import com.example.repair.repository.UserProfileRepository;
import com.example.repair.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserEditService {
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserProfileRepository userProfileRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserRegisterResponseDTO editUser(UserEditRequest request) {
        Long userId = request.getId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"));

        // 更新用户基本信息
        user.setUsername(request.getUsername());
        if (request.getPassword() != null && !request.getPassword().trim().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        // 更新用户个人资料
        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElseGet(() -> {
                    UserProfile newProfile = new UserProfile();
                    newProfile.setUser(user);
                    return newProfile;
                });

        if (request.getProfile() != null) {
            profile.setPhone(request.getProfile().getPhone());
            profile.setName(request.getProfile().getName());
            profile.setEmail(request.getProfile().getEmail());
            profile.setAddress(request.getProfile().getAddress());
        }

        // 保存更新
        User savedUser = userRepository.save(user);
        UserProfile savedProfile = userProfileRepository.save(profile);

        // 构建响应DTO
        UserProfileDTO profileDTO = new UserProfileDTO();
        profileDTO.setId(savedProfile.getId());
        profileDTO.setPhone(savedProfile.getPhone());
        profileDTO.setName(savedProfile.getName());
        profileDTO.setEmail(savedProfile.getEmail());
        profileDTO.setAddress(savedProfile.getAddress());

        UserRegisterResponseDTO response = new UserRegisterResponseDTO();
        response.setId(savedUser.getId());
        response.setUsername(savedUser.getUsername());
        response.setPassword(null); // 不返回密码
        response.setProfile(profileDTO);

        return response;
    }
}
