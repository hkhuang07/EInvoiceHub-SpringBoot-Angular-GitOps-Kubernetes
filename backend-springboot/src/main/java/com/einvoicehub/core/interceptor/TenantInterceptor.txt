package com.einvoicehub.core.interceptor;

import com.einvoicehub.core.multitenant.TenantContext;
import com.einvoicehub.core.exception.TenantNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.util.StringUtils;
//Interceptor để đánh chặn mọi request, trích xuất X-Tenant-Id từ HTTP Header và nạp vào Context.
@Component
public class TenantInterceptor implements HandlerInterceptor {
    private static final String TENANT_HEADER = "X-Tenant-Id";

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request,
                             @NonNull HttpServletResponse response,
                             @NonNull Object handler) {

        String tenantId = request.getHeader(TENANT_HEADER);

        if (!StringUtils.hasText(tenantId)) {
            throw new TenantNotFoundException("Required Header 'X-Tenant-Id' is missing.");
        }

        TenantContext.setTenantId(tenantId);
        return true;
    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request,
                                @NonNull HttpServletResponse response,
                                @NonNull Object handler, Exception ex) {
        TenantContext.clear();
    }
}