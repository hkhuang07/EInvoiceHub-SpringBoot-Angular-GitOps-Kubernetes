package com.einvoicehub.core.security;

import com.einvoicehub.core.entity.mysql.MerchantUser;
import com.einvoicehub.core.repository.mysql.MerchantUserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * Filter xác thực JWT cho các request từ UI (Admin Portal)
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter {
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEADER_PREFIX = "Bearer";

    private final JwtTokenProvider jwtTokenPRovider;
    private final MerchantUserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletReponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt
        }
    }

}
