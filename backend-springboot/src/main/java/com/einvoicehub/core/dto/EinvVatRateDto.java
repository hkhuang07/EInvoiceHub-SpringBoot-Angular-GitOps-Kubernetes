package com.einvoicehub.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EinvVatRateDto {
    @JsonProperty("ID")
    private Long id;

    @JsonProperty("RateCode")
    private String rateCode;

    @JsonProperty("RatePercent")
    private BigDecimal ratePercent;

    @JsonProperty("RateName")
    private String rateName;

    @JsonProperty("Description")
    private String description;

    @JsonProperty("IsActive")
    private Boolean isActive;

    @JsonProperty("DisplayOrder")
    private Integer displayOrder;
}