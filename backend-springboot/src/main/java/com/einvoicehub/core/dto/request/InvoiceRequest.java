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
/*

File mới được minimax viết
package com.einvoicehub.core.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO cho yêu cầu tạo hóa đơn điện tử.
 * Tích hợp validation annotations để kiểm tra dữ liệu đầu vào.
 *
 * Validation messages sử dụng ErrorCode keys để GlobalExceptionHandler
 * có thể chuyển đổi thành thông báo lỗi chuẩn hóa.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceRequest {

    /**
     * Mã merchant - bắt buộc.
     */
    @NotBlank(message = "MERCHANT_NOT_FOUND")
    private String merchantId;

    /**
     * Mã provider (MISA, BKAV, VIETTEL) - bắt buộc.
     */
    @NotBlank(message = "VALIDATION_ERROR")
    @Pattern(regexp = "^(MISA|BKAV|VIETTEL)$",
            message = "VALIDATION_ERROR",
            flags = Flag.CASE_INSENSITIVE)
    private String providerCode;

    /**
     * Mã số thuế người bán - bắt buộc, định dạng 10-13 số.
     */
    @NotBlank(message = "VALIDATION_ERROR")
    @Pattern(regexp = "^\\d{10,13}$", message = "VALIDATION_ERROR")
    private String sellerTaxCode;

    /**
     * Tên người bán - bắt buộc.
     */
    @NotBlank(message = "VALIDATION_ERROR")
    @Size(max = 255, message = "VALIDATION_ERROR")
    private String sellerName;

    /**
     * Địa chỉ người bán - tối đa 500 ký tự.
     */
    @Size(max = 500, message = "VALIDATION_ERROR")
    private String sellerAddress;

    /**
     * Danh sách sản phẩm/dịch vụ - bắt buộc, ít nhất 1 item.
     */
    @NotEmpty(message = "VALIDATION_ERROR")
    @Valid
    private List<InvoiceItemRequest> items;

    /**
     * Thông tin khách hàng - tùy chọn.
     */
    @Valid
    private CustomerInfo customerInfo;

    /**
     * Phương thức thanh toán - bắt buộc.
     */
    @NotNull(message = "VALIDATION_ERROR")
    private PaymentMethod paymentMethod;

    /**
     * Tổng tiền trước thuế - bắt buộc, >= 0.
     */
    @NotNull(message = "VALIDATION_ERROR")
    @DecimalMin(value = "0.0", inclusive = true, message = "VALIDATION_ERROR")
    private BigDecimal subtotal;

    /**
     * Tổng thuế GTGT - bắt buộc, >= 0.
     */
    @NotNull(message = "VALIDATION_ERROR")
    @DecimalMin(value = "0.0", inclusive = true, message = "VALIDATION_ERROR")
    private BigDecimal vatAmount;

    /**
     * Tổng tiền thanh toán - bắt buộc, >= 0.
     */
    @NotNull(message = "VALIDATION_ERROR")
    @DecimalMin(value = "0.0", inclusive = true, message = "VALIDATION_ERROR")
    private BigDecimal totalAmount;

    /**
     * Tiền tệ - mặc định VND.
     */
    @Builder.Default
    private String currency = "VND";

    /**
     * Ghi chú - tối đa 1000 ký tự.
     */
    @Size(max = 1000, message = "VALIDATION_ERROR")
    private String note;

    /**
     * Ngày hóa đơn - bắt buộc.
     */
    @NotNull(message = "VALIDATION_ERROR")
    private java.time.LocalDate invoiceDate;

    /**
     * Kỳ hóa đơn - tùy chọn.
     */
    private String invoicePeriod;

    /**
     * Enum phương thức thanh toán.
     */
    public enum PaymentMethod {
        CASH,           // Tiền mặt
        BANK_TRANSFER,  // Chuyển khoản
        CREDIT_CARD,    // Thẻ tín dụng
        DEBIT_CARD,     // Thẻ ghi nợ
        E_WALLET,       // Ví điện tử
        OTHER           // Khác
    }

    /**
     * Nested class cho thông tin khách hàng.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CustomerInfo {

        @Size(max = 100, message = "VALIDATION_ERROR")
        private String customerName;

        @Pattern(regexp = "^\\d{10,13}$|^$", message = "VALIDATION_ERROR")
        private String customerTaxCode;

        @Email(message = "VALIDATION_ERROR")
        @Size(max = 255, message = "VALIDATION_ERROR")
        private String customerEmail;

        @Size(max = 500, message = "VALIDATION_ERROR")
        private String customerAddress;

        @Pattern(regexp = "^\\d{10,11}$|^$", message = "VALIDATION_ERROR")
        private String customerPhone;
    }
} */