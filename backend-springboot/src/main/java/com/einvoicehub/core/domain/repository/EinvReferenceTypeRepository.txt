package com.einvoicehub.core.domain.repository;

import com.einvoicehub.core.domain.entity.EinvReferenceTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface EinvReferenceTypeRepository extends JpaRepository<EinvReferenceTypeEntity, Integer>,
        JpaSpecificationExecutor<EinvReferenceTypeEntity> {
}