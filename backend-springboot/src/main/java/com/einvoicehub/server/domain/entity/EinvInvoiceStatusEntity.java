package com.einvoicehub.server.domain.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "einv_invoice_status")
public class EinvInvoiceStatusEntity extends BaseEntity {

    @Id
    @Column(name = "id", nullable = false, columnDefinition = "TINYINT")
    private Byte id;

    @Column(name = "name", length = 255, nullable = false, unique = true)
    private String name;

    @Column(name = "description", length = 255, nullable = false)
    private String description;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
