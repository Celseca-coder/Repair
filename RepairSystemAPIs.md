# 数据库管理员API

## 基础信息
- 基础URL: `/api/admin`
- 认证方式: JWT Token
- 权限要求: 需要 `ROLE_ADMIN` 角色

## 认证接口

### 管理员登录
- **URL**: `/login`
- **方法**: POST
- **参数**:
  - `username`: 用户名
  - `password`: 密码
- **响应**:
  ```json
  {
    "admin": {
      "id": "管理员ID",
      "username": "用户名",
      "name": "姓名",
      "email": "邮箱",
      "lastLoginTime": "最后登录时间",
      "role": "ROLE_ADMIN"
    },
    "token": "JWT令牌"
  }
  ```

## 用户管理接口

### 获取所有用户
- **URL**: `/users`
- **方法**: GET
- **响应**: 用户列表

### 获取单个用户
- **URL**: `/users/{userId}`
- **方法**: GET
- **响应**: 用户详细信息

### 更新用户信息
- **URL**: `/users/{userId}`
- **方法**: PUT
- **请求体**: UserDTO对象
- **响应**: 更新后的用户信息

### 删除用户
- **URL**: `/users/{userId}`
- **方法**: DELETE
- **响应**: 204 No Content

## 维修人员管理接口

### 获取所有维修人员
- **URL**: `/repairmen`
- **方法**: GET
- **响应**: 维修人员列表

### 获取单个维修人员
- **URL**: `/repairmen/{repairmanId}`
- **方法**: GET
- **响应**: 维修人员详细信息

### 更新维修人员信息
- **URL**: `/repairmen/{repairmanId}`
- **方法**: PUT
- **请求体**: RepairmanDTO对象
- **响应**: 更新后的维修人员信息

### 删除维修人员
- **URL**: `/repairmen/{repairmanId}`
- **方法**: DELETE
- **响应**: 204 No Content

## 车辆管理接口

### 获取所有车辆
- **URL**: `/vehicles`
- **方法**: GET
- **响应**: 车辆列表

### 获取单个车辆
- **URL**: `/vehicles/{vehicleId}`
- **方法**: GET
- **响应**: 车辆详细信息

### 更新车辆信息
- **URL**: `/vehicles/{vehicleId}`
- **方法**: PUT
- **请求体**: VehicleDTO对象
- **响应**: 更新后的车辆信息

### 删除车辆
- **URL**: `/vehicles/{vehicleId}`
- **方法**: DELETE
- **响应**: 204 No Content

## 维修工单管理接口

### 获取所有维修工单
- **URL**: `/orders`
- **方法**: GET
- **响应**: 维修工单列表

### 获取单个维修工单
- **URL**: `/orders/{orderId}`
- **方法**: GET
- **响应**: 维修工单详细信息

### 更新维修工单
- **URL**: `/orders/{orderId}`
- **方法**: PUT
- **请求体**: RepairOrderDTO对象
- **响应**: 更新后的维修工单信息

### 删除维修工单
- **URL**: `/orders/{orderId}`
- **方法**: DELETE
- **响应**: 204 No Content

## 数据统计接口

### 系统统计
- **URL**: `/statistics/system`
- **方法**: GET
- **响应**:
  ```json
  {
    "totalUsers": "总用户数",
    "totalRepairmen": "总维修人员数",
    "totalVehicles": "总车辆数",
    "totalOrders": "总工单数",
    "activeOrders": "进行中工单数",
    "completedOrders": "已完成工单数",
    "userActivity": {
      "newUsersToday": "今日新增用户",
      "activeUsersLastWeek": "上周活跃用户"
    }
  }
  ```

### 维修统计
- **URL**: `/statistics/repair`
- **方法**: GET
- **响应**:
  ```json
  {
    "ordersLastMonth": "上月工单数",
    "completedOrdersLastMonth": "上月完成工单数",
    "averageRepairTime": "平均维修时间",
    "repairmanWorkload": "维修人员工作量",
    "repairTypeDistribution": "维修类型分布",
    "qualityStatistics": {
      "averageCompletionTime": "平均完成时间",
      "reworkRate": "返工率"
    }
  }
  ```

### 财务统计
- **URL**: `/statistics/financial`
- **方法**: GET
- **响应**:
  ```json
  {
    "totalIncome": "总收入",
    "totalLaborCost": "总人工成本",
    "totalMaterialCost": "总材料成本",
    "netIncome": "净收入",
    "monthlyIncome": "月度收入趋势"
  }
  ```

## 数据一致性接口

