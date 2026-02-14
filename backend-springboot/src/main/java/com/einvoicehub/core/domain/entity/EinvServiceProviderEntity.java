package com.einvoicehub.core.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "service_providers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class EinvServiceProviderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "provider_code", length = 20, nullable = false, unique = true)
    private String providerCode; // Ví dụ: VNPT, VIETTEL

    @Column(name = "provider_name", length = 100, nullable = false)
    private String providerName;

    @Column(name = "official_api_url", length = 500, nullable = false)
    private String officialApiUrl;

    // --- Enterprise Shell Fields ---
    @Column(name = "lookup_url", length = 500)
    private String lookupUrl; // URL tra cứu hóa đơn

    @Column(name = "documentation_url", length = 500)
    private String documentationUrl;

    @Column(name = "support_email", length = 255)
    private String supportEmail;

    @Column(name = "support_phone", length = 20)
    private String supportPhone;

    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Builder.Default
    @Column(name = "is_default", nullable = false)
    private Boolean isDefault = false;

    @Builder.Default
    @Column(name = "display_order", nullable = false)
    private Integer displayOrder = 0;

    @Column(name = "extra_config_schema", columnDefinition = "JSON")
    private String extraConfigSchema; // Cấu hình đặc thù dạng JSON

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    // --------------------------------

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}