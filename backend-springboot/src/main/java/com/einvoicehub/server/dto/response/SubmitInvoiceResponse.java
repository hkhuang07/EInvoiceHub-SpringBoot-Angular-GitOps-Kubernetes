package com.einvoicehub.server.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**Response DTO dùng chung cho:
 *  • SubmitInvoice       (submitInvoiceType 100/101/102)
 *  • SubmitAdjustInvoice (submitInvoiceType 111/112) */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubmitInvoiceResponse {

    @JsonProperty("invoice_id")
    private String invoiceId;

    @JsonProperty("partner_invoice_id")
    private String partnerInvoiceId;

    @JsonProperty("provider_invoice_id")
    private String providerInvoiceId;

    @JsonProperty("invoice_form")
    private String invoiceForm;

    @JsonProperty("invoice_series")
    private String invoiceSeries;

    @JsonProperty("invoice_no")
    private String invoiceNo;

    @JsonProperty("invoice_date")
    private LocalDateTime invoiceDate;

    @JsonProperty("status_id")
    private Byte statusId;

    @JsonProperty("status_name")
    private String statusName;

    /** 0 = Gốc, 2 = Điều chỉnh, 3 = Thay thế */
    @JsonProperty("reference_type_id")
    private Byte referenceTypeId;

    @JsonProperty("provider")
    private String provider;

    @JsonProperty("invoice_lookup_code")
    private String invoiceLookupCode;

    /**Link tra cứu đầy đủ= einv_provider.lookup_url + einv_invoices.invoice_lookup_code*/
    @JsonProperty("url_lookup")
    private String urlLookup;

    /** Tổng tiền hàng trước chiết khấu*/
    @JsonProperty("gross_amount")
    private BigDecimal grossAmount;

    @JsonProperty("discount_amount")
    private BigDecimal discountAmount;

    /** Tổng tiền trước thuế */
    @JsonProperty("net_amount")
    private BigDecimal netAmount;

    @JsonProperty("tax_amount")
    private BigDecimal taxAmount;

    @JsonProperty("total_amount")
    private BigDecimal totalAmount;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("signed_date")
    private LocalDateTime signedDate;

    @JsonProperty("message")
    private String message;

    @JsonProperty("error_code")
    private String errorCode;

    @JsonProperty("response_message")
    private String responseMessage;

    public static SubmitInvoiceResponse success(String invoiceId, String partnerInvoiceId,
                                                String providerInvoiceId, String invoiceNo,
                                                Byte statusId) {
        return SubmitInvoiceResponse.builder()
                .invoiceId(invoiceId)
                .partnerInvoiceId(partnerInvoiceId)
                .providerInvoiceId(providerInvoiceId)
                .invoiceNo(invoiceNo)
                .statusId(statusId)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static SubmitInvoiceResponse error(String partnerInvoiceId, String errorCode, String message) {
        return SubmitInvoiceResponse.builder()
                .partnerInvoiceId(partnerInvoiceId)
                .errorCode(errorCode)
                .message(message)
                .build();
    }
}