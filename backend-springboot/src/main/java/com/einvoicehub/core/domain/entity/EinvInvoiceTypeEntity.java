package com.einvoicehub.core.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "einv_invoice_type")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class EinvInvoiceTypeEntity {

    @Id
    private Integer id;

    @Column(name = "invoice_type_name", length = 100, nullable = false)
    private String invoiceTypeName;

    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder;

    @Column(name = "note", length = 500)
    private String note;
}