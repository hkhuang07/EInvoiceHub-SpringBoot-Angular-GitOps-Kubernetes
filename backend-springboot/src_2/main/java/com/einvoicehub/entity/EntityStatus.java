package com.einvoicehub.entity;

/**
 * Entity Status Enum
 * 
 * Định nghĩa các trạng thái của thực thể trong hệ thống
 */
public enum EntityStatus {
    ACTIVE("Hoạt động"),
    INACTIVE("Không hoạt động"),
    SUSPENDED("Tạm khóa"),
    PENDING("Chờ duyệt"),
    DELETED("Đã xóa");

    private final String description;

    EntityStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}