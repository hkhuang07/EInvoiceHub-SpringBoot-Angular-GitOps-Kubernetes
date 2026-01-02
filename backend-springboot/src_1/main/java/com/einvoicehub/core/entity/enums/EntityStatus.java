package com.einvoicehub.core.entity.enums;

public enum EntityStatus {
    ACTIVE("Hoạt động"),
    INACTIVE("Không hoạt động"),
    SUSPENDED("Tạm đình chỉ");

    private final String description;

    EntityStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}