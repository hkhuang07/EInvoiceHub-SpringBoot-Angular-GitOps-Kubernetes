package com.einvoicehub.core.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EinvInvoiceAdjustmentResponse {

    @JsonProperty("ID")
    private Long id;

    @JsonProperty("OriginalInvoiceID")
    private Long originalInvoiceId;

    @JsonProperty("OriginalInvoiceNumber")
    private String originalInvoiceNumber;

    @JsonProperty("OriginalSymbolCode")
    private String originalSymbolCode;

    @JsonProperty("OriginalTemplateCode")
    private String originalTemplateCode;

    @JsonProperty("OriginalIssueDate")
    private String originalIssueDate;

    @JsonProperty("AdjustmentType")
    private String adjustmentType;

    @JsonProperty("AgreementNumber")
    private String agreementNumber;

    @JsonProperty("AgreementDate")
    private String agreementDate; // Định dạng dd/MM/yyyy theo SOFTZ

    @JsonProperty("ReasonDescription")
    private String reasonDescription;

    @JsonProperty("Status")
    private String status; // PENDING, APPROVED, REJECTED

    @JsonProperty("OldTotalAmount")
    private BigDecimal oldTotalAmount;

    @JsonProperty("NewTotalAmount")
    private BigDecimal newTotalAmount;

    @JsonProperty("DifferenceAmount")
    private BigDecimal differenceAmount;

    @JsonProperty("SubmittedToCqt")
    private Boolean submittedToCqt;

    @JsonProperty("CqtResponseCode")
    private String cqtResponseCode;

    @JsonProperty("CqtResponseMessage")
    private String cqtResponseMessage;

    @JsonProperty("ApprovedAt")
    private String approvedAt;

    @JsonProperty("CreatedAt")
    private String createdAt;
}