### 检查数据一致性
- **URL**: `/consistency/check`
- **方法**: GET
- **响应**:
  ```json
  {
    "inconsistencies": ["不一致项列表"],
    "warnings": ["警告项列表"],
    "hasIssues": "是否存在问题",
    "inconsistencyCount": "不一致项数量",
    "warningCount": "警告项数量"
  }
  ```

### 修复数据不一致
- **URL**: `/consistency/repair`
- **方法**: POST
- **参数**:
  - `type`: 数据类型（REPAIR_ORDER/VEHICLE）
  - `id`: 数据ID
- **响应**: 204 No Content

## 数据导出接口

### 导出用户数据
- **URL**: `/export/users`
- **方法**: GET
- **响应**: Excel文件（users.xlsx）
- **内容**: ID、用户名、姓名、电话、邮箱、地址

### 导出维修人员数据
- **URL**: `/export/repairmen`
- **方法**: GET
- **响应**: Excel文件（repairmen.xlsx）
- **内容**: ID、用户名、工种、时薪、状态

### 导出车辆数据
- **URL**: `/export/vehicles`
- **方法**: GET
- **响应**: Excel文件（vehicles.xlsx）
- **内容**: ID、车牌号、品牌、型号、年份、颜色、VIN、最后维护时间、车主ID、车主姓名

### 导出维修工单数据
- **URL**: `/export/orders`
- **方法**: GET
- **响应**: Excel文件（repair_orders.xlsx）
- **内容**: ID、维修人员ID、状态、描述、创建时间、接受时间、完成时间、总工时、人工费用、材料费用、维修结果

## 系统监控接口

### 获取系统性能
- **URL**: `/monitor/performance`
- **方法**: GET
- **响应**:
  ```json
  {
    "systemInfo": {
      "osName": "操作系统名称",
      "osVersion": "操作系统版本",
      "osArch": "系统架构",
      "processorCount": "处理器数量",
      "systemLoadAverage": "系统负载",
      "heapMemoryUsage": "堆内存使用情况",
      "nonHeapMemoryUsage": "非堆内存使用情况",
      "threadCount": "线程数",
      "peakThreadCount": "峰值线程数",
      "totalStartedThreadCount": "总启动线程数",
      "daemonThreadCount": "守护线程数"
    },
    "jvmInfo": {
      "jvmVersion": "JVM版本",
      "jvmVendor": "JVM供应商",
      "jvmName": "JVM名称",
      "jvmMaxMemory": "最大内存",
      "jvmTotalMemory": "总内存",
      "jvmFreeMemory": "空闲内存",
      "jvmUsedMemory": "已用内存"
    }
  }
  ```

### 获取系统日志
- **URL**: `/monitor/logs`
- **方法**: GET
- **参数**:
  - `level`: 日志级别（可选）
  - `startTime`: 开始时间（可选）
  - `endTime`: 结束时间（可选）
- **响应**: 日志列表（待实现）

## 注意事项
1. 所有接口都需要在请求头中携带有效的JWT令牌
2. 所有接口都需要管理员权限
3. 文件导出接口返回的是二进制数据流，需要正确处理响应类型
4. 统计接口的数据都是实时计算的，可能需要一定响应时间
5. 系统监控接口的数据反映了当前系统的运行状态

## 错误响应
所有接口在发生错误时都会返回适当的HTTP状态码和错误信息：
- 400 Bad Request: 请求参数错误
- 401 Unauthorized: 未认证或认证失败
- 403 Forbidden: 权限不足
- 404 Not Found: 资源不存在
- 500 Internal Server Error: 服务器内部错误

错误响应格式：
```json
{
  "error": "错误信息",
  "timestamp": "错误发生时间",
  "path": "请求路径"
}
```

# 维修人员API

## 基础信息
- 基础URL: `/api/repairman`
- 认证方式: JWT Token
- 权限要求: 需要维修人员角色

## 认证接口

### 维修人员注册
- **URL**: `/register`
- **方法**: POST
- **请求体**: RepairmanDTO对象
  ```json
  {
    "username": "用户名",
    "password": "密码",
    "workType": "工种类型",
    "hourlyRate": "时薪"
  }
  ```
- **响应**: 注册成功的维修人员信息

### 维修人员登录
- **URL**: `/login`
- **方法**: POST
- **参数**:
  - `username`: 用户名
  - `password`: 密码
- **响应**:
  ```json
  {
    "repairman": {
      "id": "维修人员ID",
      "username": "用户名",
      "workType": "工种类型",
      "hourlyRate": "时薪",
      "status": "当前状态"
    },
    "token": "JWT令牌"
  }
  ```

