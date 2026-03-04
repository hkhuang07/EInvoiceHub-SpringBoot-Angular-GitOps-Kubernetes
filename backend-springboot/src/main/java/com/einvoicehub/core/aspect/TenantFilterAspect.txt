package com.einvoicehub.core.aspect;

import com.einvoicehub.core.multitenant.TenantContext;
import jakarta.persistence.EntityManager;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
//Sử dụng AOP (Aspect Oriented Programming) để tự động kích hoạt Hibernate Filter mỗi khi có truy vấn vào Database.
@Aspect
@Component
public class TenantFilterAspect {

    @Autowired
    private EntityManager entityManager;
    @Before("execution(* com.einvoicehub.core.domain.repository..*(..))")
    public void enableTenantFilter() {
        String tenantId = TenantContext.getTenantId();
        if (tenantId != null) {
            Session session = entityManager.unwrap(Session.class);
            session.enableFilter("tenantFilter")
                    .setParameter("tenantId", tenantId);
        }
    }
}