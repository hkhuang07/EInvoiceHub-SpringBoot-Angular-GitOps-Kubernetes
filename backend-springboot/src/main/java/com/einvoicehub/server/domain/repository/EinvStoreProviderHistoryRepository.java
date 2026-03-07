package com.einvoicehub.server.domain.repository;

import com.einvoicehub.server.domain.entity.EinvStoreProviderHistoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EinvStoreProviderHistoryRepository extends JpaRepository<EinvStoreProviderHistoryEntity, String>, JpaSpecificationExecutor<EinvStoreProviderHistoryEntity> {

    List<EinvStoreProviderHistoryEntity> findByTenantId(String tenantId);

    List<EinvStoreProviderHistoryEntity> findByStoreId(String storeId);

    List<EinvStoreProviderHistoryEntity> findByProviderId(String providerId);

    List<EinvStoreProviderHistoryEntity> findByStoreIdAndProviderId(String storeId, String providerId);

    Page<EinvStoreProviderHistoryEntity> findByTenantId(String tenantId, Pageable pageable);

    List<EinvStoreProviderHistoryEntity> findByActionType(String actionType);

    long countByStoreId(String storeId);
}
