package com.einvoicehub.core.security;

import com.einvoicehub.core.entity.mysql.ApiCredential;
import com.einvoicehub.core.entity.mysql.Merchant;
import com.einvoicehub.core.repository.mysql.ApiCredentialRepository;
import com.einvoicehub.core.repository.mysql.MerchantRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Filter xác thực API Key cho mọi request đến Controller
 * Kế thừa OncePerRequestFilter để đảm bảo chỉ chạy một lần per request
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ApiKeyFilter extends OncePerRequestFilter {

    private static final String API_KEY_HEADER = "X-API-KEY";
    private static final String CLIENT_ID_HEADER = "X-CLIENT-ID";
    private static final String SCOPE_READ = "invoice.view";
    private static final String SCOPE_CREATE = "invoice.create";
    private static final String SCOPE_DELETE = "invoice.delete";
    private static final String SCOPE_CONFIG = "config.manage";

    private final ApiCredentialRepository apiCredentialRepository;
    private final MerchantRepository merchantRepository;

    // Các endpoint không cần xác thực API Key
    private static final Set<String> PUBLIC_PATHS = Set.of(
            "/api/v1/auth/**",
            "/api/v1/health/**",
            "/api/v1/public/**",
            "/actuator/**"
    );

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();

        // Bỏ qua các endpoint public
        if (isPublicPath(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        // Lấy headers
        String clientId = request.getHeader(CLIENT_ID_HEADER);
        String apiKey = request.getHeader(API_KEY_HEADER);

        // Validate headers
        if (clientId == null || clientId.isBlank()) {
            log.warn("Missing client ID for request: {}", path);
            sendUnauthorizedResponse(response, "Missing client ID header (X-CLIENT-ID)");
            return;
        }

        if (apiKey == null || apiKey.isBlank()) {
            log.warn("Missing API key for client: {}", clientId);
            sendUnauthorizedResponse(response, "Missing API key header (X-API-KEY)");
            return;
        }

        // Tìm credential
        ApiCredential credential = apiCredentialRepository
                .findByClientIdAndIsActiveTrue(clientId)
                .orElse(null);

        if (credential == null) {
            log.warn("Invalid client ID: {}", clientId);
            sendUnauthorizedResponse(response, "Invalid client credentials");
            return;
        }

        // Validate API Key
        String apiKeyHash = hashApiKey(apiKey);
        if (!apiKeyHash.equals(credential.getApiKeyHash())) {
            log.warn("Invalid API key for client: {}", clientId);
            credential.incrementFailedAttempts();
            apiCredentialRepository.save(credential);
            sendUnauthorizedResponse(response, "Invalid API key");
            return;
        }

        // Kiểm tra credential còn hiệu lực
        if (!credential.isValid()) {
            if (credential.isExpired()) {
                log.warn("API key expired for client: {}", clientId);
                sendUnauthorizedResponse(response, "API key has expired");
            } else {
                log.warn("API key inactive for client: {}", clientId);
                sendUnauthorizedResponse(response, "API key is inactive");
            }
            return;
        }

        // Kiểm tra IP whitelist (nếu có)
        if (credential.getIpWhitelist() != null && !credential.getIpWhitelist().isBlank()) {
            String clientIp = getClientIp(request);
            if (!isIpAllowed(clientIp, credential.getIpWhitelist())) {
                log.warn("IP not allowed: {} for client: {}", clientIp, clientId);
                sendForbiddenResponse(response, "IP address not allowed");
                return;
            }
        }

        // Kiểm tra rate limit
        if (!credential.canMakeRequest()) {
            log.warn("Rate limit exceeded for client: {}", clientId);
            sendRateLimitResponse(response);
            return;
        }

        // Lấy Merchant
        Merchant merchant = credential.getMerchant();
        if (merchant == null || merchant.getIsDeleted() ||
                merchant.getStatus() != com.einvoicehub.core.entity.enums.EntityStatus.ACTIVE) {
            log.warn("Merchant not active for client: {}", clientId);
            sendForbiddenResponse(response, "Merchant account is not active");
            return;
        }

        // Tạo authorities từ scopes
        List<SimpleGrantedAuthority> authorities = buildAuthorities(credential.getScopes());

        // Tạo authentication token
        ApiKeyAuthenticationToken authentication = new ApiKeyAuthenticationToken(
                clientId, apiKey, credential, merchant, authorities
        );

        // Cập nhật thông tin sử dụng
        credential.incrementRequestCount();
        credential.updateLastUsed();
        apiCredentialRepository.save(credential);

        // Set authentication vào SecurityContext
        SecurityContextHolder.getContext().setAuthentication(authentication);

        log.debug("Authenticated request for client: {}, merchant: {}",
                clientId, merchant.getTaxCode());

        // Tiếp tục filter chain
        filterChain.doFilter(request, response);
    }

    /**
     * Kiểm tra path có phải là public không
     */
    private boolean isPublicPath(String path) {
        return PUBLIC_PATHS.stream().anyMatch(pattern -> pathMatcher.match(pattern, path));
    }

    /**
     * Hash API key bằng SHA-256
     */
    private String hashApiKey(String apiKey) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(apiKey.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }

    /**
     * Lấy IP thực của client (có xử lý proxy)
     */
    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isBlank()) {
            return xForwardedFor.split(",")[0].trim();
        }
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isBlank()) {
            return xRealIp;
        }
        return request.getRemoteAddr();
    }

    /**
     * Kiểm tra IP có trong whitelist không
     */
    private boolean isIpAllowed(String clientIp, String ipWhitelist) {
        try {
            List<String> allowedIps = Arrays.asList(ipWhitelist.split(","));
            for (String allowedIp : allowedIps) {
                String trimmed = allowedIp.trim();
                if (trimmed.equals(clientIp)) {
                    return true;
                }
                // Hỗ trợ CIDR notation
                if (trimmed.contains("/")) {
                    if (isIpInCidr(clientIp, trimmed)) {
                        return true;
                    }
                }
            }
            return false;
        } catch (Exception e) {
            log.error("Error checking IP whitelist", e);
            return false;
        }
    }

    /**
     * Kiểm tra IP có trong range CIDR không (simplified)
     */
    private boolean isIpInCidr(String ip, String cidr) {
        try {
            String[] parts = cidr.split("/");
            String cidrIp = parts[0];
            int prefix = Integer.parseInt(parts[1]);

            long ipLong = ipToLong(ip);
            long cidrIpLong = ipToLong(cidrIp);
            long mask = -1L << (32 - prefix);

            return (ipLong & mask) == (cidrIpLong & mask);
        } catch (Exception e) {
            return false;
        }
    }

    private long ipToLong(String ip) {
        String[] octets = ip.split("\\.");
        long result = 0;
        for (int i = 0; i < 4; i++) {
            result |= (Long.parseLong(octets[i]) << ((3 - i) * 8));
        }
        return result;
    }

    /**
     * Build authorities từ scopes string
     */
    private List<SimpleGrantedAuthority> buildAuthorities(String scopes) {
        if (scopes == null || scopes.isBlank()) {
            return Collections.singletonList(new SimpleGrantedAuthority("ROLE_API"));
        }

        return Arrays.stream(scopes.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(s -> new SimpleGrantedAuthority("SCOPE_" + s.toUpperCase().replace(".", "_")))
                .toList();
    }

    /**
     * Gửi phản hồi 401 Unauthorized
     */
    private void sendUnauthorizedResponse(HttpServletResponse response, String message)
            throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write(String.format(
                "{\"error\":\"Unauthorized\",\"message\":\"%s\",\"timestamp\":\"%s\"}",
                message, LocalDateTime.now()
        ));
    }

    /**
     * Gửi phản hồi 403 Forbidden
     */
    private void sendForbiddenResponse(HttpServletResponse response, String message)
            throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        response.getWriter().write(String.format(
                "{\"error\":\"Forbidden\",\"message\":\"%s\",\"timestamp\":\"%s\"}",
                message, LocalDateTime.now()
        ));
    }

    /**
     * Gửi phản hồi 429 Rate Limit
     */
    private void sendRateLimitResponse(HttpServletResponse response) throws IOException {
        response.setStatus(429);
        response.setHeader("Retry-After", "60");
        response.setContentType("application/json");
        response.getWriter().write(String.format(
                "{\"error\":\"Too Many Requests\",\"message\":\"Rate limit exceeded\",\"retryAfter\":60}",
                LocalDateTime.now()
        ));
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        // Chỉ filter các request đến /api/
        return !path.startsWith("/api/");
    }
}