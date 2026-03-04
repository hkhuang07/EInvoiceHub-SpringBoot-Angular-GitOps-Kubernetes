package com.einvoicehub.core.domain.repository;

import com.einvoicehub.core.domain.entity.EinvItemTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface EinvItemTypeRepository extends JpaRepository<EinvItemTypeEntity, Integer>,
        JpaSpecificationExecutor<EinvItemTypeEntity> {
}