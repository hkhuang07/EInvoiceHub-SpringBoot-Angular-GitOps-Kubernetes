package com.einvoicehub.core.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Invoice Request DTO - Unified Model for EInvoiceHub.
 * Hợp nhất các trường nghiệp vụ thực tế và hệ thống Validation dựa trên ErrorCode.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceRequest {

    /**
     * Mã Merchant - Bắt buộc để xác thực quyền hạn và Quota.
     * Khắc phục lỗi "Cannot resolve method getMerchantId" trong InvoiceService.
     */
    @NotBlank(message = "MERCHANT_NOT_FOUND")
    private String merchantId;

    /**
     * Mã Provider (VNPT, MISA, BKAV, VIETTEL).
     */
    @NotBlank(message = "VALIDATION_ERROR")
    @Pattern(regexp = "^(?i)(VNPT|MISA|BKAV|VIETTEL)$", message = "VALIDATION_ERROR")
    private String providerCode;

    @NotBlank(message = "VALIDATION_ERROR")
    private String invoiceType; // VD: 01GTKT

    @NotNull(message = "VALIDATION_ERROR")
    private LocalDateTime invoiceDate;

    @NotBlank(message = "VALIDATION_ERROR")
    @Builder.Default
    private String currency = "VND";

    @Builder.Default
    private String exchangeRate = "1";

    // --- THÔNG TIN NGƯỜI BÁN (SELLER) ---
    @NotBlank(message = "VALIDATION_ERROR")
    private String sellerName;

    @NotBlank(message = "VALIDATION_ERROR")
    @Pattern(regexp = "^\\d{10,13}$", message = "VALIDATION_ERROR")
    private String sellerTaxCode;

    @Size(max = 500, message = "VALIDATION_ERROR")
    private String sellerAddress;

    private String sellerPhone;
    private String sellerEmail;
    private String sellerBankAccount;
    private String sellerBankName;

    // --- THÔNG TIN NGƯỜI MUA (BUYER/CUSTOMER) ---
    @NotBlank(message = "VALIDATION_ERROR")
    private String buyerName;

    @Pattern(regexp = "^(\\d{10,13})?$", message = "VALIDATION_ERROR")
    private String buyerTaxCode;

    private String buyerAddress;
    private String buyerPhone;
    private String buyerEmail;
    private String buyerBankAccount;
    private String buyerBankName;

    // --- THÔNG TIN THANH TOÁN & TỔNG HỢP ---
    @NotNull(message = "VALIDATION_ERROR")
    private PaymentMethod paymentMethod;

    private String paymentStatus;

    @NotNull(message = "VALIDATION_ERROR")
    @DecimalMin(value = "0.0", message = "VALIDATION_ERROR")
    private BigDecimal totalAmount; // Tổng tiền trước thuế

    @NotNull(message = "VALIDATION_ERROR")
    @DecimalMin(value = "0.0", message = "VALIDATION_ERROR")
    private BigDecimal totalTaxAmount;

    @Builder.Default
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @NotNull(message = "VALIDATION_ERROR")
    @DecimalMin(value = "0.0", message = "VALIDATION_ERROR")
    private BigDecimal grandTotalAmount; // Tổng tiền thanh toán cuối cùng

    // --- CHI TIẾT MẶT HÀNG ---
    @NotEmpty(message = "VALIDATION_ERROR")
    @Valid
    private List<InvoiceItemRequest> items;

    // --- THÔNG TIN THAM CHIẾU & PHỤ TRỢ ---
    private String invoiceNote;

    /**
     * Idempotency Key - Mã tham chiếu duy nhất từ phía Client.
     * Dùng để tránh phát hành trùng hóa đơn.
     */
    @NotBlank(message = "VALIDATION_ERROR")
    private String internalReferenceCode;

    private String customerCode;
    private String orderNumber;

    // --- THÔNG TIN GIAO HÀNG (DELIVERY) ---
    private String deliveryAddress;
    private String deliveryName;
    private String deliveryPhone;

    /**
     * Enum phương thức thanh toán chuẩn hóa.
     */
    public enum PaymentMethod {
        CASH, BANK_TRANSFER, CREDIT_CARD, E_WALLET, OTHER
    }
}