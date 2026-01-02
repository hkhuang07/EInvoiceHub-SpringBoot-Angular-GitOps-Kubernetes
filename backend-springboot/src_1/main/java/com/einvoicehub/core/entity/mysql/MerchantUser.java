package com.einvoicehub.core.entity.mysql;

import com.einvoicehub.core.entity.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "merchant_users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MerchantUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchant_id", nullable = false)
    private Merchant merchant;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Column(name = "full_name", length = 100)
    private String fullName;

    @Column(length = 20)
    private String phone;

    @Column(name = "avatar_url", length = 500)
    private String avatarUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private UserRole role = UserRole.STAFF;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    @Column(name = "is_primary", nullable = false)
    @Builder.Default
    private Boolean isPrimary = false;

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    @Column(name = "failed_login_attempts", nullable = false)
    @Builder.Default
    private Integer failedLoginAttempts = 0;

    @Column(name = "locked_until")
    private LocalDateTime lockedUntil;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Business methods
    public boolean isLocked() {
        return lockedUntil != null && lockedUntil.isAfter(LocalDateTime.now());
    }

    public void incrementFailedAttempts() {
        this.failedLoginAttempts++;
        // Lock account after 5 failed attempts for 30 minutes
        if (this.failedLoginAttempts >= 5) {
            this.lockedUntil = LocalDateTime.now().plusMinutes(30);
        }
    }

    public void resetFailedAttempts() {
        this.failedLoginAttempts = 0;
        this.lockedUntil = null;
    }

    public void updateLastLogin() {
        this.lastLoginAt = LocalDateTime.now();
        this.failedLoginAttempts = 0;
    }

    public boolean canManageUsers() {
        return role.canManageUsers();
    }

    public boolean canCreateInvoice() {
        return role.canCreateInvoice();
    }

    public boolean canViewReports() {
        return role.canViewReports();
    }

    public String getDisplayName() {
        return fullName != null && !fullName.isEmpty() ? fullName : username;
    }
}