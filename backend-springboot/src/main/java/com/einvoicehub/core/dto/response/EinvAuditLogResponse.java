package com.einvoicehub.core.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EinvAuditLogResponse {

    @JsonProperty("ID")
    private Long id;

    @JsonProperty("MerchantID")
    private Long merchantId;

    @JsonProperty("MerchantName")
    private String merchantName;

    @JsonProperty("UserID")
    private Long userId;

    @JsonProperty("UserName")
    private String userName;

    @JsonProperty("EntityType")
    private String entityType; // Invoice, Merchant, Config...

    @JsonProperty("EntityID")
    private Long entityId;

    @JsonProperty("Action")
    private String action;

    @JsonProperty("OldValue")
    private String oldValue; //old JSON

    @JsonProperty("NewValue")
    private String newValue; //new Json

    @JsonProperty("IpAddress")
    private String ipAddress;

    @JsonProperty("UserAgent")
    private String userAgent;

    @JsonProperty("RequestID")
    private String requestId;

    @JsonProperty("CreatedAt")
    private String createdAt;
}