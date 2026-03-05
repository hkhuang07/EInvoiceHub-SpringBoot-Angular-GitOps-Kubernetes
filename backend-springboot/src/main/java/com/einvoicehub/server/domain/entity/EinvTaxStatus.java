package com.einvoicehub.server.domain.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "einv_tax_status")
public class EinvTaxStatus extends BaseEntity {

    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    public Integer getTaxStatusId() {
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
}
