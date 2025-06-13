package com.example.repair.service.impl;

import com.example.repair.dto.*;
import com.example.repair.entity.*;
import com.example.repair.enums.OrderStatus;
import com.example.repair.enums.StaffStatus;
import com.example.repair.enums.UserRepairRequestStatus;
import com.example.repair.repository.*;
import com.example.repair.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminRepository adminRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserProfileRepository userProfileRepository;
    
    @Autowired
    private MaintenanceStaffRepository repairmanRepository;
    
    @Autowired
    private VehicleRepository vehicleRepository;
    
    @Autowired
    private RepairOrderRepository repairOrderRepository;
    
    @Autowired
    private RepairRequestRepository repairRequestRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public AdminDTO login(String username, String password) {
        Admin admin = adminRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("管理员不存在"));
                
        if (!passwordEncoder.matches(password, admin.getPassword())) {
            throw new RuntimeException("密码错误");
        }
        
        return convertToAdminDTO(admin);
    }

    // 用户管理
    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToUserDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO getUserById(Long userId) {
        return userRepository.findById(userId)
                .map(this::convertToUserDTO)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
    }

    @Override
    @Transactional
    public UserDTO updateUser(Long userId, UserDTO userDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        User finalUser = user;
        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElseGet(() -> {
                    UserProfile newProfile = new UserProfile();
                    newProfile.setUser(finalUser);
                    return newProfile;
                });
                
        if (userDTO.getProfile() != null) {
            UserDTO.UserProfileDTO profileDTO = userDTO.getProfile();
            profile.setPhone(profileDTO.getPhone());
            profile.setName(profileDTO.getName());
            profile.setEmail(profileDTO.getEmail());
            profile.setAddress(profileDTO.getAddress());
            userProfileRepository.save(profile);
        }
        
        user = userRepository.save(user);
        return convertToUserDTO(user);
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("用户不存在");
        }
        userRepository.deleteById(userId);
    }

    // 维修人员管理
    @Override
    public List<RepairmanDTO> getAllRepairmen() {
        return repairmanRepository.findAll().stream()
                .map(this::convertToRepairmanDTO)
                .collect(Collectors.toList());
    }

    @Override
    public RepairmanDTO getRepairmanById(Long repairmanId) {
        return repairmanRepository.findById(repairmanId)
                .map(this::convertToRepairmanDTO)
                .orElseThrow(() -> new RuntimeException("维修人员不存在"));
    }

    @Override
    @Transactional
    public RepairmanDTO updateRepairman(Long repairmanId, RepairmanDTO repairmanDTO) {
        MaintenanceStaff staff = repairmanRepository.findById(repairmanId)
                .orElseThrow(() -> new RuntimeException("维修人员不存在"));
                
        staff.setWorkType(repairmanDTO.getWorkType());
        staff.setHourlyRate(BigDecimal.valueOf(repairmanDTO.getHourlyRate()));
        staff.setStatus(repairmanDTO.getStatus());
        
        staff = repairmanRepository.save(staff);
        return convertToRepairmanDTO(staff);
    }

    @Override
    @Transactional
    public void deleteRepairman(Long repairmanId) {
        if (!repairmanRepository.existsById(repairmanId)) {
            throw new RuntimeException("维修人员不存在");
        }
        repairmanRepository.deleteById(repairmanId);
    }

    // 车辆管理
    @Override
    public List<VehicleDTO> getAllVehicles() {
        return vehicleRepository.findAll().stream()
                .map(this::convertToVehicleDTO)
                .collect(Collectors.toList());
    }

    @Override
    public VehicleDTO getVehicleById(Long vehicleId) {
        return vehicleRepository.findById(vehicleId)
                .map(this::convertToVehicleDTO)
                .orElseThrow(() -> new RuntimeException("车辆不存在"));
    }

    @Override
    @Transactional
    public VehicleDTO updateVehicle(Long vehicleId, VehicleDTO vehicleDTO) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new RuntimeException("车辆不存在"));
                
        vehicle.setLicensePlate(vehicleDTO.getLicensePlate());
        vehicle.setBrand(vehicleDTO.getBrand());
        vehicle.setModel(vehicleDTO.getModel());
        vehicle.setYear(vehicleDTO.getYear());
        vehicle.setColor(vehicleDTO.getColor());
        vehicle.setVin(vehicleDTO.getVin());
        vehicle.setLastMaintenanceDate(vehicleDTO.getLastMaintenanceDate());
        
        vehicle = vehicleRepository.save(vehicle);
        return convertToVehicleDTO(vehicle);
    }

    @Override
    @Transactional
    public void deleteVehicle(Long vehicleId) {
        if (!vehicleRepository.existsById(vehicleId)) {
            throw new RuntimeException("车辆不存在");
        }
        vehicleRepository.deleteById(vehicleId);
    }

    // 维修工单管理
    @Override
    public List<RepairOrderDTO> getAllRepairOrders() {
        return repairOrderRepository.findAll().stream()
                .map(this::convertToRepairOrderDTO)
                .collect(Collectors.toList());
    }

    @Override
    public RepairOrderDTO getRepairOrderById(Long orderId) {
        return repairOrderRepository.findById(orderId)
                .map(this::convertToRepairOrderDTO)
                .orElseThrow(() -> new RuntimeException("维修工单不存在"));
    }

    @Override
    @Transactional
    public RepairOrderDTO updateRepairOrder(Long orderId, RepairOrderDTO orderDTO) {
        RepairOrder order = repairOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("维修工单不存在"));
                
        order.setStatus(OrderStatus.valueOf(orderDTO.getStatus()));
        order.setDescription(orderDTO.getDescription());
        order.setRepairResult(orderDTO.getRepairResult());
        
        order = repairOrderRepository.save(order);
        return convertToRepairOrderDTO(order);
    }

    @Override
    @Transactional
    public void deleteRepairOrder(Long orderId) {
        if (!repairOrderRepository.existsById(orderId)) {
            throw new RuntimeException("维修工单不存在");
        }
        repairOrderRepository.deleteById(orderId);
    }

    // 数据统计
    @Override
    public Map<String, Object> getSystemStatistics() {
        Map<String, Object> stats = new HashMap<>();
        LocalDateTime now = LocalDateTime.now();
        
        // 基础统计
        stats.put("totalUsers", userRepository.count());
        stats.put("totalRepairmen", repairmanRepository.count());
        stats.put("totalVehicles", vehicleRepository.count());
        stats.put("totalOrders", repairOrderRepository.count());
        
        // 活跃订单统计
        stats.put("activeOrders", repairOrderRepository.countByStatus(OrderStatus.IN_PROGRESS));
        stats.put("completedOrders", repairOrderRepository.countByStatus(OrderStatus.COMPLETED));
        
        // 用户活跃度统计
        Map<String, Long> userActivity = new HashMap<>();
        userActivity.put("newUsersToday", userRepository.countByCreateTimeBetween(
            now.minusDays(1), now));
        userActivity.put("activeUsersLastWeek", userRepository.countByLastLoginTimeBetween(
            now.minusWeeks(1), now));
        stats.put("userActivity", userActivity);
        
        return stats;
    }

    @Override
    public Map<String, Object> getRepairStatistics() {
        Map<String, Object> stats = new HashMap<>();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime lastMonth = now.minusMonths(1);
        
        // 维修订单统计
        stats.put("ordersLastMonth", repairOrderRepository.countByCreateTimeBetween(lastMonth, now));
        stats.put("completedOrdersLastMonth", repairOrderRepository.countByStatusAndCompleteTimeBetween(
                OrderStatus.COMPLETED, lastMonth, now));
        
        // 维修效率统计
        stats.put("averageRepairTime", calculateAverageRepairTime());
        stats.put("repairmanWorkload", calculateRepairmanWorkload());
        
        // 维修类型分布
        Map<String, Long> repairTypeDistribution = new HashMap<>();
        repairOrderRepository.findAll().stream()
            .collect(Collectors.groupingBy(
                order -> order.getRepairman().getWorkType().name(),
                Collectors.counting()))
            .forEach(repairTypeDistribution::put);
        stats.put("repairTypeDistribution", repairTypeDistribution);
        
        // 维修质量统计
        Map<String, Object> qualityStats = new HashMap<>();
        qualityStats.put("averageCompletionTime", calculateAverageCompletionTime());
        qualityStats.put("reworkRate", calculateReworkRate());
        stats.put("qualityStatistics", qualityStats);
        
        return stats;
    }

    @Override
    public Map<String, Object> getFinancialStatistics() {
        Map<String, Object> stats = new HashMap<>();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime lastMonth = now.minusMonths(1);
        
        // 月度财务统计
        List<RepairOrder> ordersLastMonth = repairOrderRepository.findByCompleteTimeBetween(lastMonth, now);
        
        double totalIncome = ordersLastMonth.stream()
                .mapToDouble(order -> order.getLaborCost() + order.getMaterialCost())
                .sum();
                
        double totalLaborCost = ordersLastMonth.stream()
                .mapToDouble(RepairOrder::getLaborCost)
                .sum();
                
        double totalMaterialCost = ordersLastMonth.stream()
                .mapToDouble(RepairOrder::getMaterialCost)
                .sum();
        
        stats.put("totalIncome", totalIncome);
        stats.put("totalLaborCost", totalLaborCost);
        stats.put("totalMaterialCost", totalMaterialCost);
        stats.put("netIncome", totalIncome - totalLaborCost - totalMaterialCost);
        
        // 月度收入趋势
        Map<String, Double> monthlyIncome = new HashMap<>();
        for (int i = 0; i < 12; i++) {
            LocalDateTime monthStart = now.minusMonths(i).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
            LocalDateTime monthEnd = monthStart.plusMonths(1);
            double monthIncome = repairOrderRepository.findByCompleteTimeBetween(monthStart, monthEnd)
                .stream()
                .mapToDouble(order -> order.getLaborCost() + order.getMaterialCost())
                .sum();
            monthlyIncome.put(monthStart.getMonth().toString(), monthIncome);
        }
        stats.put("monthlyIncome", monthlyIncome);
        
        return stats;
    }

    @Override
    public Map<String, Object> checkDataConsistency() {
        Map<String, Object> results = new HashMap<>();
        List<String> inconsistencies = new ArrayList<>();
        List<String> warnings = new ArrayList<>();
        
        // 检查维修工单与维修人员的关联
        repairOrderRepository.findAll().forEach(order -> {
            if (order.getRepairman() != null && !repairmanRepository.existsById(order.getRepairman().getId())) {
                inconsistencies.add("工单 " + order.getId() + " 关联了不存在的维修人员");
            }
            if (order.getStatus() == OrderStatus.IN_PROGRESS && order.getAcceptTime() == null) {
                warnings.add("工单 " + order.getId() + " 状态为进行中但未记录接受时间");
            }
            if (order.getStatus() == OrderStatus.COMPLETED && order.getCompleteTime() == null) {
                warnings.add("工单 " + order.getId() + " 状态为已完成但未记录完成时间");
            }
        });
        
        // 检查维修工单与车辆的关联
        repairOrderRepository.findAll().forEach(order -> {
            if (order.getVehicle() != null && !vehicleRepository.existsById(order.getVehicle().getId())) {
                inconsistencies.add("工单 " + order.getId() + " 关联了不存在的车辆");
            }
        });
        
        // 检查车辆与车主的关联
        vehicleRepository.findAll().forEach(vehicle -> {
            if (vehicle.getOwner() != null && !userRepository.existsById(vehicle.getOwner().getId())) {
                inconsistencies.add("车辆 " + vehicle.getId() + " 关联了不存在的车主");
            }
            if (vehicle.getLastMaintenanceDate() != null && 
                vehicle.getLastMaintenanceDate().isAfter(LocalDateTime.now())) {
                warnings.add("车辆 " + vehicle.getId() + " 的最后维护时间在未来");
            }
        });
        
        // 检查维修人员状态
        repairmanRepository.findAll().forEach(staff -> {
            if (staff.getStatus() == StaffStatus.IDLE && 
                repairOrderRepository.existsByRepairmanIdAndStatus(staff.getId(), OrderStatus.IN_PROGRESS)) {
                warnings.add("维修人员 " + staff.getId() + " 状态为空闲但有关联的进行中工单");
            }
        });
        
        results.put("inconsistencies", inconsistencies);
        results.put("warnings", warnings);
        results.put("hasIssues", !inconsistencies.isEmpty() || !warnings.isEmpty());
        results.put("inconsistencyCount", inconsistencies.size());
        results.put("warningCount", warnings.size());
        
        return results;
    }

    @Override
    @Transactional
    public void repairDataInconsistency(String type, Long id) {
        switch (type) {
            case "REPAIR_ORDER":
                RepairOrder order = repairOrderRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("维修工单不存在"));
                if (order.getRepairman() != null && !repairmanRepository.existsById(order.getRepairman().getId())) {
                    order.setRepairman(null);
                    repairOrderRepository.save(order);
                }
                break;
            case "VEHICLE":
                Vehicle vehicle = vehicleRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("车辆不存在"));
                if (vehicle.getOwner() != null && !userRepository.existsById(vehicle.getOwner().getId())) {
                    vehicle.setOwner(null);
                    vehicleRepository.save(vehicle);
                }
                break;
            default:
                throw new RuntimeException("不支持的数据类型");
        }
    }

    @Override
    public List<RepairRequestDTO> getPendingRepairRequests() {
        return repairRequestRepository.findByStatus(UserRepairRequestStatus.UNREVIEWED)
                .stream()
                .map(request -> {
                    RepairRequestDTO dto = new RepairRequestDTO();
                    dto.setId(request.getId());
                    dto.setUserId(request.getUserId());
                    dto.setVehicleId(request.getVehicleId());
                    dto.setDescription(request.getDescription());
                    dto.setStatus(request.getStatus());
                    dto.setCreatedAt(request.getCreatedAt());

                    // 获取用户信息
                    User user = userRepository.findById(request.getUserId())
                            .orElse(null);
                    if (user != null) {
                        dto.setUsername(user.getUsername());
                    }

                    // 获取车辆信息
                    Vehicle vehicle = vehicleRepository.findById(request.getVehicleId())
                            .orElse(null);
                    if (vehicle != null) {
                        dto.setVehicleInfo(vehicle.getLicensePlate() + " - " + 
                                         vehicle.getBrand() + " " + vehicle.getModel());
                    }

                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RepairOrderDTO convertRequestToOrder(Long requestId, RepairRequestToOrderDTO request) {
        // 获取维修请求
        RepairRequest repairRequest = repairRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("维修请求不存在"));

        // 验证请求状态
        if (repairRequest.getStatus() != UserRepairRequestStatus.UNREVIEWED) {
            throw new IllegalArgumentException("该维修请求已被处理");
        }

        // 获取维修人员
        MaintenanceStaff repairman = repairmanRepository.findById(request.getRepairmanId())
                .orElseThrow(() -> new IllegalArgumentException("维修人员不存在"));

        // 获取车辆信息
        Vehicle vehicle = vehicleRepository.findById(repairRequest.getVehicleId())
                .orElseThrow(() -> new IllegalArgumentException("车辆不存在"));

        // 创建维修工单
        RepairOrder order = new RepairOrder();
        order.setRepairman(repairman);
        order.setVehicle(vehicle);
        order.setStatus(OrderStatus.PENDING);
        order.setDescription(request.getDescription() != null ? 
                request.getDescription() : repairRequest.getDescription());
        order.setCreateTime(LocalDateTime.now());

        // 保存工单
        order = repairOrderRepository.save(order);

        // 更新维修请求状态
        repairRequest.setStatus(UserRepairRequestStatus.APPROVED);
        repairRequestRepository.save(repairRequest);

        // 转换为DTO返回
        return convertToRepairOrderDTO(order);
    }

    // 辅助方法
    private AdminDTO convertToAdminDTO(Admin admin) {
        AdminDTO dto = new AdminDTO();
        dto.setId(admin.getId());
        dto.setUsername(admin.getUsername());
        return dto;
    }

    private UserDTO convertToUserDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        
        // 根据实体类型设置用户类型
        if (user instanceof MaintenanceStaff) {
            dto.setUserType("MAINTENANCE");
        } else {
            dto.setUserType("USER");
        }
        
        // 获取用户档案信息
        userProfileRepository.findByUserId(user.getId())
            .ifPresent(profile -> {
                UserDTO.UserProfileDTO profileDTO = new UserDTO.UserProfileDTO();
                profileDTO.setId(profile.getId());
                profileDTO.setPhone(profile.getPhone());
                profileDTO.setName(profile.getName());
                profileDTO.setEmail(profile.getEmail());
                profileDTO.setAddress(profile.getAddress());
                dto.setProfile(profileDTO);
            });
            
        return dto;
    }

    private RepairmanDTO convertToRepairmanDTO(MaintenanceStaff staff) {
        RepairmanDTO dto = new RepairmanDTO();
        dto.setId(staff.getId());
        dto.setUsername(staff.getUsername());
        dto.setWorkType(staff.getWorkType());
        dto.setHourlyRate(staff.getHourlyRate().doubleValue());
        dto.setStatus(staff.getStatus());
        return dto;
    }

    private VehicleDTO convertToVehicleDTO(Vehicle vehicle) {
        VehicleDTO dto = new VehicleDTO();
        dto.setId(vehicle.getId());
        dto.setLicensePlate(vehicle.getLicensePlate());
        dto.setBrand(vehicle.getBrand());
        dto.setModel(vehicle.getModel());
        dto.setYear(vehicle.getYear());
        dto.setColor(vehicle.getColor());
        dto.setVin(vehicle.getVin());
        dto.setLastMaintenanceDate(vehicle.getLastMaintenanceDate());
        if (vehicle.getOwner() != null) {
            dto.setOwnerId(vehicle.getOwner().getId());
        }
        return dto;
    }

    private RepairOrderDTO convertToRepairOrderDTO(RepairOrder order) {
        RepairOrderDTO dto = new RepairOrderDTO();
        dto.setId(order.getId());
        dto.setStatus(order.getStatus().name());
        dto.setDescription(order.getDescription());
        dto.setCreateTime(order.getCreateTime());
        dto.setAcceptTime(order.getAcceptTime());
        dto.setCompleteTime(order.getCompleteTime());
        dto.setTotalHours(order.getTotalHours());
        dto.setLaborCost(order.getLaborCost());
        dto.setMaterialCost(order.getMaterialCost());
        dto.setRepairResult(order.getRepairResult());
        if (order.getRepairman() != null) {
            dto.setRepairmanId(order.getRepairman().getId());
        }
        if (order.getVehicle() != null) {
            dto.setId(order.getVehicle().getId());
        }
        return dto;
    }

    private double calculateAverageRepairTime() {
        List<RepairOrder> completedOrders = repairOrderRepository.findByStatus(OrderStatus.COMPLETED);
        if (completedOrders.isEmpty()) {
            return 0.0;
        }
        
        return completedOrders.stream()
                .mapToDouble(order -> order.getTotalHours())
                .average()
                .orElse(0.0);
    }

    private Map<Long, Integer> calculateRepairmanWorkload() {
        Map<Long, Integer> workload = new HashMap<>();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime lastMonth = now.minusMonths(1);
        
        repairmanRepository.findAll().forEach(staff -> {
            Long count = repairOrderRepository.countByRepairmanIdAndCreateTimeBetween(
                    staff.getId(), lastMonth, now);
            workload.put(staff.getId(), Math.toIntExact(count));
        });
        
        return workload;
    }

    private double calculateAverageCompletionTime() {
        List<RepairOrder> completedOrders = repairOrderRepository.findByStatus(OrderStatus.COMPLETED);
        if (completedOrders.isEmpty()) {
            return 0.0;
        }
        
        return completedOrders.stream()
                .filter(order -> order.getAcceptTime() != null && order.getCompleteTime() != null)
                .mapToDouble(order -> 
                    java.time.Duration.between(order.getAcceptTime(), order.getCompleteTime()).toHours())
                .average()
                .orElse(0.0);
    }

    private double calculateReworkRate() {
        List<RepairOrder> allOrders = repairOrderRepository.findAll();
        if (allOrders.isEmpty()) {
            return 0.0;
        }
        
        long reworkCount = allOrders.stream()
                .filter(order -> order.getRepairResult() != null && 
                        order.getRepairResult().toLowerCase().contains("返工"))
                .count();
                
        return (double) reworkCount / allOrders.size();
    }
} 