package com.einvoicehub.core.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EinvInvoiceAdjustmentRequest {

    @NotNull(message = "ID hóa đơn gốc là bắt buộc")
    @JsonProperty("OriginalInvoiceID")
    private Long originalInvoiceId;

    @NotBlank(message = "Loại điều chỉnh (ADJUSTMENT, REPLACEMENT, CANCELLATION) là bắt buộc")
    @JsonProperty("AdjustmentType")
    private String adjustmentType;

    @NotBlank(message = "Số biên bản thỏa thuận không được để trống")
    @JsonProperty("AgreementNumber")
    private String agreementNumber;

    @NotNull(message = "Ngày lập biên bản là bắt buộc")
    @JsonProperty("AgreementDate")
    private LocalDate agreementDate;

    @JsonProperty("AgreementContent")
    private String agreementContent;

    @JsonProperty("Signers")
    private String signers;

    @JsonProperty("ReasonCode")
    private String reasonCode;

    @NotBlank(message = "Lý do điều chỉnh không được để trống")
    @JsonProperty("ReasonDescription")
    private String reasonDescription;

    @JsonProperty("OldSubtotalAmount")
    private BigDecimal oldSubtotalAmount;

    @JsonProperty("OldTaxAmount")
    private BigDecimal oldTaxAmount;

    @JsonProperty("OldTotalAmount")
    private BigDecimal oldTotalAmount;

    @JsonProperty("NewSubtotalAmount")
    private BigDecimal newSubtotalAmount;

    @JsonProperty("NewTaxAmount")
    private BigDecimal newTaxAmount;

    @JsonProperty("NewTotalAmount")
    private BigDecimal newTotalAmount;

    @JsonProperty("DifferenceAmount")
    private BigDecimal differenceAmount;
}