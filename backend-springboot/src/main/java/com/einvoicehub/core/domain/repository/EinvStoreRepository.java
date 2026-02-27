package com.einvoicehub.core.domain.repository;

import com.einvoicehub.core.domain.entity.EinvStoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EinvStoreRepository extends JpaRepository<EinvStoreEntity, String>, JpaSpecificationExecutor<EinvStoreEntity> {

    List<EinvStoreEntity> findByTenantId(String tenantId);

    Optional<EinvStoreEntity> findByIdAndTenantId(String id, String tenantId);
}