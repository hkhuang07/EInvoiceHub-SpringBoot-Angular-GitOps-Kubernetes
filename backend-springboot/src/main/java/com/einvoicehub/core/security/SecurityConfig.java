package com.einvoicehub.core.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Cấu hình Security cho ứng dụng
 * Hỗ trợ cả API Key authentication (cho API) và JWT authentication (cho UI)
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final ApiKeyFilter apiKeyFilter;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * Security Filter Chain cho API requests (sử dụng API Key)
     */
    @Bean
    @Order(1)
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/v1/invoices/**", "/api/v1/merchants/**", "/api/v1/configs/**")
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(apiKeyFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Security Filter Chain cho Admin/UI requests (sử dụng JWT)
     */
    @Bean
    @Order(2)
    public SecurityFilterChain adminSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/v1/admin/**", "/api/v1/portal/**")
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/auth/login").permitAll()
                        .requestMatchers("/api/v1/admin/users/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/admin/**").hasAnyRole("ADMIN", "MANAGER")
                        .anyRequest().authenticated())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Security Filter Chain cho các request còn lại (Actuator, health check)
     */
    @Bean
    @Order(3)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/actuator/**").permitAll()
                        .requestMatchers("/api/v1/public/**").permitAll()
                        .anyRequest().denyAll());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}