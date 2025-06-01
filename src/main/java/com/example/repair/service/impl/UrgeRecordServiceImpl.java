package com.example.repair.service.impl;

import com.example.repair.entity.RepairOrder;
import com.example.repair.entity.UrgeRecord;
import com.example.repair.entity.User;
import com.example.repair.repository.UrgeRecordRepository;
import com.example.repair.service.UrgeRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UrgeRecordServiceImpl implements UrgeRecordService {

    private static final long MIN_URGE_INTERVAL_MINUTES = 60; // 1小时内只能催一次

    @Autowired
    private UrgeRecordRepository urgeRecordRepository;

    @Override
    public void addUrgeRecord(RepairOrder order, User user, String remark) {
        UrgeRecord record = new UrgeRecord();
        record.setRepairOrder(order);
        record.setUser(user);
        record.setUrgeTime(LocalDateTime.now());
        record.setRemark(remark);
        urgeRecordRepository.save(record);
        // 可在此扩展消息通知
    }

    @Override
    public boolean isUrgedRecently(RepairOrder order, User user) {
        UrgeRecord lastUrge = urgeRecordRepository.findTopByRepairOrderAndUserOrderByUrgeTimeDesc(order, user);
        if (lastUrge == null) {
            return false;
        }
        LocalDateTime now = LocalDateTime.now();
        return lastUrge.getUrgeTime().plusMinutes(MIN_URGE_INTERVAL_MINUTES).isAfter(now);
    }

    @Override
    public List<UrgeRecord> getUrgeRecordsByOrder(RepairOrder order) {
        return urgeRecordRepository.findByRepairOrderOrderByUrgeTimeDesc(order);
    }
}
