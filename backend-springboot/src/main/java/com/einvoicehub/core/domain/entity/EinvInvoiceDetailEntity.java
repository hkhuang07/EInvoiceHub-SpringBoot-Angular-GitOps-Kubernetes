package com.einvoicehub.core.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.math.BigDecimal;

@Entity
@Table(name = "einv_invoices_detail")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString(exclude = "invoice")
public class EinvInvoiceDetailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doc_id", nullable = false)
    private EinvInvoiceEntity invoice; // FK -> einv_invoices.id

    @Column(name = "line_no")
    private Integer lineNo;

    @Column(name = "item_id", length = 36)
    private String itemId; // Mã hàng từ POS

    @Column(name = "item_name", length = 500, nullable = false)
    private String itemName;

    @Column(name = "item_type_id")
    private Integer itemTypeId; // Mapping sang loại item của NCC

    @Column(name = "is_free")
    private Boolean isFree;

    @Column(name = "unit", length = 50)
    private String unit;

    @Column(name = "quantity", precision = 15, scale = 6)
    private BigDecimal quantity;

    @Column(name = "price", precision = 15, scale = 6)
    private BigDecimal price; // Đơn giá chưa CK

    @Column(name = "gross_amount", precision = 15, scale = 2)
    private BigDecimal grossAmount; // quantity * price

    @Column(name = "discount_rate", precision = 15, scale = 2)
    private BigDecimal discountRate;

    @Column(name = "discount_amount", precision = 15, scale = 2)
    private BigDecimal discountAmount;

    @Column(name = "net_amount", precision = 15, scale = 2)
    private BigDecimal netAmount; // gross - discount

    @Column(name = "net_price", precision = 15, scale = 6)
    private BigDecimal netPrice; // net_amount / quantity

    @Column(name = "tax_type_id", length = 36)
    private String taxTypeId; // Mã loại thuế HUB (Mapping sang NCC)

    @Column(name = "tax_rate", precision = 15, scale = 2)
    private BigDecimal taxRate;

    @Column(name = "tax_amount", precision = 15, scale = 2)
    private BigDecimal taxAmount;

    @Column(name = "net_price_vat", precision = 15, scale = 6)
    private BigDecimal netPriceVat; // Đơn giá sau thuế

    @Column(name = "total_amount", precision = 15, scale = 2)
    private BigDecimal totalAmount; // net_amount + tax_amount

    @Column(name = "notes", length = 500)
    private String notes;
}