package com.example.repair.service.impl;

import com.example.repair.dto.ReviewAddDTO;
import com.example.repair.entity.Review;
import com.example.repair.repository.ReviewRepository;
import com.example.repair.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ReviewServiceImpl implements ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    @Override
    public void addReview(ReviewAddDTO request) {
        if (request.getRating() == null) {
            throw new IllegalArgumentException("评分不能为空");
        }
        if (request.getComment() == null) {
            throw new IllegalArgumentException("评论不能为空");
        }
        if (request.getRepairOrderId() == null) {
            throw new IllegalArgumentException("维修订单ID不能为空");
        }
        if (request.getUserId() == null) {
            throw new IllegalArgumentException("用户ID不能为空");
        }
        if (request.getRating() < 1 || request.getRating() > 5) {
            throw new IllegalArgumentException("评分必须在1-5之间");
        }

        Review review = new Review();
        review.setUserId(request.getUserId());
        review.setRepairOrderId(request.getRepairOrderId());
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        review.setCreatedAt(LocalDateTime.now());
        reviewRepository.save(review);
    }
}
