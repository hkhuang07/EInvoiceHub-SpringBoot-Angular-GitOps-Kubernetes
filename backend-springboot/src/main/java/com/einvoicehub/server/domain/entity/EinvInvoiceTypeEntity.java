package com.einvoicehub.server.domain.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "einv_invoice_type")
public class EinvInvoiceTypeEntity extends BaseEntity {

    @Id
    @Column(name = "id", nullable = false, columnDefinition = "TINYINT")
    private Byte id;

    @Column(name = "name", length = 255, nullable = false, unique = true)
    private String name;

    @Column(name = "sort_order", columnDefinition = "INT DEFAULT 0")
    private Integer sortOrder = 0;

    @Column(name = "note", length = 255)
    private String note;

    public Byte getId() {
        return id;
    }

    public void setId(Byte id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
