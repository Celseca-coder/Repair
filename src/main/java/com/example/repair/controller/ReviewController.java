package com.example.repair.controller;

import com.example.repair.dto.ReviewAddDTO;
import com.example.repair.dto.VehicleRepairRequest;
import com.example.repair.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reviews")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    @PostMapping("/addReviews")
    public ResponseEntity<?> addReviews(@Valid @RequestBody ReviewAddDTO request) {
        try {
            reviewService.addReview(request);
            return ResponseEntity.ok("评论添加成功");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("评论添加失败");
        }
    }
}
