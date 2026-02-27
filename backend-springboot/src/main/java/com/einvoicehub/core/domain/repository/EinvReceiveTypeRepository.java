package com.einvoicehub.core.domain.repository;

import com.einvoicehub.core.domain.entity.EinvReceiveTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface EinvReceiveTypeRepository extends JpaRepository<EinvReceiveTypeEntity, Integer>,
        JpaSpecificationExecutor<EinvReceiveTypeEntity> {
}