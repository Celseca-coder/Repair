package com.example.repair.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.http.HttpMethod;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    
    @Autowired
    private UserDetailsService userDetailsService;
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())  // 禁用CSRF保护
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))  // 使用无状态会话
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.POST, "/api/repairman/login").permitAll()  // 允许POST登录
                .requestMatchers(HttpMethod.POST, "/api/repairman/register").permitAll()  // 允许POST注册
                .requestMatchers(HttpMethod.POST, "/users/register").permitAll()  // 允许用户注册
                .requestMatchers(HttpMethod.POST, "/users/login").permitAll()  // 允许用户登录
                .requestMatchers(HttpMethod.POST, "/users/logout").permitAll()
                .requestMatchers(HttpMethod.POST, "/users/editUser").permitAll()
                .requestMatchers(HttpMethod.POST, "/vehicles/**").authenticated()  // 允许已认证用户访问车辆相关端点
                .requestMatchers(HttpMethod.GET, "/vehicles/**").authenticated()  // 允许已认证用户访问车辆相关端点
                .anyRequest().authenticated()  // 其他请求需要认证
            )
            .authenticationProvider(authenticationProvider())  // 添加认证提供者
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);  // 添加JWT过滤器
        
        return http.build();
    }
} 