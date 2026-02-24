package com.einvoicehub.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EinvStoreProviderDto {

    @JsonProperty("ProviderID")
    private Long providerId;

    @JsonProperty("ProviderCode")
    private String providerCode; //MISA, BKAV, MOBIFONE...

    @JsonProperty("TaxCode")
    private String taxCode;

    @JsonProperty("IntegrationUrl")
    private String integrationUrl;

    @JsonProperty("UsernameService")
    private String usernameService;

    @JsonProperty("PasswordService")
    private String passwordService;

    @JsonProperty("PartnerToken")
    private String partnerToken;

    @JsonProperty("IsTestMode")
    private Boolean isTestMode;

    @JsonProperty("IsActive")
    private Boolean isActive;
}