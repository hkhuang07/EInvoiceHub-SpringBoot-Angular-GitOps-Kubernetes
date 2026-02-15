package com.einvoicehub.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EinvPaymentMethodDto {
    @JsonProperty("ID")
    private Long id;

    @JsonProperty("MethodCode")
    private String methodCode;

    @JsonProperty("MethodName")
    private String methodName;

    @JsonProperty("Description")
    private String description;

    @JsonProperty("IsActive")
    private Boolean isActive;

    @JsonProperty("DisplayOrder")
    private Integer displayOrder;
}