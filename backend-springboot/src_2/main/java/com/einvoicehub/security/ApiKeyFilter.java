package com.einvoicehub.security;

import com.einvoicehub.entity.ApiCredential;
import com.einvoicehub.entity.Merchant;
import com.einvoicehub.repository.ApiCredentialRepository;
import com.einvoicehub.repository.MerchantRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

/**
 * API Key Authentication Filter
 * 
 * Filter này xác thực request dựa trên API Key và Client ID
 * Trích xuất headers X-API-KEY và X-CLIENT-ID từ request
 * Kiểm tra credentials trong database và thiết lập SecurityContext
 */
@Component
public class ApiKeyFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(ApiKeyFilter.class);
    
    private static final String API_KEY_HEADER = "X-API-KEY";
    private static final String CLIENT_ID_HEADER = "X-CLIENT-ID";
    
    private final ApiCredentialRepository apiCredentialRepository;
    private final MerchantRepository merchantRepository;

    public ApiKeyFilter(ApiCredentialRepository apiCredentialRepository,
                       MerchantRepository merchantRepository) {
        this.apiCredentialRepository = apiCredentialRepository;
        this.merchantRepository = merchantRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                   HttpServletResponse response,
                                   FilterChain filterChain) throws ServletException, IOException {
        
        String path = request.getRequestURI();
        
        // Bỏ qua các endpoint công khai
        if (isPublicEndpoint(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        // Trích xuất headers
        String clientId = request.getHeader(CLIENT_ID_HEADER);
        String apiKey = request.getHeader(API_KEY_HEADER);

        // Validate required headers
        if (clientId == null || clientId.isBlank()) {
            logger.warn("Missing X-CLIENT-ID header for request: {}", path);
            sendUnauthorizedResponse(response, "Missing X-CLIENT-ID header");
            return;
        }

        if (apiKey == null || apiKey.isBlank()) {
            logger.warn("Missing X-API-KEY header for request: {}", path);
            sendUnauthorizedResponse(response, "Missing X-API-KEY header");
            return;
        }

        // Validate API credentials
        Optional<ApiCredential> credentialOpt = apiCredentialRepository
            .findByClientIdAndApiKey(clientId, apiKey);

        if (credentialOpt.isEmpty()) {
            logger.warn("Invalid credentials for clientId: {}", clientId);
            sendUnauthorizedResponse(response, "Invalid API credentials");
            return;
        }

        ApiCredential credential = credentialOpt.get();
        
        // Check if credential is active
        if (!credential.isActive()) {
            logger.warn("Inactive credentials for clientId: {}", clientId);
            sendUnauthorizedResponse(response, "API credentials are inactive");
            return;
        }

        // Get merchant associated with this credential
        Optional<Merchant> merchantOpt = merchantRepository.findById(credential.getMerchantId());
        if (merchantOpt.isEmpty()) {
            logger.warn("Merchant not found for clientId: {}", clientId);
            sendUnauthorizedResponse(response, "Merchant not found");
            return;
        }

        Merchant merchant = merchantOpt.get();
        
        // Check if merchant is active
        if (!merchant.isActive()) {
            logger.warn("Inactive merchant: {} (clientId: {})", merchant.getCompanyName(), clientId);
            sendUnauthorizedResponse(response, "Merchant account is inactive");
            return;
        }

        // Create authentication token
        UsernamePasswordAuthenticationToken authentication = 
            new UsernamePasswordAuthenticationToken(
                merchant,
                null,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_MERCHANT"))
            );

        // Set authentication in SecurityContext
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        logger.debug("Authenticated merchant: {} (clientId: {})", 
            merchant.getCompanyName(), clientId);

        // Continue with the filter chain
        filterChain.doFilter(request, response);
    }

    /**
     * Kiểm tra xem endpoint có phải là public endpoint không
     */
    private boolean isPublicEndpoint(String path) {
        return path.startsWith("/actuator") ||
               path.startsWith("/v3/api-docs") ||
               path.startsWith("/swagger-ui") ||
               path.startsWith("/swagger-ui.html") ||
               path.startsWith("/health") ||
               path.equals("/favicon.ico");
    }

    /**
     * Gửi response 401 Unauthorized
     */
    private void sendUnauthorizedResponse(HttpServletResponse response, String message) 
            throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write(String.format(
            "{\"error\": \"Unauthorized\", \"message\": \"%s\", \"status\": 401}", message));
    }
}