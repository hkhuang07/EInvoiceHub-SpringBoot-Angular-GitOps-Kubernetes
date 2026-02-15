package com.einvoicehub.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EinvMerchantUserResponse {

    @JsonProperty("ID")
    private Long id;

    @JsonProperty("Username")
    private String username;

    @JsonProperty("Email")
    private String email;

    @JsonProperty("FullName")
    private String fullName;

    @JsonProperty("Phone")
    private String phone;

    @JsonProperty("Role")
    private String role;

    @JsonProperty("IsActive")
    private Boolean isActive;

    @JsonProperty("LastLoginAt")
    private String lastLoginAt;

    @JsonProperty("MerchantID")
    private Long merchantId;

    @JsonProperty("MerchantName")
    private String merchantName;

    @JsonProperty("MerchantTaxCode")
    private String merchantTaxCode;
}