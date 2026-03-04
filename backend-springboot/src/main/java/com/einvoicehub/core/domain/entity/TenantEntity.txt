package com.einvoicehub.core.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
//Lớp cha cho các thực thể nghiệp vụ (Invoices, Stores...), triển khai cơ chế lọc dữ liệu Hibernate Filter.
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@MappedSuperclass
@FilterDef(
        name = "tenantFilter",
        parameters = @ParamDef(name = "tenantId", type = String.class)
)
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
public abstract class TenantEntity extends BaseAuditEntity {

    @Column(name = "tenant_id", nullable = false, length = 50)
    private String tenantId;
}