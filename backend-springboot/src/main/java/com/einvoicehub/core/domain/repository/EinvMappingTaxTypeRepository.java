package com.einvoicehub.core.domain.repository;

import com.einvoicehub.core.domain.entity.EinvMappingTaxTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EinvMappingTaxTypeRepository extends JpaRepository<EinvMappingTaxTypeEntity, Long>, JpaSpecificationExecutor<EinvMappingTaxTypeEntity> {

    Optional<EinvMappingTaxTypeEntity> findByProviderIdAndSystemCode(String providerId, String systemCode);
    Optional<EinvMappingTaxTypeEntity> findByProviderIdAndSystemCodeCode(String providerId, String systemCode);
}