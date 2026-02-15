package com.einvoicehub.core.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EinvSubmitInvoiceRequest {

    @NotBlank(message = "Loại nghiệp vụ (SubmitInvoiceType) là bắt buộc")
    @JsonProperty("SubmitInvoiceType")
    private String submitInvoiceType;

    @JsonProperty("PartnerInvoiceID")
    private String partnerInvoiceId;
    @NotNull(message = "ID loại hóa đơn là bắt buộc")
    @JsonProperty("InvoiceTypeID")
    private Long invoiceTypeId;

    @JsonProperty("InvoiceDate")
    private String invoiceDate;

    @JsonProperty("InvoiceForm")
    private String invoiceForm;

    @JsonProperty("InvoiceSeries")
    private String invoiceSeries;

    @NotNull(message = "Phương thức thanh toán là bắt buộc")
    @JsonProperty("PaymentMethodID")
    private Long paymentMethodId; //

    @JsonProperty("BuyerTaxCode")
    private String buyerTaxCode;

    @JsonProperty("BuyerCompany")
    private String buyerCompany;

    @JsonProperty("BuyerName")
    private String buyerName;

    @JsonProperty("BuyerAddress")
    private String buyerAddress;

    @JsonProperty("BuyerIDNo")
    private String buyerIdNo;

    @JsonProperty("BuyerMobile")
    private String buyerMobile;

    @JsonProperty("BuyerBankAccount")
    private String buyerBankAccount;

    @JsonProperty("BuyerBankName")
    private String buyerBankName;

    @JsonProperty("BuyerBudgetCode")
    private String buyerBudgetCode;

    @Email(message = "Email nhận hóa đơn không hợp lệ")
    @JsonProperty("ReceiverEmail")
    private String receiverEmail;

    @NotBlank(message = "Mã tiền tệ là bắt buộc")
    @JsonProperty("CurrencyCode")
    private String currencyCode;

    @NotNull(message = "Tỷ giá là bắt buộc")
    @JsonProperty("ExchangeRate")
    private BigDecimal exchangeRate;

    @JsonProperty("Notes")
    private String notes;

    @NotEmpty(message = "Hóa đơn phải có ít nhất một dòng hàng")
    @Valid
    @JsonProperty("Details")
    private List<EinvSubmitInvoiceDetailRequest> details;
}