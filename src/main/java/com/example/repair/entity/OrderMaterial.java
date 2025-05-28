package com.example.repair.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
public class OrderMaterial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private MaintenanceOrder order;

    @ManyToOne
    private Material material;

    private BigDecimal quantity;
    private BigDecimal actualPrice; // 可能不同于材料当前价格
}
