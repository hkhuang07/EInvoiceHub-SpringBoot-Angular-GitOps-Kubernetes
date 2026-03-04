package com.einvoicehub.core.domain.repository;

import com.einvoicehub.core.domain.entity.EinvStoreSerialEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EinvStoreSerialRepository extends JpaRepository<EinvStoreSerialEntity, String>, JpaSpecificationExecutor<EinvStoreSerialEntity> {
    List<EinvStoreSerialEntity> findByStoreProviderIdAndStatus(String storeProviderId, Integer status);

    @Query(value = "SELECT * FROM einv_store_serial e " +
            "WHERE e.store_id = :storeProviderId " +
            "AND e.status = 1 " +
            "ORDER BY e.created_date DESC LIMIT 1",
            nativeQuery = true)
    Optional<EinvStoreSerialEntity> findLatestActive(
            @Param("storeProviderId") String storeProviderId);

}