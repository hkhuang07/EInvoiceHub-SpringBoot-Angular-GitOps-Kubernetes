package com.einvoicehub.core.domain.repository;

import com.einvoicehub.core.domain.entity.EinvUnitEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface EinvUnitRepository extends JpaRepository<EinvUnitEntity, String>,
        JpaSpecificationExecutor<EinvUnitEntity> {
    //...
}