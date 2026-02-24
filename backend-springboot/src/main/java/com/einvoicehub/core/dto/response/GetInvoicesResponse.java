package com.einvoicehub.core.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetInvoicesResponse {

    @JsonProperty("PartnerInvoiceID")
    private String partnerInvoiceId;

    @JsonProperty("InvoiceID")
    private Long invoiceId;

    @JsonProperty("InvoiceNumber")
    private String invoiceNumber;

    @JsonProperty("InvoiceStatusID")
    private Integer invoiceStatusId;

    @JsonProperty("StatusName")
    private String statusName;

    @JsonProperty("TotalAmount")
    private BigDecimal totalAmount;

    @JsonProperty("Details")
    private List<GetInvoiceDetailResponse> details;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetInvoiceDetailResponse {
        @JsonProperty("ItemName")
        private String itemName;

        @JsonProperty("Quantity")
        private BigDecimal quantity;

        @JsonProperty("Price")
        private BigDecimal price;

        @JsonProperty("TotalAmount")
        private BigDecimal totalAmount;
    }
}