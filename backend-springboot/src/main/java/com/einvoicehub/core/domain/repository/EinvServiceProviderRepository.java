package com.einvoicehub.core.domain.repository;

import com.einvoicehub.core.domain.entity.EinvProviderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EinvServiceProviderRepository extends JpaRepository<EinvProviderEntity, Long>,
        JpaSpecificationExecutor<EinvProviderEntity> {
    Optional<EinvProviderEntity> findByProviderCodeAndIsActiveTrue(String providerCode);

    List<EinvProviderEntity> findByIsActiveTrueOrderByDisplayOrderAsc();

    Optional<EinvProviderEntity> findByIsDefaultTrue();
}