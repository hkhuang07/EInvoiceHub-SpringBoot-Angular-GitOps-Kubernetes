package vn.softz.app.einvoicehub.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.softz.app.einvoicehub.domain.entity.EinvStoreProviderEntity;

import java.util.Optional;

@Repository
public interface EinvStoreProviderRepository extends JpaRepository<EinvStoreProviderEntity, String> {
    @Query("SELECT e FROM EinvStoreProviderEntity e WHERE e.storeId = :storeId")
    Optional<EinvStoreProviderEntity> findByStoreId(@Param("storeId") String storeId);
}
