package com.einvoicehub.core.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignInvoiceResponse {

    @JsonProperty("PartnerInvoiceID")
    private String partnerInvoiceId;

    @JsonProperty("InvoiceID")
    private Long invoiceId;

    @JsonProperty("SignedAt")
    private String signedAt;

    @JsonProperty("Status")
    private String status;

    @JsonProperty("Message")
    private String message;
}