package com.einvoicehub.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EinvStoreDto {

    @JsonProperty("ID")
    private String id; // UUID

    @JsonProperty("TenantID")
    private Long tenantId;

    @JsonProperty("StoreName")
    private String storeName;

    @JsonProperty("TaxCode")
    private String taxCode;

    @JsonProperty("IsActive")
    private Boolean isActive;
}