## 个人信息接口

### 获取当前用户信息
- **URL**: `/current`
- **方法**: GET
- **响应**: 当前登录维修人员的详细信息

### 获取指定维修人员信息
- **URL**: `/{repairmanId}`
- **方法**: GET
- **响应**: 指定维修人员的详细信息

## 工单管理接口

### 接受维修工单
- **URL**: `/{repairmanId}/orders/{orderId}/accept`
- **方法**: POST
- **响应**: 更新后的工单信息

### 拒绝维修工单
- **URL**: `/{repairmanId}/orders/{orderId}/reject`
- **方法**: POST
- **响应**: 更新后的工单信息

### 记录材料使用
- **URL**: `/orders/{orderId}/materials`
- **方法**: POST
- **请求体**: MaterialUsageDTO对象
  ```json
  {
    "materialName": "材料名称",
    "quantity": "使用数量",
    "unitPrice": "单价",
    "usageDescription": "使用说明"
  }
  ```
- **响应**: 记录的材料使用信息

### 更新维修进度
- **URL**: `/orders/{orderId}/progress`
- **方法**: POST
- **请求体**: RepairProgressDTO对象
  ```json
  {
    "progress": "进度描述",
    "status": "当前状态",
    "remark": "备注"
  }
  ```
- **响应**: 更新后的进度信息

### 更新维修结果
- **URL**: `/{repairmanId}/orders/{orderId}/result`
- **方法**: PUT
- **参数**:
  - `result`: 维修结果描述
- **响应**: 更新后的工单信息

## 历史记录接口

### 获取维修历史
- **URL**: `/{repairmanId}/history`
- **方法**: GET
- **响应**: 维修历史工单列表
  ```json
  [
    {
      "id": "工单ID",
      "status": "工单状态",
      "description": "维修描述",
      "createTime": "创建时间",
      "acceptTime": "接受时间",
      "completeTime": "完成时间",
      "totalHours": "总工时",
      "laborCost": "人工费用",
      "materialCost": "材料费用",
      "repairResult": "维修结果"
    }
  ]
  ```

### 计算工时收入
- **URL**: `/{repairmanId}/income`
- **方法**: GET
- **响应**: 工时收入金额

## 数据模型

### RepairmanDTO
```json
{
  "id": "维修人员ID",
  "username": "用户名",
  "workType": "工种类型",
  "hourlyRate": "时薪",
  "status": "当前状态"
}
```

### MaterialUsageDTO
```json
{
  "id": "记录ID",
  "repairOrderId": "工单ID",
  "materialName": "材料名称",
  "quantity": "使用数量",
  "unitPrice": "单价",
  "totalPrice": "总价",
  "usageDescription": "使用说明"
}
```

### RepairProgressDTO
```json
{
  "id": "进度ID",
  "repairOrderId": "工单ID",
  "progress": "进度描述",
  "status": "当前状态",
  "updateTime": "更新时间",
  "remark": "备注"
}
```

### RepairOrderDTO
```json
{
  "id": "工单ID",
  "repairmanId": "维修人员ID",
  "status": "工单状态",
  "description": "维修描述",
  "createTime": "创建时间",
  "acceptTime": "接受时间",
  "completeTime": "完成时间",
  "totalHours": "总工时",
  "laborCost": "人工费用",
  "materialCost": "材料费用",
  "repairResult": "维修结果",
  "materialUsages": "使用的材料列表"
}
```

## 状态说明

### 工单状态
- `PENDING`: 待接收
- `ACCEPTED`: 已接收
- `REJECTED`: 已拒绝
- `IN_PROGRESS`: 维修中
- `COMPLETED`: 已完成

### 维修人员状态
- `IDLE`: 空闲
- `BUSY`: 工作中
- `ON_LEAVE`: 休假
- `OFF_DUTY`: 下班

## 注意事项
1. 所有接口都需要在请求头中携带有效的JWT令牌
2. 维修人员只能操作分配给自己的工单
3. 工单状态变更需要按照正确的流程进行
4. 材料使用记录需要准确填写数量和单价
5. 维修进度更新需要包含详细的进度描述

## 错误响应
所有接口在发生错误时都会返回适当的HTTP状态码和错误信息：
- 400 Bad Request: 请求参数错误
- 401 Unauthorized: 未认证或认证失败
- 403 Forbidden: 权限不足
- 404 Not Found: 资源不存在
- 500 Internal Server Error: 服务器内部错误

错误响应格式：
```json
{
  "error": "错误信息",
  "timestamp": "错误发生时间",
  "path": "请求路径"
}
```

