package com.einvoicehub.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EinvoiceHubRequest<T> {

    @JsonProperty("RequestID")
    private String requestId;

    @JsonProperty("RequestDateTime")
    private String requestDateTime;

    @NotNull(message = "TenantID không được để trống")
    @JsonProperty("TenantID")
    private Long tenantId;

    @NotBlank(message = "StoreID không được để trống")
    @JsonProperty("StoreID")
    private String storeId; // VARCHAR(36) - UUID

    @JsonProperty("UserAgent")
    private String userAgent;

    @Valid
    @JsonProperty("Data")
    private T data;
}