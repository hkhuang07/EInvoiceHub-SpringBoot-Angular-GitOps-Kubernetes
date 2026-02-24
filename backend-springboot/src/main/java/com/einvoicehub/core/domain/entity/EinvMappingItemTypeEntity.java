package com.einvoicehub.core.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "einv_mapping_item_type")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString(exclude = "provider")
public class EinvMappingItemTypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id", nullable = false)
    private EinvProviderEntity provider;

    @Column(name = "system_code", length = 50, nullable = false)
    private String systemCode; //Mã loại item

    @Column(name = "provider_code", length = 100, nullable = false)
    private String providerCode; //Mã NCC

    @Column(name = "description", length = 255)
    private String description;
}