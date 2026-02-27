package com.einvoicehub.core.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.math.BigDecimal;

/** Chi tiết Hóa đơn */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "einv_invoices_detail",
        indexes = { @Index(name = "idx_detail_doc", columnList = "doc_id") })
public class EinvInvoiceDetailEntity extends TenantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doc_id", nullable = false, foreignKey = @ForeignKey(name = "fk_detail_invoice"))
    private EinvInvoiceEntity invoice;

    @Column(name = "line_no")
    private Integer lineNo;

    @Column(name = "item_id", length = 36)
    private String itemId;

    @Column(name = "item_code", length = 50)
    private String itemCode;

    @Column(name = "item_name", length = 500, nullable = false)
    private String itemName;

    @Column(name = "item_type_id")
    private Integer itemTypeId;

    @Column(name = "is_free")
    private Boolean isFree = false;

    @Column(name = "unit_name", length = 50)
    private String unitName;

    @Column(name = "quantity", precision = 15, scale = 6)
    private BigDecimal quantity;

    @Column(name = "price", precision = 15, scale = 6)
    private BigDecimal Price;

    @Column(name = "gross_amount", precision = 20, scale = 2)
    private BigDecimal grossAmount;

    @Column(name = "discount_rate", precision = 20, scale = 2)
    private BigDecimal discountRate;

    @Column(name = "discount_amount", precision = 20, scale = 2)
    private BigDecimal discountAmount;

    @Column(name = "net_amount", precision = 20, scale = 2)
    private BigDecimal netAmount;

    @Column(name = "net_price", precision = 20, scale = 6)
    private BigDecimal netPrice;

    @Column(name = "tax_type_id", length = 36)
    private String taxTypeId;

    @Column(name = "tax_rate", precision = 15, scale = 2)
    private BigDecimal taxRate;

    @Column(name = "tax_amount", precision = 20, scale = 2)
    private BigDecimal taxAmount;

    @Column(name = "net_price_vat", precision = 20, scale = 6)
    private BigDecimal netPriceVat;

    @Column(name = "total_amount", precision = 20, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "adjustment_type")
    private Integer adjustmentType = 0; // 1:Tăng, 2:Giảm

    @Column(name = "notes", length = 500)
    private String notes;
}