package com.einvoicehub.server.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SignInvoiceResult {

    @JsonProperty("invoice_id")
    private String invoiceId;

    @JsonProperty("partner_invoice_id")
    private String partnerInvoiceId;

    @JsonProperty("provider_invoice_id")
    private String providerInvoiceId;

    @JsonProperty("invoice_no")
    private String invoiceNo;

    @JsonProperty("status_id")
    private Byte statusId;

    @JsonProperty("status_name")
    private String statusName;

    @JsonProperty("signed_date")
    private LocalDateTime signedDate;

    @JsonProperty("invoice_lookup_code")
    private String invoiceLookupCode;

    @JsonProperty("url_lookup")
    private String urlLookup;

    @JsonProperty("result")
    private String result;

    @JsonProperty("error_code")
    private String errorCode;

    @JsonProperty("response_message")
    private String responseMessage;


    public static SignInvoiceResult success(String invoiceId, String invoiceNo,
                                            Byte statusId, LocalDateTime signedDate) {
        return SignInvoiceResult.builder()
                .invoiceId(invoiceId)
                .invoiceNo(invoiceNo)
                .statusId(statusId)
                .signedDate(signedDate)
                .result("SUCCESS")
                .build();
    }

    public static SignInvoiceResult failed(String invoiceId, String errorCode, String responseMessage) {
        return SignInvoiceResult.builder()
                .invoiceId(invoiceId)
                .errorCode(errorCode)
                .responseMessage(responseMessage)
                .result("FAILED")
                .build();
    }
}