package com.einvoicehub.server.domain.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "einv_receive_type")
public class EinvReceiveType extends BaseEntity {

    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", length = 255, nullable = false)
    private String name;

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
}
