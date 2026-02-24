package com.einvoicehub.core.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EinvAuthResponse {

    @JsonProperty("AccessToken")
    private String accessToken;

    @JsonProperty("TokenType")
    @Builder.Default
    private String tokenType = "Bearer";

    @JsonProperty("Username")
    private String username;

    @JsonProperty("FullName")
    private String fullName;

    @JsonProperty("Role")
    private String role;

    @JsonProperty("MerchantID")
    private Long merchantId;

    @JsonProperty("MerchantName")
    private String merchantName;
}