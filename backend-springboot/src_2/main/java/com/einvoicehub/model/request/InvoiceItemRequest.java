package com.einvoicehub.model.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;

/**
 * Invoice Item Request DTO
 * 
 * Model cho từng mặt hàng trong hóa đơn
 */
public class InvoiceItemRequest {

    @NotBlank(message = "Item name is required")
    private String itemName;

    private String itemCode;
    private String unitName;
    private String unitCode;

    @DecimalMin(value = "0.0", message = "Quantity must be non-negative")
    private BigDecimal quantity = BigDecimal.ONE;

    @DecimalMin(value = "0.0", message = "Unit price must be non-negative")
    private BigDecimal unitPrice;

    private BigDecimal amount;
    
    private BigDecimal discountAmount;
    private BigDecimal discountPercent;
    
    @DecimalMin(value = "0.0", message = "Tax rate must be non-negative")
    private BigDecimal taxRate = BigDecimal.ZERO;

    private String taxType; // 0: No tax, 1: VAT included, 2: VAT excluded
    private String taxCategory;

    private BigDecimal totalAmount;
    private BigDecimal totalTaxAmount;

    private String description;
    private String sequence;

    // Getters and Setters
    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(BigDecimal discountPercent) {
        this.discountPercent = discountPercent;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    public String getTaxType() {
        return taxType;
    }

    public void setTaxType(String taxType) {
        this.taxType = taxType;
    }

    public String getTaxCategory() {
        return taxCategory;
    }

    public void setTaxCategory(String taxCategory) {
        this.taxCategory = taxCategory;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getTotalTaxAmount() {
        return totalTaxAmount;
    }

    public void setTotalTaxAmount(BigDecimal totalTaxAmount) {
        this.totalTaxAmount = totalTaxAmount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    /**
     * Calculate amount = quantity * unit price
     */
    public void calculateAmount() {
        if (quantity != null && unitPrice != null) {
            this.amount = quantity.multiply(unitPrice);
        }
    }

    /**
     * Calculate total amount after discount
     */
    public void calculateTotalAmount() {
        calculateAmount();
        if (amount != null && discountAmount != null) {
            this.totalAmount = amount.subtract(discountAmount);
        } else {
            this.totalAmount = amount;
        }
    }

    /**
     * Calculate tax amount
     */
    public void calculateTaxAmount() {
        if (totalAmount != null && taxRate != null) {
            this.totalTaxAmount = totalAmount.multiply(taxRate)
                .divide(BigDecimal.valueOf(100), 0, java.math.RoundingMode.HALF_UP);
        }
    }
}