package com.example.repair.controller;

import com.example.repair.dto.*;
import com.example.repair.service.impl.UserEditService;
import com.example.repair.service.impl.UserLoginService;
import com.example.repair.service.impl.UserRegisterService;
import com.example.repair.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private JwtUtil jwtUtil;
    private final UserRegisterService userRegisterService;
    private final UserLoginService userLoginService;
    private final UserEditService userEditService;

    public UserController(
            UserRegisterService userRegisterService,
            UserLoginService userLoginService,
            UserEditService userEditService
    ) {
        this.userRegisterService = userRegisterService;
        this.userLoginService = userLoginService;
        this.userEditService = userEditService;
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


//用户登录
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request, HttpServletResponse response) {
        try {
            String token = userLoginService.login(request);
            // 创建 HttpOnly Cookie
            Cookie cookie = new Cookie("token", token);
            cookie.setHttpOnly(true); // 禁止前端 JavaScript 访问
            cookie.setSecure(true);   // 仅在 HTTPS 传输
            cookie.setPath("/");      // 适用于整个应用
            cookie.setMaxAge(3600);   // 1小时过期
            response.addCookie(cookie);
            return new LoginResponse(true, "登录成功", token);
        } catch (RuntimeException e) {
            return new LoginResponse(false, e.getMessage(), null);
        }
    }

    //注销
    @GetMapping("/logout")
    public LogoutResponse logout(HttpServletRequest request, HttpServletResponse response) {
        String username = userLoginService.logout(request, response);
        String message = username != null ? "用户 " + username + " 注销成功" : "注销成功";
        return new LogoutResponse(true, message);
    }

    // 查询修改个人账户信息
    @PostMapping("/editUser")
    public ResponseEntity<?> editUser(@RequestBody UserEditRequest request) {
        try {
            UserRegisterResponseDTO editUser = userEditService.editUser(request);
            String token = jwtUtil.generateToken(request.getUsername());

            Map<String, Object> response = new HashMap<>();
            response.put("user", editUser);
            response.put("token", token);

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("修改过程中发生错误");
        }
    }

}
