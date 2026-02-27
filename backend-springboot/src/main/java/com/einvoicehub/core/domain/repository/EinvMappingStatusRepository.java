package com.einvoicehub.core.domain.repository;

import com.einvoicehub.core.domain.entity.EinvMappingStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EinvMappingStatusRepository extends JpaRepository<EinvMappingStatusEntity, Long>, JpaSpecificationExecutor<EinvMappingStatusEntity> {

    Optional<EinvMappingStatusEntity> findByProviderIdAndProviderStatusCode(String providerId, String providerStatusCode);
}