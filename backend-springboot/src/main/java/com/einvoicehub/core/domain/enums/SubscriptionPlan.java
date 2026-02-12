package com.einvoicehub.core.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SubscriptionPlan {
    TRIAL("Trial", 100),
    BASIC("Basic", 1000),
    PREMIUM("Premium", 10000),
    ENTERPRISE("Enterprise", -1); // -1 indicates unlimited quota

    private final String displayName;
    private final int defaultQuota;

    public boolean isUnlimited() {
        return defaultQuota == -1;
    }
}