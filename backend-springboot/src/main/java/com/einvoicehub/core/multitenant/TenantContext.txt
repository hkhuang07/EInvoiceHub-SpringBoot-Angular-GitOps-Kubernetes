package com.einvoicehub.core.multitenant;

import lombok.extern.slf4j.Slf4j;

//Sử dụng ThreadLocal để cô lập tenantId trong phạm vi mỗi request, đảm bảo an toàn đa luồng
@Slf4j
public class TenantContext {
    private static final ThreadLocal<String> CURRENT_TENANT = new ThreadLocal<>();

    public static void setTenantId(String tenantId) {
        log.trace("Setting current tenant to: {}", tenantId);
        CURRENT_TENANT.set(tenantId);
    }

    public static String getTenantId() {
        return CURRENT_TENANT.get();
    }

    public static void clear() {
        CURRENT_TENANT.remove();
    }
}


