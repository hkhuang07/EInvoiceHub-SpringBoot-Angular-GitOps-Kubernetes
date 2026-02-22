package com.einvoicehub.core.security.jwt;

import com.einvoicehub.core.context.TenantContext;
import com.einvoicehub.core.security.SecurityUser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            jwt = authHeader.substring(7);
            username = jwtService.extractUsername(jwt);

            //Nếu có username và chưa được xác thực trong SecurityContext
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                SecurityUser userDetails = (SecurityUser) this.userDetailsService.loadUserByUsername(username);

                if (jwtService.isTokenValid(jwt, userDetails)) {
                    //Thiết lập TenantContext cho luồng xử lý hiện tại
                    //Trích xuất MerchantID từ Claim của Token thay vì query DB để tối ưu
                    Long merchantId = jwtService.extractMerchantId(jwt);
                    TenantContext.setMerchantId(merchantId);
                    log.debug("[Security] Request from User: {} - MerchantID: {}", username, merchantId);

                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    //Đưa thông tin xác thực vào context của Spring Security
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            log.error("[Security] JWT Authentication failed: {}", e.getMessage());
            // Không set context, request sẽ bị FilterChain chặn ở bước sau
        } finally {
            try {
                filterChain.doFilter(request, response);
            } finally {
                //Giải phóng ThreadLocal sau khi kết thúc request để tránh rò rỉ dữ liệu
                TenantContext.clear();
            }
        }
    }
}