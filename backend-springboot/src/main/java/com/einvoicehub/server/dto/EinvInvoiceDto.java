package com.einvoicehub.server.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.Builder.Default;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EinvInvoiceDto {

    @JsonProperty("id")
    private String id;

    @JsonProperty("tenant_id")
    @Size(max = 36, message = "Tenant ID must not exceed 36 characters")
    private String tenantId;

    @JsonProperty("store_id")
    @Size(max = 36, message = "Store ID must not exceed 36 characters")
    private String storeId;

    @JsonProperty("provider_id")
    @Size(max = 36, message = "Provider ID must not exceed 36 characters")
    private String providerId;

    @JsonProperty("partner_invoice_id")
    @NotBlank(message = "Partner invoice ID is required")
    @Size(max = 50, message = "Partner invoice ID must not exceed 50 characters")
    private String partnerInvoiceId;

    @JsonProperty("provider_invoice_id")
    @Size(max = 50, message = "Provider invoice ID must not exceed 50 characters")
    private String providerInvoiceId;

    @JsonProperty("invoice_type_id")
    private Byte invoiceTypeId;

    @JsonProperty("reference_type_id")
    private Byte referenceTypeId;

    @JsonProperty("status_id")
    private Byte statusId;

    @JsonProperty("invoice_form")
    @Size(max = 50, message = "Invoice form must not exceed 50 characters")
    private String invoiceForm;

    @JsonProperty("invoice_series")
    @Size(max = 50, message = "Invoice series must not exceed 50 characters")
    private String invoiceSeries;

    @JsonProperty("invoice_no")
    @Size(max = 50, message = "Invoice number must not exceed 50 characters")
    private String invoiceNo;

    @JsonProperty("invoice_date")
    private LocalDateTime invoiceDate;

    @JsonProperty("signed_date")
    private LocalDateTime signedDate;

    @JsonProperty("payment_method_id")
    private Byte paymentMethodId;

    @JsonProperty("buyer_tax_code")
    @Size(max = 50, message = "Buyer tax code must not exceed 50 characters")
    private String buyerTaxCode;

    @JsonProperty("buyer_company")
    @Size(max = 300, message = "Buyer company must not exceed 300 characters")
    private String buyerCompany;

    @JsonProperty("buyer_id_no")
    @Size(max = 20, message = "Buyer ID number must not exceed 20 characters")
    private String buyerIdNo;

    @JsonProperty("buyer_full_name")
    @Size(max = 200, message = "Buyer full name must not exceed 200 characters")
    private String buyerFullName;

    @JsonProperty("buyer_address")
    @Size(max = 300, message = "Buyer address must not exceed 300 characters")
    private String buyerAddress;

    @JsonProperty("buyer_mobile")
    @Size(max = 50, message = "Buyer mobile must not exceed 50 characters")
    private String buyerMobile;

    @JsonProperty("buyer_bank_account")
    @Size(max = 50, message = "Buyer bank account must not exceed 50 characters")
    private String buyerBankAccount;

    @JsonProperty("buyer_bank_name")
    @Size(max = 200, message = "Buyer bank name must not exceed 200 characters")
    private String buyerBankName;

    @JsonProperty("buyer_budget_code")
    @Size(max = 20, message = "Buyer budget code must not exceed 20 characters")
    private String buyerBudgetCode;

    @JsonProperty("receive_type_id")
    private Byte receiveTypeId;

    @JsonProperty("receiver_email")
    @Size(max = 300, message = "Receiver email must not exceed 300 characters")
    @Email(message = "Receiver email must be a valid email address")
    private String receiverEmail;

    @JsonProperty("currency_code")
    @Size(max = 20, message = "Currency code must not exceed 20 characters")
    @Default
    private String currencyCode = "VND";

    @JsonProperty("exchange_rate")
    @DecimalMin(value = "0.01", inclusive = true, message = "Exchange rate must be > 0")
    @Default
    private BigDecimal exchangeRate = BigDecimal.ONE;

    @JsonProperty("tax_authority_code")
    @Size(max = 50, message = "Tax authority code must not exceed 50 characters")
    private String taxAuthorityCode;

    @JsonProperty("invoice_lookup_code")
    @Size(max = 50, message = "Invoice lookup code must not exceed 50 characters")
    private String invoiceLookupCode;

    @JsonProperty("org_invoice_id")
    @Size(max = 36, message = "Original invoice ID must not exceed 36 characters")
    private String orgInvoiceId;

    @JsonProperty("org_invoice_form")
    @Size(max = 50, message = "Original invoice form must not exceed 50 characters")
    private String orgInvoiceForm;

    @JsonProperty("org_invoice_series")
    @Size(max = 50, message = "Original invoice series must not exceed 50 characters")
    private String orgInvoiceSeries;

    @JsonProperty("org_invoice_no")
    @Size(max = 50, message = "Original invoice number must not exceed 50 characters")
    private String orgInvoiceNo;

    @JsonProperty("org_invoice_date")
    private LocalDateTime orgInvoiceDate;

    @JsonProperty("org_invoice_reason")
    @Size(max = 500, message = "Original invoice reason must not exceed 500 characters")
    private String orgInvoiceReason;

    @JsonProperty("gross_amount")
    @DecimalMin(value = "0.0", inclusive = true, message = "Gross amount must be >= 0")
    private BigDecimal grossAmount;

    @JsonProperty("discount_amount")
    @DecimalMin(value = "0.0", inclusive = true, message = "Discount amount must be >= 0")
    private BigDecimal discountAmount;

    @JsonProperty("net_amount")
    @DecimalMin(value = "0.0", inclusive = true, message = "Net amount must be >= 0")
    private BigDecimal netAmount;

    @JsonProperty("tax_amount")
    @DecimalMin(value = "0.0", inclusive = true, message = "Tax amount must be >= 0")
    private BigDecimal taxAmount;

    @JsonProperty("total_amount")
    @DecimalMin(value = "0.0", inclusive = true, message = "Total amount must be >= 0")
    private BigDecimal totalAmount;

    @JsonProperty("tax_status_id")
    @Default
    private Byte taxStatusId = 0;

    @JsonProperty("cqt_response_code")
    @Size(max = 10, message = "CQT response code must not exceed 10 characters")
    private String cqtResponseCode;

    @JsonProperty("provider_response_id")
    @Size(max = 100, message = "Provider response ID must not exceed 100 characters")
    private String providerResponseId;

    @JsonProperty("is_draft")
    @Default
    private Boolean isDraft = false;

    @JsonProperty("is_mtt")
    @Default
    private Boolean isMtt = false;

    @JsonProperty("is_petrol")
    @Default
    private Boolean isPetrol = false;

    @JsonProperty("is_locked")
    @Default
    private Boolean isLocked = false;

    @JsonProperty("is_deleted")
    @Default
    private Boolean isDeleted = false;

    @JsonProperty("sign_type")
    @Min(value = 0, message = "Sign type must be 0 (Token) or 1 (HSM)")
    @Max(value = 1, message = "Sign type must be 0 (Token) or 1 (HSM)")
    private Byte signType;

    @JsonProperty("submit_invoice_type")
    @Size(max = 3, message = "Submit invoice type must not exceed 3 characters")
    private String submitInvoiceType;

    @JsonProperty("response_message")
    @Size(max = 500, message = "Response message must not exceed 500 characters")
    private String responseMessage;

    @JsonProperty("error_code")
    @Size(max = 50, message = "Error code must not exceed 50 characters")
    private String errorCode;

    @JsonProperty("secret_code")
    @Size(max = 50, message = "Secret code must not exceed 50 characters")
    private String secretCode;

    @JsonProperty("buyer_plate_no")
    @Size(max = 50, message = "Buyer plate number must not exceed 50 characters")
    private String buyerPlateNo;

    @JsonProperty("extra_metadata")
    private JsonNode extraMetadata;

    @JsonProperty("delivery_info")
    private JsonNode deliveryInfo;

    @JsonProperty("total_amount_text")
    @Size(max = 500, message = "Total amount text must not exceed 500 characters")
    private String totalAmountText;

    @JsonProperty("tax_summary_json")
    private JsonNode taxSummaryJson;

    @JsonProperty("notes")
    @Size(max = 300, message = "Notes must not exceed 300 characters")
    private String notes;

    @JsonProperty("details")
    @Valid
    private List<EinvInvoiceDetailDto> details;

    @JsonProperty("created_by")
    @Size(max = 36, message = "Created by must not exceed 36 characters")
    private String createdBy;

    @JsonProperty("updated_by")
    @Size(max = 36, message = "Updated by must not exceed 36 characters")
    private String updatedBy;

    @JsonProperty("created_date")
    private LocalDateTime createdDate;

    @JsonProperty("updated_date")
    private LocalDateTime updatedDate;
}