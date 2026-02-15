package com.einvoicehub.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EinvInvoiceTaxBreakdownDto {

    @JsonProperty("ID")
    private Long id;

    @JsonProperty("VatRateCode")
    private String vatRateCode;

    @JsonProperty("VatRatePercent")
    private BigDecimal vatRatePercent;

    @JsonProperty("SubtotalAmount")
    private BigDecimal subtotalAmount;

    @JsonProperty("TaxableAmount")
    private BigDecimal taxableAmount;

    @JsonProperty("TaxAmount")
    private BigDecimal taxAmount;

    @JsonProperty("TotalAmount")
    private BigDecimal totalAmount;
}