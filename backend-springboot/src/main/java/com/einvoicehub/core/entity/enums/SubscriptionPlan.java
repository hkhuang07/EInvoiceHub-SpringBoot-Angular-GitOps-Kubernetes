package com.einvoicehub.core.entity.enums;

public enum SubscriptionPlan {
    TRIAL("Dùng thử", 100),
    BASIC("Cơ bản", 1000),
    PREMIUM("Cao cấp", 10000),
    ENTERPRISE("Doanh nghiệp", -1); // -1 = không giới hạn

    private final String displayName;
    private final int defaultQuota;

    SubscriptionPlan(String displayName, int defaultQuota) {
        this.displayName = displayName;
        this.defaultQuota = defaultQuota;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getDefaultQuota() {
        return defaultQuota;
    }

    public boolean isUnlimited() {
        return defaultQuota == -1;
    }
}