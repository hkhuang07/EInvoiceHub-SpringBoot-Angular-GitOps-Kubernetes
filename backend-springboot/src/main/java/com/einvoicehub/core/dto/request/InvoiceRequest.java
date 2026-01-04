package com.einvoicehub.core.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Invoice Request DTO - Unified Invoice Request Model
 *
 * Model chuẩn hóa cho việc phát hành hóa đơn điện tử
 * Được sử dụng bởi tất cả các Provider adapters
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceRequest {

    @NotBlank(message = "Provider code is required")
    private String providerCode;

    @NotBlank(message = "Invoice type is required")
    private String invoiceType;

    @NotNull(message = "Invoice date is required")
    private LocalDateTime invoiceDate;

    @NotBlank(message = "Currency is required")
    @Builder.Default
    private String currency = "VND";

    @Builder.Default
    private String exchangeRate = "1";

    // Seller Information (Người bán)
    @NotBlank(message = "Seller name is required")
    private String sellerName;

    @NotBlank(message = "Seller tax code is required")
    private String sellerTaxCode;

    private String sellerAddress;
    private String sellerPhone;
    private String sellerEmail;
    private String sellerBankAccount;
    private String sellerBankName;

    // Buyer Information (Người mua)
    @NotBlank(message = "Buyer name is required")
    private String buyerName;

    private String buyerTaxCode;
    private String buyerAddress;
    private String buyerPhone;
    private String buyerEmail;
    private String buyerBankAccount;
    private String buyerBankName;

    // Payment Information
    private String paymentMethod;
    private String paymentStatus;

    // Invoice Details
    @NotEmpty(message = "At least one invoice item is required")
    @Valid
    private List<InvoiceItemRequest> items;

    private BigDecimal totalAmount;
    private BigDecimal totalTaxAmount;
    private BigDecimal discountAmount;
    private BigDecimal grandTotalAmount;

    // Additional Information
    private String invoiceNote;
    private String internalReferenceCode;
    private String customerCode;
    private String orderNumber;

    // Delivery Information
    private String deliveryAddress;
    private String deliveryName;
    private String deliveryPhone;
}
