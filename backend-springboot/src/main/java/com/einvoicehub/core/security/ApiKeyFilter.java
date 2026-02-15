package com.einvoicehub.core.security;

import com.einvoicehub.core.common.exception.ErrorCode;
import com.einvoicehub.core.domain.entity.MerchantEntity;
import com.einvoicehub.core.domain.enums.EntityStatus;
import com.einvoicehub.core.domain.entity.ApiCredential;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApiKeyFilter extends OncePerRequestFilter {

    private static final String API_KEY_HEADER = "X-API-KEY";
    private static final String CLIENT_ID_HEADER = "X-CLIENT-ID";

    private final ApiCredentialRepository apiCredentialRepository;
    private final ObjectMapper objectMapper;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    private static final Set<String> PUBLIC_PATHS = Set.of(
            "/api/v1/auth/**",
            "/api/v1/health/**",
            "/api/v1/public/**",
            "/actuator/**"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        if (isPublicPath(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        String clientId = request.getHeader(CLIENT_ID_HEADER);
        String apiKey = request.getHeader(API_KEY_HEADER);

        if (clientId == null || apiKey == null) {
            writeErrorResponse(response, ErrorCode.UNAUTHENTICATED, "Missing security headers");
            return;
        }

        ApiCredential credential = apiCredentialRepository
                .findByClientIdAndIsActiveTrue(clientId)
                .orElse(null);

        if (credential == null || !hashApiKey(apiKey).equals(credential.getApiKeyHash())) {
            writeErrorResponse(response, ErrorCode.API_KEY_INVALID, "Invalid credentials");
            return;
        }

        if (!credential.isValid()) {
            writeErrorResponse(response, ErrorCode.API_KEY_INVALID, "Credential expired or inactive");
            return;
        }

        /* IP Whitelist validation */
        if (credential.getIpWhitelist() != null && !credential.getIpWhitelist().isBlank()) {
            String clientIp = getClientIp(request);
            if (!isIpAllowed(clientIp, credential.getIpWhitelist())) {
                writeErrorResponse(response, ErrorCode.UNAUTHORIZED, "IP address not whitelisted");
                return;
            }
        }

        /* Rate limit validation */
        if (!credential.canMakeRequest()) {
            writeErrorResponse(response, ErrorCode.VALIDATION_ERROR, "Rate limit exceeded");
            return;
        }

        MerchantEntity merchant = credential.getMerchant();
        if (merchant == null || merchant.getIsDeleted() || merchant.getStatus() != EntityStatus.ACTIVE) {
            writeErrorResponse(response, ErrorCode.UNAUTHORIZED, "Merchant account is inactive");
            return;
        }

        List<SimpleGrantedAuthority> authorities = buildAuthorities(credential.getScopes());
        ApiKeyAuthenticationToken auth = new ApiKeyAuthenticationToken(clientId, apiKey, credential, merchant, authorities);

        credential.incrementRequestCount();
        apiCredentialRepository.save(credential);

        SecurityContextHolder.getContext().setAuthentication(auth);
        filterChain.doFilter(request, response);
    }

    private boolean isPublicPath(String path) {
        return PUBLIC_PATHS.stream().anyMatch(pattern -> pathMatcher.match(pattern, path));
    }

    private String hashApiKey(String apiKey) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(apiKey.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not available", e);
        }
    }

    private String getClientIp(HttpServletRequest request) {
        String xf = request.getHeader("X-Forwarded-For");
        return (xf != null) ? xf.split(",")[0].trim() : request.getRemoteAddr();
    }

    private boolean isIpAllowed(String clientIp, String whitelist) {
        return Arrays.stream(whitelist.split(","))
                .map(String::trim)
                .anyMatch(allowed -> allowed.equals(clientIp));
    }

    private List<SimpleGrantedAuthority> buildAuthorities(String scopes) {
        if (scopes == null || scopes.isBlank()) return List.of(new SimpleGrantedAuthority("ROLE_API"));
        return Arrays.stream(scopes.split(","))
                .map(s -> new SimpleGrantedAuthority("SCOPE_" + s.trim().toUpperCase().replace(".", "_")))
                .toList();
    }

    private void writeErrorResponse(HttpServletResponse response, ErrorCode errorCode, String message) throws IOException {
        response.setStatus(errorCode.getStatusCode().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ApiResponse<?> apiResponse = ApiResponse.error(errorCode, message);
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !request.getRequestURI().startsWith("/api/");
    }
}