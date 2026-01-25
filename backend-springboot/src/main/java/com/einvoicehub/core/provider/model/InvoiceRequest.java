package com.einvoicehub.core.provider.model;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * DTO đại diện cho yêu cầu phát hành hóa đơn nội bộ.
 * Đã tối ưu cho việc mapping sang mọi Provider (VNPT, MISA, BKAV, Viettel).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceRequest {

    private Long invoiceMetadataId;
    private String clientRequestId;
    private String providerCode;
    private String invoiceType; // VD: 01GTKT, 02GTTT

    /** Cần thiết cho hầu hết Provider Việt Nam */
    private String templateCode; // Mẫu số (VD: 1/001)
    private String symbolCode;   // Ký hiệu (VD: C23TBA)

    private SellerInfo seller;
    private BuyerInfo buyer;
    private List<InvoiceItem> items;
    private InvoiceSummary summary;

    private LocalDate issueDate;
    private PaymentTerm paymentTerm;

    /** Chứa các tham số đặc thù không nằm trong chuẩn chung */
    private Map<String, Object> extraConfig;

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class SellerInfo {
        private String name;
        private String taxCode;
        private String address;
        private String phone;
        private String email;
        private String bankAccount;
        private String bankName;
        private String representativeName;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class BuyerInfo {
        private String name;
        private String taxCode;
        private String address;
        private String phone;
        private String email;
        private String bankAccount;
        private String bankName;
        private String customerCode; // Mã khách hàng nội bộ
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class InvoiceItem {
        private String itemCode;
        private String itemName;
        private String unitName;
        private BigDecimal quantity;
        private BigDecimal unitPrice;
        private BigDecimal amount; // Thành tiền trước thuế
        private BigDecimal discountAmount;
        private BigDecimal discountPercent; // Thêm để hỗ trợ tính toán chiết khấu %
        private BigDecimal taxRate; // Thuế suất (0, 5, 8, 10...)
        private BigDecimal taxAmount;
        private String taxCategory; // Loại thuế (theo quy định của từng hãng)
        private String description;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class InvoiceSummary {
        private BigDecimal subtotalAmount;
        private BigDecimal totalDiscountAmount;
        private BigDecimal totalTaxAmount;
        private BigDecimal totalAmount; // Tổng tiền thanh toán cuối cùng
        private String currencyCode;
        private BigDecimal exchangeRate; // Thêm để hỗ trợ hóa đơn ngoại tệ
        private String amountInWords;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class PaymentTerm {
        private String method; // TM, CK, TM/CK
        private LocalDate dueDate;
        private String description;
    }
}