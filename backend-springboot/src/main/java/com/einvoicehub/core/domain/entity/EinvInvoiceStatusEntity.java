package com.einvoicehub.core.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "einv_invoice_status")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EinvInvoiceStatusEntity {

    @Id
    private Integer id;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "note", length = 500)
    private String note;
}