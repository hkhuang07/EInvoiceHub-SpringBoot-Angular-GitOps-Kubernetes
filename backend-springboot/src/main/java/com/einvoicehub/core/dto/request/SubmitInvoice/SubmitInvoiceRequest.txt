package com.einvoicehub.core.dto.request.SubmitInvoice;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * Request Header hóa đơn từ POS gửi lên HUB để tạo mới hoặc điều chỉnh/thay thế.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubmitInvoiceRequest {

    @NotBlank(message = "ID tham chiếu từ POS (PartnerInvoiceID) không được để trống")
    @JsonProperty("PartnerInvoiceID")
    private String partnerInvoiceId;

    @NotBlank(message = "Loại nghiệp vụ (SubmitInvoiceType) là bắt buộc")
    @JsonProperty("SubmitInvoiceType")
    private String submitInvoiceType;

    @NotNull(message = "Loại hóa đơn (InvoiceTypeID) là bắt buộc")
    @JsonProperty("InvoiceTypeID")
    private Integer invoiceTypeId;

    @JsonProperty("ReferenceTypeID")
    private Integer referenceTypeId;

    @JsonProperty("PaymentMethodID")
    private Integer paymentMethodId;

    @JsonProperty("InvoiceForm")
    private String invoiceForm;

    @JsonProperty("InvoiceSeries")
    private String invoiceSeries;

    @JsonProperty("InvoiceDate")
    private String invoiceDate;

    @JsonProperty("BuyerTaxCode")
    private String buyerTaxCode;

    @JsonProperty("BuyerCompany")
    private String buyerCompany;

    @NotBlank(message = "Tên người mua không được để trống")
    @JsonProperty("BuyerName")
    private String BuyerName;

    @JsonProperty("BuyerAddress")
    private String buyerAddress;

    @JsonProperty("BuyerIDNo")
    private String buyerIdNo;

    @JsonProperty("BuyerBankAccount")
    private String buyerBankAccount;

    @JsonProperty("BuyerBankName")
    private String buyerBankName;

    @JsonProperty("BuyerBudgetCode")
    private String buyerBudgetCode;

    @Email(message = "Email không đúng định dạng")
    @JsonProperty("BuyerEmail")
    private String buyerEmail;

    @JsonProperty("BuyerMobile")
    private String buyerMobile;

    @NotNull(message = "ReceiveTypeID là bắt buộc")
    @JsonProperty("ReceiveTypeID")
    private Integer receiveTypeId;

    @JsonProperty("ReceiverEmail")
    private String receiverEmail;

    @NotBlank(message = "Mã tiền tệ là bắt buộc")
    @JsonProperty("CurrencyCode")
    private String currencyCode;

    @NotNull(message = "Tỷ giá là bắt buộc")
    @JsonProperty("ExchangeRate")
    private BigDecimal exchangeRate;

    @DecimalMin("0.00")
    @JsonProperty("GrossAmount")
    private BigDecimal grossAmount;

    @DecimalMin("0.00")
    @JsonProperty("DiscountAmount")
    private BigDecimal discountAmount;

    @DecimalMin("0.00")
    @JsonProperty("TaxAmount")
    private BigDecimal taxAmount;

    @NotNull(message = "Tổng tiền thanh toán là bắt buộc")
    @DecimalMin("0.00")
    @JsonProperty("TotalAmount")
    private BigDecimal totalAmount;

    @JsonProperty("TotalAmountText")
    private String totalAmountText;

    @JsonProperty("Notes")
    private String notes;

    @JsonProperty("IsMtt")
    private Boolean isMtt;

    @JsonProperty("IsPetrol")
    private Boolean isPetrol;

    @NotEmpty(message = "Danh sách dòng hàng hóa không được để trống")
    @Valid
    @JsonProperty("Details")
    private List<SubmitInvoiceDetailRequest> details;

    @JsonProperty("ExtraMetadata")
    private String extraMetadata;

    @JsonProperty("OrgInvoiceNo")
    private String orgInvoiceNo;

    @JsonProperty("OrgInvoiceDate")
    private String orgInvoiceDate;
}