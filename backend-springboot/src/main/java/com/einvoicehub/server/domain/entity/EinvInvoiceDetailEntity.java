package com.einvoicehub.server.domain.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;

@Entity
@Table(name = "einv_invoices_detail",
        indexes = {
                @Index(name = "idx_inv_det_doc_line", columnList = "doc_id, line_no"),
                @Index(name = "idx_inv_det_tenant", columnList = "tenant_id"),
                @Index(name = "idx_inv_det_item_type_fk", columnList = "item_type_id"),
                @Index(name = "idx_inv_det_tax_type_fk", columnList = "tax_type_id")
        })
public class EinvInvoiceDetailEntity extends BaseEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", length = 36, nullable = false)
    protected String id;

    @Column(name = "tenant_id", length = 36)
    private String tenantId;

    @Column(name = "store_id", length = 36)
    private String storeId;

    @Column(name = "doc_id", length = 36)
    private String docId;

    @Column(name = "line_no")
    private Integer lineNo;

    @Column(name = "is_free", columnDefinition = "TINYINT(1)")
    private Boolean isFree;

    // TINYINT in schema
    @Column(name = "item_type_id", columnDefinition = "TINYINT")
    private Byte itemTypeId;

    @Column(name = "item_id", length = 36)
    private String itemId;

    @Column(name = "item_name", length = 500)
    private String itemName;

    @Column(name = "unit", length = 50)
    private String unit;

    @Column(name = "quantity", precision = 15, scale = 2)
    private BigDecimal quantity;

    @Column(name = "price", precision = 15, scale = 2)
    private BigDecimal price;

    @Column(name = "gross_amount", precision = 15, scale = 2)
    private BigDecimal grossAmount;

    @Column(name = "discount_rate", precision = 15, scale = 2)
    private BigDecimal discountRate;

    @Column(name = "discount_amount", precision = 15, scale = 2)
    private BigDecimal discountAmount;

    @Column(name = "net_price_vat", precision = 15, scale = 2)
    private BigDecimal netPriceVat;

    @Column(name = "net_price", precision = 15, scale = 2)
    private BigDecimal netPrice;

    @Column(name = "net_amount", precision = 15, scale = 2)
    private BigDecimal netAmount;

    @Column(name = "tax_type_id", length = 36)
    private String taxTypeId;

    @Column(name = "tax_rate", precision = 15, scale = 2)
    private BigDecimal taxRate;

    @Column(name = "tax_amount", precision = 15, scale = 2)
    private BigDecimal taxAmount;

    @Column(name = "total_amount", precision = 15, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "notes", length = 500)
    private String notes;

    // TINYINT in schema: Loại HĐ Điều chỉnh: 1: Thông tin, 2: Tăng, 3: Giảm
    @Column(name = "adjustment_type", columnDefinition = "TINYINT DEFAULT 0")
    private Byte adjustmentType = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doc_id", insertable = false, updatable = false)
    private EinvInvoiceEntity invoice;

    // Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public Integer getLineNo() {
        return lineNo;
    }

    public void setLineNo(Integer lineNo) {
        this.lineNo = lineNo;
    }

    public Boolean getIsFree() {
        return isFree;
    }

    public void setIsFree(Boolean isFree) {
        this.isFree = isFree;
    }

    public Byte getItemTypeId() {
        return itemTypeId;
    }

    public void setItemTypeId(Byte itemTypeId) {
        this.itemTypeId = itemTypeId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getGrossAmount() {
        return grossAmount;
    }

    public void setGrossAmount(BigDecimal grossAmount) {
        this.grossAmount = grossAmount;
    }

    public BigDecimal getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(BigDecimal discountRate) {
        this.discountRate = discountRate;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getNetPriceVat() {
        return netPriceVat;
    }

    public void setNetPriceVat(BigDecimal netPriceVat) {
        this.netPriceVat = netPriceVat;
    }

    public BigDecimal getNetPrice() {
        return netPrice;
    }

    public void setNetPrice(BigDecimal netPrice) {
        this.netPrice = netPrice;
    }

    public BigDecimal getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(BigDecimal netAmount) {
        this.netAmount = netAmount;
    }

    public String getTaxTypeId() {
        return taxTypeId;
    }

    public void setTaxTypeId(String taxTypeId) {
        this.taxTypeId = taxTypeId;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Byte getAdjustmentType() {
        return adjustmentType;
    }

    public void setAdjustmentType(Byte adjustmentType) {
        this.adjustmentType = adjustmentType;
    }

    public EinvInvoiceEntity getInvoice() {
        return invoice;
    }

    public void setInvoice(EinvInvoiceEntity invoice) {
        this.invoice = invoice;
    }
}
