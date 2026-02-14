package com.einvoicehub.core.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "invoice_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"invoice", "vatRate"})
public class EinvInvoiceItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id", nullable = false)
    private EinvInvoiceMetadataEntity invoice; // Liên kết hóa đơn cha

    @Column(name = "line_number", nullable = false)
    private Integer lineNumber; // Số thứ tự dòng

    // --- Product Information ---
    @Builder.Default
    @Column(name = "is_free")
    private Boolean isFree = false; // Hàng khuyến mãi

    @Column(name = "product_type_id")
    private Integer productTypeId;

    @Column(name = "product_code", length = 100)
    private String productCode;

    @Column(name = "product_name", length = 500, nullable = false)
    private String productName;

    @Column(name = "unit_name", length = 50)
    private String unitName;

    // --- Quantity & Price (Precision 18,4) ---
    @Builder.Default
    @Column(name = "quantity", precision = 18, scale = 4, nullable = false)
    private BigDecimal quantity = BigDecimal.ZERO;

    @Builder.Default
    @Column(name = "unit_price", precision = 18, scale = 4, nullable = false)
    private BigDecimal unitPrice = BigDecimal.ZERO;

    // --- Calculations (Precision 18,2) ---
    @Builder.Default
    @Column(name = "gross_amount", precision = 18, scale = 2)
    private BigDecimal grossAmount = BigDecimal.ZERO; // quantity * unit_price

    @Builder.Default
    @Column(name = "discount_rate", precision = 5, scale = 2)
    private BigDecimal discountRate = BigDecimal.ZERO;

    @Builder.Default
    @Column(name = "discount_amount", precision = 18, scale = 2)
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @Builder.Default
    @Column(name = "net_price", precision = 18, scale = 4)
    private BigDecimal netPrice = BigDecimal.ZERO; // Giá sau chiết khấu

    @Builder.Default
    @Column(name = "net_amount", precision = 18, scale = 2)
    private BigDecimal netAmount = BigDecimal.ZERO; // Thành tiền sau chiết khấu

    // --- Tax Information ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vat_rate_id")
    private EinvVatRateEntity vatRate; // FK tới danh mục thuế

    @Builder.Default
    @Column(name = "tax_rate", precision = 5, scale = 2)
    private BigDecimal taxRate = BigDecimal.ZERO; // Snapshot tỷ lệ thuế (%)

    @Builder.Default
    @Column(name = "tax_amount", precision = 18, scale = 2)
    private BigDecimal taxAmount = BigDecimal.ZERO;

    @Builder.Default
    @Column(name = "total_amount", precision = 18, scale = 2, nullable = false)
    private BigDecimal totalAmount = BigDecimal.ZERO; // Net + Tax

    // --- Shell Fields ---
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "notes", length = 500)
    private String notes;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}