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
public class MerchantDto {

    @JsonProperty("ID")
    private Long id;

    @JsonProperty("TenantID")
    private String tenantId;

    @JsonProperty("CompanyName")
    private String companyName;

    @JsonProperty("TaxCode")
    private String taxCode;

    @JsonProperty("IsActive")
    private Boolean isActive;

    @JsonProperty("CreatedAt")
    private LocalDateTime createdAt;
}