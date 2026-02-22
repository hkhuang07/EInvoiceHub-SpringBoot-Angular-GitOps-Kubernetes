package com.einvoicehub.core.security.api;

import com.einvoicehub.core.context.TenantContext;
import com.einvoicehub.core.domain.entity.EinvApiCredentialsEntity;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApiKeyAuthenticationFilter extends OncePerRequestFilter {

    private final ApiKeyAuthenticationService apiKeyService;
    private static final String HEADER_CLIENT_ID = "X-Client-ID";
    private static final String HEADER_API_KEY = "X-API-Key";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String clientId = request.getHeader(HEADER_CLIENT_ID);
        String apiKey = request.getHeader(HEADER_API_KEY);

        //Chỉ xử lý nếu có đủ cả 2 header xác thực API
        if (clientId == null || apiKey == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            EinvApiCredentialsEntity credentials = apiKeyService.validateAndUsageUpdate(clientId, apiKey);

            //Lồng MerchantID vào TenantContext
            TenantContext.setMerchantId(credentials.getMerchant().getId());
            log.info("[API-Auth] Authenticated Client: {} for Merchant: {}",
                    clientId, credentials.getMerchant().getTaxCode());

            // 4. Trong SecurityContext  Gán Role là API_USER
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    clientId,
                    null,
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_API_USER"))
            );
            SecurityContextHolder.getContext().setAuthentication(auth);

        } catch (Exception e) {
            log.error("[API-Auth] Authentication failed: {}", e.getMessage());
            handleException(response, e);
            return;
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            // 5. Luôn giải phóng ThreadLocal (Requirement 4)
            TenantContext.clear();
        }
    }

    private void handleException(HttpServletResponse response, Exception e) throws IOException {
        int status = HttpServletResponse.SC_UNAUTHORIZED;
        if (e.getMessage().contains("429")) {
            status = 429;
        }
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String json = String.format("{\"error\": \"%s\", \"message\": \"%s\"}", "Security Error", e.getMessage());
        response.getWriter().write(json);
    }
}