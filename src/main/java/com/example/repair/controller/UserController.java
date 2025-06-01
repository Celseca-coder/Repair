package com.example.repair.controller;

import com.example.repair.dto.UserProfileDTO;
import com.example.repair.dto.UserRegisterDTO;
import com.example.repair.dto.UserRegisterResponseDTO;
import com.example.repair.service.UserRegisterService;
import com.example.repair.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRegisterService userRegisterService;
    
    @Autowired
    private JwtUtil jwtUtil;

    public UserController(UserRegisterService userRegisterService) {
        this.userRegisterService = userRegisterService;
    }

    //用户注册
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserRegisterDTO request) {
        try {
            UserRegisterResponseDTO registeredUser = userRegisterService.registerUser(request);
            String token = jwtUtil.generateToken(request.getUsername());
            
            Map<String, Object> response = new HashMap<>();
            response.put("user", registeredUser);
            response.put("token", token);
            
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("注册过程中发生错误");
        }
    }
}
