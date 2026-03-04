package com.einvoicehub.core.context;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TenantContext {

    private static final ThreadLocal<Long> currentTenant = new ThreadLocal<>();

    public static void setMerchantId(Long merchantId) {
        log.debug("Setting current MerchantID to: {}", merchantId);
        currentTenant.set(merchantId);
    }

    public static Long getMerchantId() {
        return currentTenant.get();
    }

    public static void clear() {
        currentTenant.remove();
    }
}


/**Chú giải thích:
 * Lưu trữ định danh Doanh nghiệp (Merchant) trong luồng xử lý hiện tại.
 * Giúp các Repository/Service tự động biết đang làm việc cho doanh nghiệp nào.
 */