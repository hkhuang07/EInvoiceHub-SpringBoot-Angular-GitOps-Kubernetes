package com.einvoicehub.config;

import com.einvoicehub.security.ApiKeyFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security Configuration cho EInvoiceHub
 * 
 * Cấu hình bảo mật sử dụng API Key authentication
 * Không sử dụng Session vì đây là stateless REST API
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final ApiKeyFilter apiKeyFilter;

    public SecurityConfig(ApiKeyFilter apiKeyFilter) {
        this.apiKeyFilter = apiKeyFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Disable CSRF vì sử dụng API Key authentication
            .csrf(AbstractHttpConfigurer::disable)
            
            // Stateless session management
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            // Authorization rules
            .authorizeHttpRequests(auth -> auth
                // Cho phép truy cập các endpoints công khai
                .requestMatchers("/actuator/**").permitAll()
                .requestMatchers("/v3/api-docs/**").permitAll()
                .requestMatchers("/swagger-ui/**").permitAll()
                .requestMatchers("/swagger-ui.html").permitAll()
                .requestMatchers("/health/**").permitAll()
                
                // Tất cả các request khác cần xác thực API Key
                .anyRequest().authenticated())
            
            // Thêm API Key filter trước UsernamePasswordAuthenticationFilter
            .addFilterBefore(apiKeyFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }

    /**
     * Đăng ký ApiKeyFilter như một Servlet Filter
     */
    @Bean
    public FilterRegistrationBean<ApiKeyFilter> apiKeyFilterRegistration() {
        FilterRegistrationBean<ApiKeyFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(apiKeyFilter);
        registration.addUrlPatterns("/*");
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE + 1);
        registration.setName("apiKeyFilter");
        return registration;
    }
}