package com.example.repair.controller;

import com.example.repair.dto.ReviewAddDTO;
import com.example.repair.dto.VehicleRepairRequest;
import com.example.repair.entity.User;
import com.example.repair.repository.UserRepository;
import com.example.repair.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/reviews")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    @Autowired
    private UserRepository userRepository;
    /**
     * 新增评论 (已更新)
     * @param reviewAddDto 从请求体中获取的评论数据传输对象 (不包含 userId)
     * @param principal Spring Security 自动注入的当前登录用户信息
     * @return 成功或失败的响应
     */
    @PostMapping("/addReviews")
    public ResponseEntity<?> addReview(@RequestBody ReviewAddDTO reviewAddDto, Principal principal) {
        // 1. 确保用户已登录
        if (principal == null) {
            return ResponseEntity.status(401).body("用户未登录");
        }

        try {
            // 2. 从 principal 获取用户名，并查询用户以获得其 ID
            String username = principal.getName();
            User currentUser = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("当前登录用户不存在: " + username));

            // 3. 将从安全上下文中获取的用户 ID 设置到 DTO 中
            reviewAddDto.setUserId(currentUser.getId());

            // 4. 调用服务层方法，该方法负责校验和保存。
            //    服务层中的 IllegalArgumentException 会在此被捕获。
            reviewService.addReview(reviewAddDto);

            // 5. 由于服务层返回 void, 我们直接返回一个成功的响应
            return ResponseEntity.ok("评论添加成功");

        } catch (IllegalArgumentException | UsernameNotFoundException e) {
            // 捕获业务逻辑或验证异常，并返回一个带有错误信息的 bad request 响应
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
