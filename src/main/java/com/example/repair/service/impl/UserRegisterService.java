package com.example.repair.service.impl;

import com.example.repair.dto.UserProfileDTO;
import com.example.repair.dto.UserRegisterDTO;
import com.example.repair.dto.UserRegisterResponseDTO;
import com.example.repair.entity.User;
import com.example.repair.entity.UserProfile;
import com.example.repair.repository.UserProfileRepository;
import com.example.repair.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserRegisterService {
    @Autowired
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    public UserRegisterService(
            UserRepository userRepository,
            UserProfileRepository userProfileRepository
    ) {
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
    }
    public UserRegisterResponseDTO registerUser(UserRegisterDTO request) {
        // 1. 校验用户名格式
        validateUsernameFormat(request.getUsername());

        // 2. 检查用户名是否存在
        checkUsernameExistence(request.getUsername());

        // 检查用户密码
        validatePasswordFormat(request.getPassword());

        // 3.检查用户名格式
        validateProfilenameFormat(request.getProfile().getName());
        
        // 4.检验电话号码格式
        validatePhoneFormat(request.getProfile().getPhone());
        
        // 5.检验邮箱格式
        validateEmailFormat(request.getProfile().getEmail());
        
        // 6.检验地址格式
        validateAddressFormat(request.getProfile().getAddress());

        // 创建并保存用户
        User user = createAndSaveUser(request);

        // 创建并记录用户信息
        UserProfile profile = createAndSaveUserProfile(user, request.getProfile());

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

    private UserProfile createAndSaveUserProfile(User user,UserRegisterDTO.UserProfileDTO profile) {
        UserProfile userProfile = new UserProfile();
        userProfile.setUser(user);
        userProfile.setPhone(profile.getPhone());
        userProfile.setName(profile.getName());
        userProfile.setEmail(profile.getEmail());
        userProfile.setAddress(profile.getAddress());
        return userProfileRepository.save(userProfile);
    }

    private void validateAddressFormat(String address) {
        if (address == null || address.trim().isEmpty()) {
            throw new IllegalArgumentException("地址不能为空。");
        }

        // 定义地址格式的正则表达式（可根据具体要求调整）
        String addressRegex = "^[\\u4e00-\\u9fa5a-zA-Z0-9,\\s\\.\\-]{5,100}$";

        // 检查是否符合地址格式
        if (address.matches(addressRegex)) {
            return; // 符合要求，验证通过
        }

        // 抛出异常
        throw new IllegalArgumentException("地址格式不正确。");
    }

    private void validateEmailFormat(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("电子邮件地址不能为空。");
        }

        // 定义电子邮件格式的正则表达式
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

        // 检查是否符合电子邮件格式
        if (email.matches(emailRegex)) {
            return; // 符合要求，验证通过
        }

        // 抛出异常
        throw new IllegalArgumentException("电子邮件地址格式不正确。");
    }

    private void validatePhoneFormat(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            throw new IllegalArgumentException("手机号码不能为空。");
        }

        // 检查是否为11位数字
        if (phone.matches("\\d{11}")) {
            return; // 符合要求，验证通过
        }

        // 抛出异常
        throw new IllegalArgumentException("手机号码必须是11位数字。");
    }

    private void validateProfilenameFormat(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("姓名不能为空");
        }

        // 检查是否只包含中文字符
        if (name.matches("[\\u4e00-\\u9fa5]+")) {
            return; // 只包含中文字符，验证通过
        }

        // 抛出异常
        throw new IllegalArgumentException("真实姓名必须为中文字符");
    }

    private void validateUsernameFormat(String username) {
        if (!username.matches("^[a-zA-Z0-9_]{4,20}$")) {
            throw new IllegalArgumentException("用户名只能包含数字、字母、下划线，长度4-20个字符");
        }
    }

    private void checkUsernameExistence(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("用户名已存在");
        }
    }

    // 辅助方法：校验密码格式
    private void validatePasswordFormat(String password) {
        if (!password.matches("^(?=.*[a-zA-Z])(?=.*\\d).{6,20}$")) {
            throw new IllegalArgumentException("密码必须包含数字和字母，长度6-20个字符");
        }
    }

    // 辅助方法：创建并保存用户
    private User createAndSaveUser(UserRegisterDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        return userRepository.save(user);
    }

}
