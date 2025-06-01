package com.example.repair.service;

import com.example.repair.entity.RepairOrder;
import com.example.repair.entity.UrgeRecord;
import com.example.repair.entity.User;

import java.util.List;

public interface UrgeRecordService {
    /**
     * 新增催单记录
     */
    void addUrgeRecord(RepairOrder order, User user, String remark);

    /**
     * 查询该用户对该订单最近一次催单时间是否过于频繁
     */
    boolean isUrgedRecently(RepairOrder order, User user);

    /**
     * 查询某订单的全部催单记录
     */
    List<UrgeRecord> getUrgeRecordsByOrder(RepairOrder order);
}
