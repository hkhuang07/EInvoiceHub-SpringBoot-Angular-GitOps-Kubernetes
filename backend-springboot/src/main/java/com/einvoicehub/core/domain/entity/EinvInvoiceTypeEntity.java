package com.einvoicehub.core.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/** danh mục loại hình hóa đơn điện tử */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "einv_invoice_type")
public class EinvInvoiceTypeEntity extends BaseAuditEntity {

    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "invoice_type_name", length = 100, nullable = false)
    private String invoiceTypeName;

    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 0;

    @Column(name = "note", length = 500)
    private String note;
}