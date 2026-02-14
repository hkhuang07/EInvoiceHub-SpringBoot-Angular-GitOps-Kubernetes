package com.einvoicehub.core.domain.repository;

import com.einvoicehub.core.domain.entity.EinvServiceProviderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EinvServiceProviderRepository extends JpaRepository<EinvServiceProviderEntity, Long>,
        JpaSpecificationExecutor<EinvServiceProviderEntity> {
    Optional<EinvServiceProviderEntity> findByProviderCodeAndIsActiveTrue(String providerCode);

    List<EinvServiceProviderEntity> findByIsActiveTrueOrderByDisplayOrderAsc();

    Optional<EinvServiceProviderEntity> findByIsDefaultTrue();
}