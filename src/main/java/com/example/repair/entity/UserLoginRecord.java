package com.example.repair.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name = "user_login_records")
public class UserLoginRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private Date loginTime;


    private Date logoutTime;

    public UserLoginRecord() {
    }

    public UserLoginRecord(String username, String token, Date loginTime) {
        this.username = username;
        this.token = token;
        this.loginTime = loginTime;
    }
}
