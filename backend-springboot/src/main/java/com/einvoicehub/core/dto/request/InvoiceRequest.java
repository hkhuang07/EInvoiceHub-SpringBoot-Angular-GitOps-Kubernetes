package com.einvoicehub.core.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceRequest {

    @NotBlank(message = "MERCHANT_NOT_FOUND")
    private String merchantId;

    @NotBlank(message = "VALIDATION_ERROR")
    @Pattern(regexp = "^(?i)(VNPT|MISA|BKAV|VIETTEL)$", message = "VALIDATION_ERROR")
    private String providerCode;

    @NotBlank(message = "VALIDATION_ERROR")
    private String invoiceType;

    @NotNull(message = "VALIDATION_ERROR")
    private LocalDateTime invoiceDate;

    @NotBlank(message = "VALIDATION_ERROR")
    @Builder.Default
    private String currency = "VND";

    @Builder.Default
    private String exchangeRate = "1";

    /* Seller Info */
    @NotBlank(message = "VALIDATION_ERROR")
    private String sellerName;

    @NotBlank(message = "VALIDATION_ERROR")
    @Pattern(regexp = "^\\d{10,13}$", message = "VALIDATION_ERROR")
    private String sellerTaxCode;

    @Size(max = 500, message = "VALIDATION_ERROR")
    private String sellerAddress;

    private String sellerPhone;
    private String sellerEmail;

    /* Buyer Info */
    @NotBlank(message = "VALIDATION_ERROR")
    private String buyerName;

    @Pattern(regexp = "^(\\d{10,13})?$", message = "VALIDATION_ERROR")
    private String buyerTaxCode;

    private String buyerAddress;
    private String buyerPhone;
    private String buyerEmail;

    /* Totals & Payment */
    @NotNull(message = "VALIDATION_ERROR")
    private PaymentMethod paymentMethod;

    @NotNull(message = "VALIDATION_ERROR")
    @DecimalMin(value = "0.0", message = "VALIDATION_ERROR")
    private BigDecimal totalAmount;

    @NotNull(message = "VALIDATION_ERROR")
    @DecimalMin(value = "0.0", message = "VALIDATION_ERROR")
    private BigDecimal totalTaxAmount;

    @NotNull(message = "VALIDATION_ERROR")
    @DecimalMin(value = "0.0", message = "VALIDATION_ERROR")
    private BigDecimal grandTotalAmount;

    @NotEmpty(message = "VALIDATION_ERROR")
    @Valid
    private List<InvoiceItemRequest> items;

    @NotBlank(message = "VALIDATION_ERROR")
    private String internalReferenceCode;

    private String invoiceNote;
    private String customerCode;
    private String orderNumber;

    public enum PaymentMethod {
        CASH, BANK_TRANSFER, CREDIT_CARD, E_WALLET, OTHER
    }
}