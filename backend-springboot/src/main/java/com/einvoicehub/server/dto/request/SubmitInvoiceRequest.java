package com.einvoicehub.server.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Request DTO dùng chung cho hai nghiệp vụ:
 *  • SubmitInvoice       → submitInvoiceType ∈ {"100","101","102"}, referenceTypeId = 0 (Gốc)
 *  • SubmitAdjustInvoice → submitInvoiceType ∈ {"111","112"},       referenceTypeId = 2 (Điều chỉnh)
 * Validation nghiệp vụ bổ sung (thực hiện tại Service layer):
 *  - Khi isAdjustRequest() == true: orgInvoiceId, orgInvoiceReason là bắt buộc.
 *  - Hóa đơn gốc phải có status_id ∈ {2, 8} và chưa bị thay thế (reference_type_id ≠ 3).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubmitInvoiceRequest {

    @NotBlank(message = "Tenant ID is required")
    @Size(max = 36, message = "Tenant ID must not exceed 36 characters")
    @JsonProperty("tenant_id")
    private String tenantId;

    @NotBlank(message = "Store ID is required")
    @Size(max = 36, message = "Store ID must not exceed 36 characters")
    @JsonProperty("store_id")
    private String storeId;

    @Size(max = 36, message = "Provider ID must not exceed 36 characters")
    @JsonProperty("provider_id")
    private String providerId;

    /**
     *   100 = Tạo HĐ (chờ ký)
     *   101 = Tạo HĐ MTT (chờ ký)
     *   102 = Tạo + ký phát hành luôn
     *   111 = Tạo HĐ điều chỉnh (chờ ký)
     *   112 = Tạo HĐ điều chỉnh + ký phát hành luôn
     */
    @NotBlank(message = "Submit invoice type is required")
    @Size(min = 3, max = 3, message = "Submit invoice type must be exactly 3 characters (e.g. '100', '111')")
    @JsonProperty("submit_invoice_type")
    private String submitInvoiceType;

    @Min(value = 0, message = "Invoice type ID must be >= 0")
    @Max(value = 11, message = "Invoice type ID must be <= 11")
    @JsonProperty("invoice_type_id")
    @Builder.Default
    private Byte invoiceTypeId = 0;

    /** 0 = Gốc  |  2 = Điều chỉnh  |  3 = Thay thế */
    @Min(value = 0, message = "Reference type ID must be 0, 2, or 3")
    @Max(value = 3, message = "Reference type ID must be 0, 2, or 3")
    @JsonProperty("reference_type_id")
    @Builder.Default
    private Byte referenceTypeId = 0;

    @NotBlank(message = "Partner invoice ID is required")
    @Size(max = 50, message = "Partner invoice ID must not exceed 50 characters")
    @JsonProperty("partner_invoice_id")
    private String partnerInvoiceId;

    @Size(max = 50, message = "Invoice form must not exceed 50 characters")
    @JsonProperty("invoice_form")
    private String invoiceForm;

    @Size(max = 50, message = "Invoice series must not exceed 50 characters")
    @JsonProperty("invoice_series")
    private String invoiceSeries;

    @NotNull(message = "Invoice date is required")
    @JsonProperty("invoice_date")
    private LocalDateTime invoiceDate;

    @NotNull(message = "Payment method ID is required")
    @Min(value = 1, message = "Payment method ID must be >= 1")
    @Max(value = 44, message = "Payment method ID must be <= 44")
    @JsonProperty("payment_method_id")
    private Byte paymentMethodId;

    @Size(max = 50, message = "Buyer tax code must not exceed 50 characters")
    @JsonProperty("buyer_tax_code")
    private String buyerTaxCode;

    @Size(max = 300, message = "Buyer company must not exceed 300 characters")
    @JsonProperty("buyer_company")
    private String buyerCompany;

    @Size(max = 20, message = "Buyer ID number must not exceed 20 characters")
    @JsonProperty("buyer_id_no")
    private String buyerIdNo;

    @Size(max = 200, message = "Buyer full name must not exceed 200 characters")
    @JsonProperty("buyer_full_name")
    private String buyerFullName;

    @Size(max = 300, message = "Buyer address must not exceed 300 characters")
    @JsonProperty("buyer_address")
    private String buyerAddress;

    @Size(max = 50, message = "Buyer mobile must not exceed 50 characters")
    @JsonProperty("buyer_mobile")
    private String buyerMobile;

    @Size(max = 50, message = "Buyer bank account must not exceed 50 characters")
    @JsonProperty("buyer_bank_account")
    private String buyerBankAccount;

    @Size(max = 200, message = "Buyer bank name must not exceed 200 characters")
    @JsonProperty("buyer_bank_name")
    private String buyerBankName;

    @Size(max = 20, message = "Buyer budget code must not exceed 20 characters")
    @JsonProperty("buyer_budget_code")
    private String buyerBudgetCode;

    @Min(value = 1, message = "Receive type ID must be >= 1")
    @Max(value = 4, message = "Receive type ID must be <= 4")
    @JsonProperty("receive_type_id")
    private Byte receiveTypeId;

    @Email(message = "Receiver email is invalid")
    @Size(max = 300, message = "Receiver email must not exceed 300 characters")
    @JsonProperty("receiver_email")
    private String receiverEmail;

    @Size(max = 20, message = "Currency code must not exceed 20 characters")
    @JsonProperty("currency_code")
    @Builder.Default
    private String currencyCode = "VND";

    @DecimalMin(value = "0.01", message = "Exchange rate must be > 0")
    @JsonProperty("exchange_rate")
    @Builder.Default
    private BigDecimal exchangeRate = BigDecimal.ONE;

    /**
     * ID hóa đơn gốc
     * Bắt buộc khi submitInvoiceType ∈ {"111","112"}.
     * Hóa đơn gốc phải có status_id ∈ {2, 8}.
     */
    @Size(max = 36, message = "Original invoice ID must not exceed 36 characters")
    @JsonProperty("org_invoice_id")
    private String orgInvoiceId;

    @Size(max = 50, message = "Original invoice form must not exceed 50 characters")
    @JsonProperty("org_invoice_form")
    private String orgInvoiceForm;

    @Size(max = 50, message = "Original invoice series must not exceed 50 characters")
    @JsonProperty("org_invoice_series")
    private String orgInvoiceSeries;

    @Size(max = 50, message = "Original invoice number must not exceed 50 characters")
    @JsonProperty("org_invoice_no")
    private String orgInvoiceNo;

    @JsonProperty("org_invoice_date")
    private LocalDateTime orgInvoiceDate;

    @Size(max = 500, message = "Original invoice reason must not exceed 500 characters")
    @JsonProperty("org_invoice_reason")
    private String orgInvoiceReason;

    /** Thành tiền hàng hóa trước chiết khấu*/
    @DecimalMin(value = "0.0", message = "Gross amount must be >= 0")
    @JsonProperty("gross_amount")
    private BigDecimal grossAmount;

    @DecimalMin(value = "0.0", message = "Discount amount must be >= 0")
    @JsonProperty("discount_amount")
    private BigDecimal discountAmount;

    /** Thành tiền trước thuế */
    @DecimalMin(value = "0.0", message = "Net amount must be >= 0")
    @JsonProperty("net_amount")
    private BigDecimal netAmount;

    @DecimalMin(value = "0.0", message = "Tax amount must be >= 0")
    @JsonProperty("tax_amount")
    private BigDecimal taxAmount;

    @DecimalMin(value = "0.0", message = "Total amount must be >= 0")
    @JsonProperty("total_amount")
    private BigDecimal totalAmount;

    @Builder.Default
    @JsonProperty("is_draft")
    private Boolean isDraft = false;

    /** Hóa đơn Máy tính tiền*/
    @Builder.Default
    @JsonProperty("is_mtt")
    private Boolean isMtt = false;

    /** Hóa đơn Xăng dầu */
    @Builder.Default
    @JsonProperty("is_petrol")
    private Boolean isPetrol = false;

    @Size(max = 50, message = "Buyer plate number must not exceed 50 characters")
    @JsonProperty("buyer_plate_no")
    private String buyerPlateNo;

    /**  0 = Token, 1 = HSM */
    @Min(value = 0, message = "Sign type must be 0 (Token) or 1 (HSM)")
    @Max(value = 1, message = "Sign type must be 0 (Token) or 1 (HSM)")
    @JsonProperty("sign_type")
    private Byte signType;

    @Size(max = 300, message = "Notes must not exceed 300 characters")
    @JsonProperty("notes")
    private String notes;

    @NotNull(message = "Invoice details are required")
    @NotEmpty(message = "Invoice details must not be empty")
    @Valid
    @JsonProperty("details")
    private List<SubmitInvoiceDetailRequest> details;

    @Size(max = 36, message = "Created by must not exceed 36 characters")
    @JsonProperty("created_by")
    private String createdBy;

    @Size(max = 36, message = "Updated by must not exceed 36 characters")
    @JsonProperty("updated_by")
    private String updatedBy;

    public boolean isAdjustRequest() {
        return "111".equals(submitInvoiceType) || "112".equals(submitInvoiceType);
    }

    public boolean isReplaceRequest() {
        return referenceTypeId != null && referenceTypeId == 3;
    }

    public boolean isSignAndPublishImmediately() {
        return "102".equals(submitInvoiceType) || "112".equals(submitInvoiceType);
    }

    /**
     * Tạo chuỗi OrgInvoiceIdentify cho Provider khi gọi API BKAV điều chỉnh/thay thế.
     * Format: [orgInvoiceForm]_[orgInvoiceSeries]_[orgInvoiceNo] | Ví dụ: "[1]_[C22TAA]_[0000001]"
     * @return chuỗi định danh hóa đơn gốc, hoặc null nếu thiếu thông tin
     */
    public String buildOrgInvoiceIdentify() {
        if (orgInvoiceForm == null || orgInvoiceSeries == null || orgInvoiceNo == null) {
            return null;
        }
        return "[" + orgInvoiceForm + "]_[" + orgInvoiceSeries + "]_[" + orgInvoiceNo + "]";
    }
}