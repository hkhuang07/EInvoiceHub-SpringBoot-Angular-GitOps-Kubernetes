package com.einvoicehub.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Merchant Entity - Doanh nghiệp sử dụng dịch vụ EInvoiceHub
 * 
 * Lưu trữ thông tin doanh nghiệp đăng ký sử dụng hệ thống
 */
@Entity
@Table(name = "merchants")
public class Merchant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tax_code", nullable = false, unique = true, length = 20)
    private String taxCode;

    @Column(name = "company_name", nullable = false, length = 255)
    private String companyName;

    @Column(name = "company_address", length = 500)
    private String companyAddress;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "website", length = 100)
    private String website;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private EntityStatus status = EntityStatus.ACTIVE;

    @Column(name = "subscription_plan")
    private String subscriptionPlan = "BASIC";

    @Column(name = "quota_limit")
    private Integer quotaLimit = 1000;

    @Column(name = "current_quota_usage")
    private Integer currentQuotaUsage = 0;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "last_activity_at")
    private LocalDateTime lastActivityAt;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    // Relationships
    @OneToMany(mappedBy = "merchant", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MerchantUser> users = new HashSet<>();

    @OneToMany(mappedBy = "merchant", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ApiCredential> apiCredentials = new HashSet<>();

    @OneToMany(mappedBy = "merchant", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MerchantProviderConfig> providerConfigs = new HashSet<>();

    // Lifecycle callbacks
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public EntityStatus getStatus() {
        return status;
    }

    public void setStatus(EntityStatus status) {
        this.status = status;
    }

    public String getSubscriptionPlan() {
        return subscriptionPlan;
    }

    public void setSubscriptionPlan(String subscriptionPlan) {
        this.subscriptionPlan = subscriptionPlan;
    }

    public Integer getQuotaLimit() {
        return quotaLimit;
    }

    public void setQuotaLimit(Integer quotaLimit) {
        this.quotaLimit = quotaLimit;
    }

    public Integer getCurrentQuotaUsage() {
        return currentQuotaUsage;
    }

    public void setCurrentQuotaUsage(Integer currentQuotaUsage) {
        this.currentQuotaUsage = currentQuotaUsage;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getLastActivityAt() {
        return lastActivityAt;
    }

    public void setLastActivityAt(LocalDateTime lastActivityAt) {
        this.lastActivityAt = lastActivityAt;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Set<MerchantUser> getUsers() {
        return users;
    }

    public void setUsers(Set<MerchantUser> users) {
        this.users = users;
    }

    public Set<ApiCredential> getApiCredentials() {
        return apiCredentials;
    }

    public void setApiCredentials(Set<ApiCredential> apiCredentials) {
        this.apiCredentials = apiCredentials;
    }

    public Set<MerchantProviderConfig> getProviderConfigs() {
        return providerConfigs;
    }

    public void setProviderConfigs(Set<MerchantProviderConfig> providerConfigs) {
        this.providerConfigs = providerConfigs;
    }

    // Business methods
    public boolean isActive() {
        return status == EntityStatus.ACTIVE;
    }

    public boolean hasAvailableQuota() {
        return currentQuotaUsage < quotaLimit;
    }

    public void incrementQuotaUsage() {
        if (hasAvailableQuota()) {
            this.currentQuotaUsage++;
        }
    }

    public void decrementQuotaUsage() {
        if (this.currentQuotaUsage > 0) {
            this.currentQuotaUsage--;
        }
    }
}