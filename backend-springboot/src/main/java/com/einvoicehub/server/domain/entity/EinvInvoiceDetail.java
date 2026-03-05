package com.einvoicehub.server.domain.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;

@Entity
@Table(name = "einv_invoices_detail")
public class EinvInvoiceDetail extends BaseEntity {

    /*@Column(name = "id", length = 36, nullable = false)
    private String id;*/
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

    //1: Hàng tặng, đơn giá = 0
    @Column(name = "is_free")
    private Boolean isFree;

    @Column(name = "item_type_id")
    private Integer itemTypeId;

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

    //Loại HĐ Điều chỉnh: 1: Thông tin, 2: Tăng, 3: Giảm
    @Column(name = "adjustment_type")
    private Integer adjustmentType = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doc_id", insertable = false, updatable = false)
    private EinvInvoice invoice;

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

    public Integer getItemTypeId() {
        return itemTypeId;
    }

    public void setItemTypeId(Integer itemTypeId) {
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

    public Integer getAdjustmentType() {
        return adjustmentType;
    }

    public void setAdjustmentType(Integer adjustmentType) {
        this.adjustmentType = adjustmentType;
    }

    public EinvInvoice getInvoice() {
        return invoice;
    }

    public void setInvoice(EinvInvoice invoice) {
        this.invoice = invoice;
    }
}
