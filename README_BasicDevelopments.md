# 一些用户开发的基础与类
## 类
1. User
```java
package com.example.repair.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "user_type")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;

    // getters/setters
}
```
延展示例如下：
```java
@Entity
@Getter
@Setter
@DiscriminatorValue("MAINTENANCE")
public class MaintenanceStaff extends User {
    @Enumerated(EnumType.STRING)
    private WorkType workType; // 工种

    private BigDecimal hourlyRate; // 时薪

    @Enumerated(EnumType.STRING)
    private StaffStatus status = StaffStatus.IDLE; // 状态，默认为空闲
}
```
## 接口
### 员工部分
1. 登录
```java
@PostMapping("/login")
public ResponseEntity<RepairmanDTO> login(@RequestParam String username, @RequestParam String password) {
    RepairmanDTO repairman = repairmanService.login(username, password);
    return ResponseEntity.ok(repairman);
}
```
用法示例
```bash
POST http://localhost:8080/api/repairman/login?username=Repairman1&password=Repairman111
```
返回示例
```json
{
    "id": 1,
    "username": "Repairman1",
    "password": null,
    "workType": "PAINTER",
    "hourlyRate": 100.0,
    "status": "IDLE"
}
```

2. 注册
```java
@PostMapping("/login")
public ResponseEntity<RepairmanDTO> login(@RequestParam String username, @RequestParam String password) {
    RepairmanDTO repairman = repairmanService.login(username, password);
    return ResponseEntity.ok(repairman);
}
```
用法示例
```json
POST http://localhost:8080/api/repairman/register
{
  "username": "Repairman2",
  "password": "Repairman222",
  "workType": "PAINTER",
  "hourlyRate": 100,
  "status": "IDLE"
}
```
返回示例
```json
{
    "id": 2,
    "username": "Repairman2",
    "password": null,
    "workType": "PAINTER",
    "hourlyRate": 100.0,
    "status": "IDLE"
}
```