package com.einvoicehub.core.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "registration_statuses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class EinvRegistrationStatusEntity {

    @Id
    private Integer id;

    @Column(name = "name", length = 100, nullable = false, unique = true)
    private String name;

    // --- Enterprise Shell Fields ---
    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "note", length = 500)
    private String note; // Ghi chú nghiệp vụ mở rộng

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
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