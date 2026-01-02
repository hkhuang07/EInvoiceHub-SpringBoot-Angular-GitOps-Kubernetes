package com.einvoicehub.core.entity.mongodb;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Document(collection = "invoice_payloads")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@CompoundIndex(name = "merchant_created_idx", def = "{'merchantId': 1, 'createdAt': -1}")
public class InvoicePayload {

    @Id
    private String id;

    @Indexed
    @Field("merchantId")
    private Long merchantId;

    @Field("clientRequestId")
    private String clientRequestId;

    @Field("data")
    private Map<String, Object> data;

    @Field("buyer")
    private BuyerInfo buyer;

    @Field("seller")
    private SellerInfo seller;

    @Field("items")
    private List<InvoiceItem> items;

    @Field("summary")
    private InvoiceSummary summary;

    @Field("metadata")
    private Map<String, Object> metadata;

    @Field("createdAt")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Field("updatedAt")
    private LocalDateTime updatedAt;

    // Nested classes for structured data
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BuyerInfo {
        private String name;
        private String taxCode;
        private String email;
        private String phone;
        private String address;
        private String bankAccount;
        private String bankName;
    }

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
        private BigDecimal discountPercent;
        private BigDecimal taxRate;
        private BigDecimal taxAmount;
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
        private BigDecimal exchangeRate;
        private String amountInWords;
    }
}