package com.einvoicehub.core.domain.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "einv_mapping_invoice_type")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString(exclude = "provider")
public class EinvMappingTaxTypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id", nullable = false)
    private EinvProviderEntity provider;

    @Column(name = "system_code", length = 50, nullable = false)
    private String systemCode;

    @Column(name = "provider_code", length = 100, nullable = false)
    private String providerCode;

    @Column(name = "description", length = 255)
    private String description;
}