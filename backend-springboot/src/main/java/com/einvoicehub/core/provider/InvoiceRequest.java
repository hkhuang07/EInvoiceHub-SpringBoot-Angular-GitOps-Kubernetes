package com.einvoicehub.core.provider;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**DTO đại diện cho yêu cầu phát hành hóa đơn*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceRequest {

    /** ID tham chiếu từ hệ thống EInvoiceHub*/
    private Long invoiceMetadataId;

    /**ID request từ client (idempotency)*/
    private String clientRequestId;

    /**Mã nhà cung cấp (VNPT, VIETTEL...)*/
    private String providerCode;

    /** Thông tin người bán*/
    private SellerInfo seller;

    /** Thông tin người mua*/
    private BuyerInfo buyer;

    /** Danh sách mặt hàng*/
    private List<InvoiceItem> items;

    /** Thông tin tổng kết*/
    private InvoiceSummary summary;

    /**Ngày lập hóa đơn*/
    private LocalDate issueDate;

    /** Kỳ thanh toán*/
    private PaymentTerm paymentTerm;

    /**Cấu hình bổ sung từ Provider*/
    private Map<String, Object> extraConfig;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
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

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BuyerInfo {
        private String name;
        private String taxCode;
        private String address;
        private String phone;
        private String email;
        private String bankAccount;
        private String bankName;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InvoiceItem {
        private String itemCode;
        private String itemName;
        private String unitName;
        private BigDecimal quantity;
        private BigDecimal unitPrice;
        private BigDecimal amount;
        private BigDecimal discountAmount;
        private BigDecimal taxRate;
        private String taxCategory;
        private String description;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InvoiceSummary {
        private BigDecimal subtotalAmount;
        private BigDecimal totalDiscountAmount;
        private BigDecimal totalTaxAmount;
        private BigDecimal totalAmount;
        private String currencyCode;
        private String amountInWords;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PaymentTerm {
        private String method;
        private LocalDate dueDate;
        private String description;
    }
}