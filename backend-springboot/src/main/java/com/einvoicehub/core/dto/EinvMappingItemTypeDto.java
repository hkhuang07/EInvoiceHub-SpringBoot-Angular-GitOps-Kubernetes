package com.einvoicehub.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * Mapping: Loại hàng hóa trên dòng HĐ (HUB) ↔ (NCC)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EinvMappingItemTypeDto {

    @JsonProperty("ID")
    private Long id;

    @JsonProperty("ProviderID")
    private String providerId;

    @JsonProperty("SystemCode")
    private String systemCode; // Mã loại item nội bộ HUB

    @JsonProperty("ProviderCode")
    private String providerCode;

    @JsonProperty("Description")
    private String description;
}