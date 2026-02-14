package com.einvoicehub.core.domain.entity;

import com.einvoicehub.core.domain.entity.enums.AuditAction;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"merchant", "user"})
public class EinvAuditLogsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchant_id")
    private EinvMerchantEntity merchant; // Doanh nghiệp liên quan

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private EinvMerchantUserEntity user; // Người thực hiện

    @Column(name = "entity_type", length = 50, nullable = false)
    private String entityType; // Loại đối tượng (Invoice, Config...)

    @Column(name = "entity_id", nullable = false)
    private Long entityId;

    @Enumerated(EnumType.STRING)
    @Column(name = "action", length = 50, nullable = false)
    private AuditAction action;

    // --- Core Management Fields (Dữ liệu phục vụ đối soát) ---
    @Column(name = "old_value", columnDefinition = "JSON")
    private String oldValue;

    @Column(name = "new_value", columnDefinition = "JSON")
    private String newValue;

    // --- Enterprise Shell Fields (Lưu vết hạ tầng) ---
    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(name = "user_agent", length = 500)
    private String userAgent;

    @Column(name = "request_id", length = 100)
    private String requestId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    // --------------------------------

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}