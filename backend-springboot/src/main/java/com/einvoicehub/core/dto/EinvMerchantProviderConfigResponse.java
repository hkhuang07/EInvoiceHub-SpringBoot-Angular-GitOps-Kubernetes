package com.einvoicehub.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EinvMerchantProviderConfigResponse {

    @JsonProperty("ID")
    private Long id;

    @JsonProperty("MerchantID")
    private Long merchantId;

    @JsonProperty("MerchantName")
    private String merchantName;

    @JsonProperty("MerchantTaxCode")
    private String merchantTaxCode;

    @JsonProperty("ProviderID")
    private Long providerId;

    @JsonProperty("ProviderCode")
    private String providerCode; // MISA, BKAV, MOBIFONE...

    @JsonProperty("ProviderName")
    private String providerName;

    @JsonProperty("PartnerID")
    private String partnerId;

    @JsonProperty("TaxCode")
    private String taxCode;

    @JsonProperty("IntegrationUrl")
    private String integrationUrl;

    @JsonProperty("UsernameService")
    private String usernameService;

    @JsonProperty("IsTestMode")
    private Boolean isTestMode;

    @JsonProperty("IsActive")
    private Boolean isActive;

    @JsonProperty("IsDefault")
    private Boolean isDefault;

    @JsonProperty("LastSyncAt")
    private LocalDateTime lastSyncAt;

    @JsonProperty("CreatedAt")
    private LocalDateTime createdAt;

    @JsonProperty("UpdatedAt")
    private LocalDateTime updatedAt;
}