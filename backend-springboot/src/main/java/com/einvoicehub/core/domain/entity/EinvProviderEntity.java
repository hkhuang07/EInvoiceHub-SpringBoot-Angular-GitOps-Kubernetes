package com.einvoicehub.core.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/** nhà cung cấp HĐĐT như BKAV, VNPT, MISA, VIETTEL, MOBI ...*/

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "einv_provider")
public class EinvProviderEntity extends BaseAuditEntity {

    @Id
    @Column(name = "id", length = 36, nullable = false)
    private String id; //UUID

    @Column(name = "provider_code", length = 20, nullable = false, unique = true)
    private String providerCode;

    @Column(name = "provider_name", length = 100, nullable = false)
    private String providerName;

    @Column(name = "integration_url", length = 500)
    private String integrationUrl;

    @Column(name = "url_lookup", length = 500)
    private String urlLookup;

    @Column(name = "integration_type")
    private Integer integrationType; // 1: Ký 1 bước, 2: Ký 2 bước

    @Column(name = "is_inactive", nullable = false)
    private Boolean isInactive = false;
}