package com.einvoicehub.core.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "system_config")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class EinvSystemConfigEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "config_key", length = 100, nullable = false, unique = true)
    private String configKey;

    @Column(name = "config_value", columnDefinition = "TEXT", nullable = false)
    private String configValue;

    @Column(name = "config_type", length = 20, nullable = false)
    private String configType; // STRING, NUMBER, BOOLEAN, JSON

    // --- Enterprise Shell Fields ---
    @Column(name = "description", length = 500)
    private String description;

    @Builder.Default
    @Column(name = "is_encrypted", nullable = false)
    private Boolean isEncrypted = false; // Mã hóa dữ liệu nhạy cảm (VD: API Secret)

    @Builder.Default
    @Column(name = "is_editable", nullable = false)
    private Boolean isEditable = true;

    @Column(name = "updated_by")
    private Long updatedBy; // ID của MerchantUser thực hiện thay đổi (Decoupled)

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    // --------------------------------

    @PrePersist
    protected void onCreate() {
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}