package com.einvoicehub.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EinvInvoiceTypeDto {
    @JsonProperty("ID")
    private Long id;

    @JsonProperty("TypeCode")
    private String typeCode;

    @JsonProperty("TypeName")
    private String typeName;

    @JsonProperty("Description")
    private String description;

    @JsonProperty("IsActive")
    private Boolean isActive;

    @JsonProperty("DisplayOrder")
    private Integer displayOrder;
}