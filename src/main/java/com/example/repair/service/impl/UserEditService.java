package com.example.repair.service.impl;

import com.example.repair.dto.UserDTO;
import com.example.repair.dto.UserEditRequest;
import com.example.repair.dto.UserProfileDTO;
import com.example.repair.dto.UserRegisterResponseDTO;
import com.example.repair.entity.User;
import com.example.repair.entity.UserProfile;
import com.example.repair.repository.UserProfileRepository;
import com.example.repair.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserEditService {
    private UserRepository userRepository;
    private UserProfileRepository userProfileRepository;

    public UserRegisterResponseDTO editUser(UserEditRequest request) {
        Long userId = request.getId(); // 确保转换为正确的类型
        User user = userRepository.findById(userId).orElse(null);
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        Long profileId = request.getProfile().getId();
        UserProfile profile = userProfileRepository.findById(profileId).orElse(null);
        profile.setPhone(request.getProfile().getPhone());
        profile.setName(request.getProfile().getName());
        profile.setEmail(request.getProfile().getEmail());
        profile.setAddress(request.getProfile().getAddress());
        userRepository.save(user);
        userProfileRepository.save(profile);

        UserProfileDTO profileDTO = new UserProfileDTO();
        profileDTO.setId(profile.getId());
        profileDTO.setPhone(profile.getPhone());
        profileDTO.setName(profile.getName());
        profileDTO.setEmail(profile.getEmail());
        profileDTO.setAddress(profile.getAddress());

        UserRegisterResponseDTO response = new UserRegisterResponseDTO();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setPassword(user.getPassword());
        response.setProfile(profileDTO);

        return response;
    }

}
