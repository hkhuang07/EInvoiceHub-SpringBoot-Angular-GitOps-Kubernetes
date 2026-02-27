package com.einvoicehub.core.domain.repository;

import com.einvoicehub.core.domain.entity.EinvStoreProviderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface EinvStoreProviderRepository extends JpaRepository<EinvStoreProviderEntity, String>, JpaSpecificationExecutor<EinvStoreProviderEntity> {

    // Optional<EinvStoreProviderEntity> findByStoreId(String storeId);

    @Query("SELECT e FROM EinvStoreProviderEntity e WHERE e.storeId = :storeId")
    Optional<EinvStoreProviderEntity> findByStoreId(@Param("storeId") String storeId);
}