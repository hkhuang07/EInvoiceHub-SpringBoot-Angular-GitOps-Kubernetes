package com.einvoicehub.core.domain.repository;

import com.einvoicehub.core.domain.entity.EinvMappingUnitEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EinvMappingUnitRepository extends JpaRepository<EinvMappingUnitEntity, Long>, JpaSpecificationExecutor<EinvMappingUnitEntity> {

    Optional<EinvMappingUnitEntity> findByProviderIdAndSystemCode(String providerId, String systemCode);
}