package com.einvoicehub.core.provider;

import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProviderConfig {
    private Long configId;
    private String providerCode;
    private String providerName;
    private String username;

    @ToString.Exclude
    private String password;

    private String appId;

    @ToString.Exclude
    private String secretKey;

    private String customApiUrl;
    private String certificateSerial;

    @ToString.Exclude
    private String certificateData;
    private LocalDateTime certificateExpiredAt;

    private String extraConfigJson;

    @ToString.Exclude
    private String accessToken;
    private LocalDateTime tokenExpiredAt;

    public boolean hasValidCertificate() {
        return certificateData != null && !certificateData.isEmpty() &&
                (certificateExpiredAt == null || certificateExpiredAt.isAfter(LocalDateTime.now()));
    }

    /* Check if token is valid with a 5-minute safety buffer */
    public boolean hasValidToken() {
        return accessToken != null && !accessToken.isEmpty() &&
                (tokenExpiredAt == null || tokenExpiredAt.isAfter(LocalDateTime.now().plusMinutes(5)));
    }
}