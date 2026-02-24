package com.einvoicehub.core.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "einv_payment_method")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EinvPaymentMethodEntity {

    @Id
    private Integer id;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "note", length = 500)
    private String note;
}