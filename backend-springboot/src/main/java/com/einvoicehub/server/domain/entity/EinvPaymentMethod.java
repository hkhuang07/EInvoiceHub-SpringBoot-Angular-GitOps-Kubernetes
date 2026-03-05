package com.einvoicehub.server.domain.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "einv_payment_method")
public class EinvPaymentMethod extends BaseEntity {

    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", length = 255, nullable = false, unique = true)
    private String name;

    @Column(name = "note", length = 255)
    private String note;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
