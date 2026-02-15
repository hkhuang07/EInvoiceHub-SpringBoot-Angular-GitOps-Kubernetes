package com.einvoicehub.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EinvApiCredentialDto {

    @JsonProperty("ID")
    private Long id;

    @JsonProperty("MerchantID")
    private Long merchantId;

    @JsonProperty("MerchantName")
    private String merchantName;

    @JsonProperty("ClientID")
    private String clientId;

    @JsonProperty("ApiKeyPrefix")
    private String apiKeyPrefix;

    @JsonProperty("Scopes")
    private String scopes; // JSON array string

    @JsonProperty("IpWhitelist")
    private String ipWhitelist;

    @JsonProperty("RateLimitPerHour")
    private Integer rateLimitPerHour;

    @JsonProperty("RateLimitPerDay")
    private Integer rateLimitPerDay;

    @JsonProperty("RequestCountHour")
    private Integer requestCountHour;

    @JsonProperty("RequestCountDay")
    private Integer requestCountDay;

    @JsonProperty("RequestCountResettedAt")
    private String requestCountResettedAt;

    @JsonProperty("ExpiredAt")
    private String expiredAt;

    @JsonProperty("LastUsedAt")
    private String lastUsedAt;

    @JsonProperty("IsActive")
    private Boolean isActive;

    @JsonProperty("CreatedAt")
    private String createdAt;

    @JsonProperty("UpdatedAt")
    private String updatedAt;
}