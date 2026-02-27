package com.einvoicehub.core.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "einv_unit")
public class EinvUnitEntity extends BaseAuditEntity {
    @Id
    @Column(name = "code", length = 50)
    private String code; // DVT01, DVT02...
    @Column(name = "name", length = 100, nullable = false)
    private String name;
}
