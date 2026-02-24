package com.einvoicehub.core.dto.request.SubmitInvoice;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubmitInvoiceRequest {

    @NotBlank(message = "Loại nghiệp vụ là bắt buộc")
    @JsonProperty("SubmitInvoiceType")
    private String submitInvoiceType;

    @JsonProperty("PartnerInvoiceID")
    private Long partnerInvoiceId;

    @JsonProperty("PartnerInvoiceStringID")
    private String partnerInvoiceStringId;

    @NotNull(message = "ID loại hóa đơn là bắt buộc")
    @JsonProperty("InvoiceTypeID")
    private Long invoiceTypeId;

    @JsonProperty("InvoiceDate")
    private String invoiceDate;

    @JsonProperty("BuyerTaxCode")
    private String buyerTaxCode;

    @JsonProperty("BuyerCompany")
    private String buyerCompany;

    @JsonProperty("BuyerName")
    private String buyerName;

    @JsonProperty("BuyerAddress")
    private String buyerAddress;

    @Email(message = "Email không hợp lệ")
    @JsonProperty("ReceiverEmail")
    private String receiverEmail;

    @NotBlank(message = "Mã tiền tệ là bắt buộc")
    @JsonProperty("CurrencyCode")
    private String currencyCode;

    @NotNull(message = "Tỷ giá là bắt buộc")
    @JsonProperty("ExchangeRate")
    private BigDecimal exchangeRate;

    @Valid
    @NotEmpty(message = "Chi tiết hóa đơn không được để trống")
    @JsonProperty("Details")
    private List<SubmitInvoiceDetailRequest> details;
}