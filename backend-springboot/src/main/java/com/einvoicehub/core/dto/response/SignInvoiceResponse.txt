package com.einvoicehub.core.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO Phản hồi kết quả ký số cho từng hóa đơn.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignInvoiceResponse {

    @JsonProperty("InvoiceID")
    private String invoiceId; // ID

    @JsonProperty("PartnerInvoiceID")
    private String partnerInvoiceId;

    @JsonProperty("IsSuccess")
    private boolean isSuccess;

    @JsonProperty("ErrorCode")
    private String errorCode;

    @JsonProperty("ErrorMessage")
    private String errorMessage;

    @JsonProperty("SignedDate")
    private String signedDate;

    @JsonProperty("SignedXml")
    private String signedXml;

    @JsonProperty("HashValue")
    private String hashValue;
}