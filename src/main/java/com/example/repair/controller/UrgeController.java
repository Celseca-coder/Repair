package com.example.repair.controller;

import com.example.repair.dto.UrgeRequestDTO;
import com.example.repair.entity.RepairOrder;
import com.example.repair.entity.User;
import com.example.repair.repository.RepairOrderRepository;
import com.example.repair.repository.UserRepository;
import com.example.repair.service.UrgeRecordService;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class UrgeController {

    @Autowired
    private RepairOrderRepository repairOrderRepository;
    @Autowired
    private UrgeRecordService urgeRecordService;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/{orderId}/urge")
    public ResponseEntity<?> urgeOrder(@PathVariable Long orderId,
                                       @RequestBody(required = false) UrgeRequestDTO request
                                       ) {
        User user = userRepository.findByUsername(request.getUsername()).orElse(null);
        RepairOrder order = repairOrderRepository.findById(orderId).orElse(null);
        if (!order.getVehicle().getOwner().getId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("不允许催别人的订单");
        }
        // 频率限制（如1小时内只能催一次）
        if (urgeRecordService.isUrgedRecently(order, user)) {
            return ResponseEntity.badRequest().body("催单太频繁，请稍后再试");
        }
        urgeRecordService.addUrgeRecord(order, user, request == null ? null : request.getRemark());
        // 可在此触发通知
        return ResponseEntity.ok("催单成功");
    }
}
