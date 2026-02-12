package com.einvoicehub.core.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "invoice_payloads")
@EntityListeners(AuditingEntityListener.class)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoicePayload {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "merchant_id")
    private Long merchantId;

    @Column(name = "client_request_id", unique = true)
    private String clientRequestId;

    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "provider_code")
    private String providerCode;

    @Column(name = "invoice_id", unique = true)
    private Long invoiceId;

    @Column(name = "raw_data", columnDefinition = "JSON")
    private String rawData;

    @Column(name = "response_raw", columnDefinition = "LONGTEXT")
    private String responseRaw;

    @Column(name = "xml_content", columnDefinition = "LONGTEXT")
    private String xmlContent;

    @Column(name = "json_content", columnDefinition = "LONGTEXT")
    private String jsonContent;

    @Column(name = "signed_xml", columnDefinition = "LONGTEXT")
    private String signedXml;

    @Column(name = "pdf_data", columnDefinition = "LONGTEXT")
    private String pdfData;

    @Column(name = "status")
    private String status;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "extra_data", columnDefinition = "JSON")
    private String extraData;

// Nested classes for buyer, seller, items, summary
// Lưu trữ dưới dạng JSON trong các cột riêng biệt hoặc trong extra_data

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Embeddable
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
    @Embeddable
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
    @Embeddable
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
    @Embeddable
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