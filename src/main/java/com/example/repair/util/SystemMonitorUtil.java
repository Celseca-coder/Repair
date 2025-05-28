package com.example.repair.util;

import org.springframework.stereotype.Component;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.ThreadMXBean;
import java.util.HashMap;
import java.util.Map;

@Component
public class SystemMonitorUtil {
    
    public Map<String, Object> getSystemPerformance() {
        Map<String, Object> performance = new HashMap<>();
        
        // 获取操作系统信息
        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
        performance.put("osName", osBean.getName());
        performance.put("osVersion", osBean.getVersion());
        performance.put("osArch", osBean.getArch());
        performance.put("processorCount", osBean.getAvailableProcessors());
        performance.put("systemLoadAverage", osBean.getSystemLoadAverage());
        
        // 获取内存信息
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        performance.put("heapMemoryUsage", memoryBean.getHeapMemoryUsage());
        performance.put("nonHeapMemoryUsage", memoryBean.getNonHeapMemoryUsage());
        
        // 获取线程信息
        ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
        performance.put("threadCount", threadBean.getThreadCount());
        performance.put("peakThreadCount", threadBean.getPeakThreadCount());
        performance.put("totalStartedThreadCount", threadBean.getTotalStartedThreadCount());
        performance.put("daemonThreadCount", threadBean.getDaemonThreadCount());
        
        return performance;
    }
    
    public Map<String, Object> getJvmInfo() {
        Map<String, Object> jvmInfo = new HashMap<>();
        Runtime runtime = Runtime.getRuntime();
        
        jvmInfo.put("jvmVersion", System.getProperty("java.version"));
        jvmInfo.put("jvmVendor", System.getProperty("java.vendor"));
        jvmInfo.put("jvmName", System.getProperty("java.vm.name"));
        jvmInfo.put("jvmMaxMemory", runtime.maxMemory());
        jvmInfo.put("jvmTotalMemory", runtime.totalMemory());
        jvmInfo.put("jvmFreeMemory", runtime.freeMemory());
        jvmInfo.put("jvmUsedMemory", runtime.totalMemory() - runtime.freeMemory());
        
        return jvmInfo;
    }
} 