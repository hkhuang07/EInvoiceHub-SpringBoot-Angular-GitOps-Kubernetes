package com.einvoicehub.core.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "einv_tax_type")
public class EinvTaxTypeEntity extends BaseAuditEntity {
    @Id
    @Column(name = "code", length = 50)
    private String code;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "rate", precision = 5, scale = 2)
    private BigDecimal rate;
}