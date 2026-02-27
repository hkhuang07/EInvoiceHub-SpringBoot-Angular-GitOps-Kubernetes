package com.einvoicehub.core.domain.repository;

import com.einvoicehub.core.domain.entity.EinvMappingItemTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EinvMappingItemTypeRepository extends JpaRepository<EinvMappingItemTypeEntity, Long>, JpaSpecificationExecutor<EinvMappingItemTypeEntity> {

    Optional<EinvMappingItemTypeEntity> findByProviderIdAndSystemCode(String providerId, String systemCode);
}