package com.example.repair.entity;

import com.example.repair.enums.StaffStatus;
import com.example.repair.enums.WorkType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

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
