package com.einvoicehub.core.security.api;

import com.einvoicehub.core.exception.ErrorCode;
import com.einvoicehub.core.exception.InvalidDataException;
import com.einvoicehub.core.domain.entity.EinvApiCredentialsEntity;
import com.einvoicehub.core.domain.repository.EinvApiCredentialRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApiKeyAuthenticationService {

    private final EinvApiCredentialRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public EinvApiCredentialsEntity validateAndUsageUpdate(String clientId, String apiKey) {
        //Tìm cấu hình API bằng ClientID
        EinvApiCredentialsEntity credentials = repository.findByClientIdAndIsActiveTrue(clientId)
                .orElseThrow(() -> new InvalidDataException(ErrorCode.INVALID_DATA, "Client ID không hợp lệ hoặc đã bị khóa"));

        //Kiểm tra trạng thái Merchant
        if (Boolean.TRUE.equals(credentials.getMerchant().getIsDeleted())) {
            log.error("[API-Auth] Chặn truy cập: Merchant ID {} đã ngừng hoạt động", credentials.getMerchant().getId());
            throw new InvalidDataException(ErrorCode.MERCHANT_NOT_FOUND, "Doanh nghiệp đã ngừng hoạt động trên hệ thống");
        }

        // So sán h API Key bằng BCrypt
        if (!passwordEncoder.matches(apiKey, credentials.getApiKeyHash())) {
            log.warn("[API-Auth] Sai API Key cho ClientID: {}", clientId);
            throw new InvalidDataException(ErrorCode.INVALID_DATA, "Thông tin xác thực API không chính xác");
        }

        //Check thời hạn hiệu lực
        if (credentials.getExpiredAt() != null && credentials.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new InvalidDataException(ErrorCode.INVALID_DATA, "Khóa API đã hết hạn");
        }
        handleRateLimiting(credentials);

        credentials.setLastUsedAt(LocalDateTime.now());
        credentials.setRequestCountDay(credentials.getRequestCountDay() + 1);
        credentials.setRequestCountHour(credentials.getRequestCountHour() + 1);
        return repository.save(credentials);
    }

    private void handleRateLimiting(EinvApiCredentialsEntity credentials) {
        // Reset count nếu qua ngày mới
        LocalDateTime now = LocalDateTime.now();
        if (credentials.getRequestCountResettedAt() == null ||
                credentials.getRequestCountResettedAt().getDayOfYear() != now.getDayOfYear()) {
            credentials.setRequestCountDay(0);
            credentials.setRequestCountHour(0);
            credentials.setRequestCountResettedAt(now);
        }
        // Check có vượt ngưỡng
        if (credentials.getRequestCountDay() >= credentials.getRateLimitPerDay()) {
            log.error("[API-Limit] ClientID {} vượt giới hạn ngày: {}/{}",
                    credentials.getClientId(), credentials.getRequestCountDay(), credentials.getRateLimitPerDay());
            throw new InvalidDataException(ErrorCode.INVALID_DATA, "429: Vượt quá giới hạn yêu cầu trong ngày");
        }
    }
}