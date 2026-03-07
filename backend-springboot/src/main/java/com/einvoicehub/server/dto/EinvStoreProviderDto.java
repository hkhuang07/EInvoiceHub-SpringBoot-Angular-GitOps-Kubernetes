package com.einvoicehub.server.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.Builder.Default;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EinvStoreProviderDto {

    @JsonProperty("id")
    private String id;

    @JsonProperty("tenant_id")
    @NotBlank(message = "Tenant ID is required")
    @Size(max = 36, message = "Tenant ID must not exceed 36 characters")
    private String tenantId;

    @JsonProperty("store_id")
    @NotBlank(message = "Store ID is required")
    @Size(max = 36, message = "Store ID must not exceed 36 characters")
    private String storeId;

    @JsonProperty("provider_id")
    @NotBlank(message = "Provider ID is required")
    @Size(max = 36, message = "Provider ID must not exceed 36 characters")
    private String providerId;

    @JsonProperty("partner_id")
    @Size(max = 200, message = "Partner ID must not exceed 200 characters")
    private String partnerId;

    @JsonProperty("partner_token")
    @Size(max = 200, message = "Partner token must not exceed 200 characters")
    private String partnerToken;

    @JsonProperty("partner_usr")
    @Size(max = 200, message = "Partner username must not exceed 200 characters")
    private String partnerUsr;

    @JsonProperty("partner_pwd")
    @Size(max = 200, message = "Partner password must not exceed 200 characters")
    private String partnerPwd;

    @JsonProperty("status")
    @NotNull(message = "Status is required")
    @Min(value = 0, message = "Status must be 0, 1 or 8")
    @Max(value = 8, message = "Status must be 0, 1 or 8")
    @Default
    private Byte status = 0;

    @JsonProperty("integrated_date")
    private LocalDateTime integratedDate;

    @JsonProperty("integration_url")
    @Size(max = 200, message = "Integration URL must not exceed 200 characters")
    private String integrationUrl;

    @JsonProperty("tax_code")
    @Size(max = 200, message = "Tax code must not exceed 200 characters")
    private String taxCode;

    @JsonProperty("sign_type")
    @Min(value = 0, message = "Sign type must be 0 (Token), 1 (HSM) or 2 (SmartCA)")
    @Max(value = 2, message = "Sign type must be 0 (Token), 1 (HSM) or 2 (SmartCA)")
    private Byte signType;

    @JsonProperty("is_two_step_signing")
    private Boolean isTwoStepSigning;

    @JsonProperty("app_id")
    @Size(max = 100, message = "App ID must not exceed 100 characters")
    private String appId;

    @JsonProperty("fkey_prefix")
    @Size(max = 50, message = "Fkey prefix must not exceed 50 characters")
    private String fkeyPrefix;

    @JsonProperty("username_service")
    @Size(max = 100, message = "Username service must not exceed 100 characters")
    private String usernameService;

    @JsonProperty("password_service")
    @Size(max = 255, message = "Password service must not exceed 255 characters")
    private String passwordService;

    @JsonProperty("created_by")
    private LocalDateTime createdBy;

    @JsonProperty("updated_by")
    private LocalDateTime updatedBy;

    @JsonProperty("created_date")
    private LocalDateTime createdDate;

    @JsonProperty("updated_date")
    private LocalDateTime updatedDate;
}