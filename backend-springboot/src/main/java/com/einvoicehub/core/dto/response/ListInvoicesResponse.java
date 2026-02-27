package com.einvoicehub.core.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListInvoicesResponse {

    @JsonProperty("ID")
    private Long id;

    @JsonProperty("PartnerInvoiceID")
    private String partnerInvoiceId;

    @JsonProperty("InvoiceNo")
    private String invoiceNo;

    @JsonProperty("InvoiceSeries")
    private String invoiceSeries;

    @JsonProperty("InvoiceDate")
    private LocalDateTime invoiceDate;

    @JsonProperty("StatusID")
    private Integer statusId;

    @JsonProperty("StatusName")
    private String statusName;

    @JsonProperty("TotalAmount")
    private BigDecimal totalAmount;

    @JsonProperty("BuyerFullName")
    private String buyerFullName;

    @JsonProperty("BuyerTaxCode")
    private String buyerTaxCode;

    @JsonProperty("InvoiceLookupCode")
    private String invoiceLookupCode;
}