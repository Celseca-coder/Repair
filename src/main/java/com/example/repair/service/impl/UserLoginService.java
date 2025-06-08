package com.example.repair.service.impl;

import com.example.repair.dto.LoginRequest;
import com.example.repair.dto.UserProfileDTO;
import com.example.repair.dto.UserRegisterResponseDTO;
import com.example.repair.entity.User;
import com.example.repair.entity.UserLoginRecord;
import com.example.repair.entity.UserProfile;
import com.example.repair.repository.UserLoginRecordRepository;
import com.example.repair.repository.UserProfileRepository;
import com.example.repair.repository.UserRepository;
import com.example.repair.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class UserLoginService {
    @Autowired
    private final UserRepository userRepository;
    private final UserLoginRecordRepository userLoginRecordRepository;
    private final UserProfileRepository userProfileRepository;
    private final JwtUtil jwtUtil;
    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserLoginService(
            UserRepository userRepository,
            UserLoginRecordRepository userLoginRecordRepository,
            UserProfileRepository userProfileRepository,
            JwtUtil jwtUtil
    ) {
        this.userRepository = userRepository;
        this.userLoginRecordRepository = userLoginRecordRepository;
        this.userProfileRepository = userProfileRepository;
        this.jwtUtil = jwtUtil;
    }

    public String login(LoginRequest request) {
        //Optional<User> userOptional = userRepository.findByUsername(request.username());
        User user = userRepository.findByUsername(request.username()).orElse(null);
        if (user != null) {
            //User user = userOptional.get();
            if (checkPassword(request.password(), user.getPassword())) {
                // 调用 JwtTokenUtil 生成 Token
                String token = jwtUtil.generateToken(user.getUsername());
                UserLoginRecord record = new UserLoginRecord(user.getUsername(), token, new Date());
                record.setLoginTime(new Date());
                userLoginRecordRepository.save(record);
                return token;
            }
            throw new RuntimeException(user.getPassword());
        }
        throw new RuntimeException("昵称不存在");
    }

    public boolean checkPassword(String password,String encryptedPassword){
        return passwordEncoder.matches(password, encryptedPassword);
    }

    public String logout(HttpServletRequest request, HttpServletResponse response) {
        String token = jwtUtil.getTokenFromRequest(request);

        if (token == null) {
            throw new RuntimeException("未提供有效的 Token");
        }

        try {
            // 解析 token，确认有效性
            String username = jwtUtil.extractUsername(token);

            // 如果 Token 已经过期，直接返回
            if (jwtUtil.isTokenExpired(token)) {
                throw new RuntimeException("Token 已经过期");
            }
            //记录登出时间
            Optional<UserLoginRecord> recordOptional = userLoginRecordRepository.findByToken(token);
            if (recordOptional.isPresent()) {
                UserLoginRecord record = recordOptional.get();
                record.setLogoutTime(new Date());
                userLoginRecordRepository.save(record);
            }


            // 删除 HttpOnly Cookie
            Cookie cookie = new Cookie("token", null);
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setPath("/");
            cookie.setMaxAge(0); // 立即过期
            response.addCookie(cookie);
            return  username ;
        } catch (Exception e) {
            throw new RuntimeException("无效的 Token: " + e.getMessage());
        }

    }

    public UserRegisterResponseDTO getUserInfo(HttpServletRequest request) {
        String token = jwtUtil.getTokenFromRequest(request);
        if (token == null) {
            throw new IllegalArgumentException("未提供有效的Token");
        }

        try {
            String username = jwtUtil.extractUsername(token);
            if (jwtUtil.isTokenExpired(token)) {
                throw new IllegalArgumentException("Token已过期");
            }

            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new IllegalArgumentException("用户不存在"));

            UserProfile profile = userProfileRepository.findByUserId(user.getId())
                    .orElse(null);

            UserProfileDTO profileDTO = new UserProfileDTO();
            if (profile != null) {
                profileDTO.setId(profile.getId());
                profileDTO.setPhone(profile.getPhone());
                profileDTO.setName(profile.getName());
                profileDTO.setEmail(profile.getEmail());
                profileDTO.setAddress(profile.getAddress());
            }

            UserRegisterResponseDTO response = new UserRegisterResponseDTO();
            response.setId(user.getId());
            response.setUsername(user.getUsername());
            response.setPassword(null); // 不返回密码
            response.setProfile(profileDTO);

            return response;
        } catch (Exception e) {
            throw new IllegalArgumentException("获取用户信息失败: " + e.getMessage());
        }
    }
}
