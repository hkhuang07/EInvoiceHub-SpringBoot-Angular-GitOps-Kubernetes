package com.einvoicehub.core.domain.repository;

import com.einvoicehub.core.domain.entity.EinvTaxTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface EinvTaxTypeRepository extends JpaRepository<EinvTaxTypeEntity, String>,
        JpaSpecificationExecutor<EinvTaxTypeEntity> {
    //...
}