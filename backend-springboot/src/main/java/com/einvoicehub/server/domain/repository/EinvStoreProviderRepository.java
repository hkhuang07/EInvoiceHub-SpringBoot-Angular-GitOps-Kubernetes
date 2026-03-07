package com.einvoicehub.server.domain.repository;

import com.einvoicehub.server.domain.entity.EinvStoreProviderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EinvStoreProviderRepository extends JpaRepository<EinvStoreProviderEntity, String>, JpaSpecificationExecutor<EinvStoreProviderEntity> {
    List<EinvStoreProviderEntity> findByTenantId(String tenantId);

    List<EinvStoreProviderEntity> findByStoreId(String storeId);

    List<EinvStoreProviderEntity> findByProviderId(String providerId);

    Optional<EinvStoreProviderEntity> findByStoreIdAndProviderId(String storeId, String providerId);

    List<EinvStoreProviderEntity> findByTenantIdAndStatus(String tenantId, Boolean status);

    List<EinvStoreProviderEntity> findByStoreIdAndStatus(String tenantId, Boolean status);

    Page<EinvStoreProviderEntity> findByStatus(Boolean status, Pageable pageable);

    boolean existsByStoreIdAndProviderId(String storeId, String providerId);

    long countByStoreId(String storeId);

    long countByProviderId(String providerId);
}
