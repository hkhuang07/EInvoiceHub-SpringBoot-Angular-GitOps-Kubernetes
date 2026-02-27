package com.einvoicehub.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EinvStoreProviderDto {

    @JsonProperty("ID")
    private String id; // UUID

    @JsonProperty("StoreID")
    private String storeId;

    @JsonProperty("ProviderID")
    private String providerId;

    @JsonProperty("SignType")
    private Integer signType;

    @JsonProperty("IsTwoStepSigning")
    private Boolean isTwoStepSigning;

    @JsonProperty("PartnerID")
    private String partnerId;

    @JsonProperty("AppID")
    private String appId;

    @JsonProperty("FkeyPrefix")
    private String fkeyPrefix;

    @JsonProperty("PartnerToken")
    private String partnerToken;

    @JsonProperty("PartnerPwd")
    private String partnerPwd;

    @JsonProperty("PartnerUsr")
    private String partnerUsr;

    @JsonProperty("UsernameService")
    private String usernameService;

    @JsonProperty("PasswordService")
    private String passwordService;

    @JsonProperty("IntegrationUrl")
    private String integrationUrl;

    @JsonProperty("TaxCode")
    private String taxCode;

    @JsonProperty("Status")
    private Integer status;

    @JsonProperty("IntegratedDate")
    private LocalDateTime integratedDate;
}