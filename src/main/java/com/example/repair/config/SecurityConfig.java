package com.example.repair.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod; // 引入 HttpMethod
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
import org.springframework.web.cors.CorsConfiguration; // 引入 CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource; // 引入 CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource; // 引入 UrlBasedCorsConfigurationSource

import java.util.Arrays; // 引入 Arrays
import static org.springframework.security.config.Customizer.withDefaults; // 引入 withDefaults

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
                .cors(withDefaults()) // **<--- 添加这一行来启用通过 CorsConfigurationSource bean 配置的 CORS**
                .csrf(csrf -> csrf.disable())  // 禁用CSRF保护
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))  // 使用无状态会话
                .authorizeHttpRequests(auth -> auth
                        // 允许 OPTIONS 预检请求 (对于所有路径，或者更具体的路径)
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/**").permitAll()
                        // 公开访问的端点
                        .requestMatchers(HttpMethod.POST, "/api/repairman/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/repairman/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/repairman/{repairmanId}/orders/{orderId}/accept").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/repairman/{repairmanId}/orders/{orderId}/reject").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/repairman/{repairmanId}/orders/{orderId}/result").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/repairman/orders/{orderId}/materials").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/repairman/orders/{orderId}/progress").permitAll()

                        .requestMatchers(HttpMethod.POST, "/users/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/users/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/admin/login").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/api/admin/orders/").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/admin/repairmen/").permitAll()
                        // 其他一些您希望允许匿名访问的 POST 请求 (请仔细评估这些是否真的需要对未认证用户开放)
                        .requestMatchers(HttpMethod.POST, "/users/logout").permitAll() // logout 通常也应该在认证后，但有时设计为清除前端状态
                        .requestMatchers(HttpMethod.POST, "/users/editUser").authenticated() // 编辑用户信息通常需要认证
                        .requestMatchers(HttpMethod.POST, "/vehicles/addVehicles").authenticated() // 添加车辆通常需要认证
                        .requestMatchers(HttpMethod.POST, "/vehicles/getVehicles").authenticated() // 获取车辆信息通常需要认证 (或部分公开)
                        .requestMatchers(HttpMethod.POST, "/vehicles/editVehicles").authenticated() // 编辑车辆通常需要认证
                        .requestMatchers(HttpMethod.POST, "/vehicles/deleteVehicles").authenticated() // 删除车辆通常需要认证
                        .requestMatchers(HttpMethod.POST, "/repairs/addRequest").authenticated() // 添加维修请求通常需要认证
                        .requestMatchers(HttpMethod.POST, "/repairs/getRequest").authenticated() // 获取维修请求通常需要认证 (或部分公开)
                        .requestMatchers(HttpMethod.POST, "/repairs/getOrders").authenticated() // 获取订单通常需要认证 (或部分公开)
                        .requestMatchers(HttpMethod.POST, "/reviews/addReviews").authenticated() // 添加评论通常需要认证
                        .requestMatchers(HttpMethod.POST, "/api/repairman/").authenticated()
                        // 对于 /vehicles/** 的 POST 和 GET 请求，要求认证 (这与上面的permitAll规则有重叠和潜在冲突，需要整合)
                        // .requestMatchers(HttpMethod.POST, "/vehicles/**").authenticated() // 这条规则被上面的具体路径覆盖了
                        // .requestMatchers(HttpMethod.GET, "/vehicles/**").authenticated()

                        .anyRequest().authenticated()  // 其他所有未明确指定的请求都需要认证
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // 定义 CorsConfigurationSource Bean 以供 Spring Security 使用
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173")); // 明确指定允许的前端源
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*")); // 允许所有请求头
        configuration.setAllowCredentials(true); // 允许发送凭据 (如 cookies)
        configuration.setMaxAge(3600L); // 预检请求的缓存时间
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // 将此配置应用于所有路径
        return source;
    }
}