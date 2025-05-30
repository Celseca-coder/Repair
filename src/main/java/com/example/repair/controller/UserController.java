package com.example.repair.controller;

import com.example.repair.dto.UserProfileDTO;
import com.example.repair.dto.UserRegisterDTO;
import com.example.repair.dto.UserRegisterResponseDTO;
import com.example.repair.service.UserRegisterService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRegisterService userRegisterService;

    public UserController(UserRegisterService userRegisterService) {
        this.userRegisterService = userRegisterService;
    }

    //用户注册
    @PostMapping("/register")
    public UserRegisterResponseDTO registerUserCheckCaptcha(@Valid @RequestBody UserRegisterDTO request) {
        return userRegisterService.registerUser(request);
    }
}
