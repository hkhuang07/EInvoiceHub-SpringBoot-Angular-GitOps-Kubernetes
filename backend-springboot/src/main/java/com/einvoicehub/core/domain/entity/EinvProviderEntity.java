package com.einvoicehub.core.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.time.LocalDateTime;

@Entity
@Table(name = "einv_provider")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class EinvProviderEntity {

    @Id
    @Column(name = "id", length = 36)
    private String id;
    @Column(name = "provider_code", length = 20, nullable = false, unique = true)
    private String providerCode; // BKAV, VNPT, MOBI...

    @Column(name = "provider_name", length = 100, nullable = false)
    private String providerName;

    @Column(name = "integration_url", length = 500)
    private String integrationUrl;

    @Column(name = "url_lookup", length = 500)
    private String urlLookup;

    @Column(name = "is_inactive", nullable = false)
    private Boolean isInactive;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;


    @PrePersist
    protected void onCreate() {
        if (this.isInactive == null) this.isInactive = false;
        this.createdAt = LocalDateTime.now();
    }

    @PrePersist
    protected void onUpdate() {
        if (this.isInactive == null) this.isInactive = false;
        this.updatedAt = LocalDateTime.now();
    }
}