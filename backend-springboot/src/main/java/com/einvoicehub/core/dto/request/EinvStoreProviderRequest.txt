package com.einvoicehub.core.dto.request;

import com.einvoicehub.core.dto.EinvoiceHubRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/** request cập nhật cấu hình tích hợp Nhà cung cấp cho Chi nhánh */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EinvStoreProviderRequest {

    @JsonProperty("ID")
    private String id; // UUID

    @NotBlank(message = "StoreID không được để trống")
    @JsonProperty("StoreID")
    private String storeId;

    @NotBlank(message = "ProviderID không được để trống")
    @JsonProperty("ProviderID")
    private String providerId;

    @JsonProperty("SignType")
    private Integer signType; // 0: Token, 1: HSM, 2: SmartCA

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

    @NotBlank(message = "IntegrationUrl không được để trống")
    @JsonProperty("IntegrationUrl")
    private String integrationUrl;

    @JsonProperty("TaxCode")
    private String taxCode;

    @JsonProperty("Status")
    private Integer status;
}