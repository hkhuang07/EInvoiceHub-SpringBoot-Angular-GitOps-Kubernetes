package vn.softz.app.einvoicehub.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.softz.app.einvoicehub.domain.entity.EinvStoreSerialEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface EinvStoreSerialRepository extends JpaRepository<EinvStoreSerialEntity, String> {
    
    List<EinvStoreSerialEntity> findByStoreId(String storeId);
    
    List<EinvStoreSerialEntity> findByStoreIdAndProviderId(String storeId, String providerId);
    
    boolean existsByStoreIdAndStatus(String storeId, Integer status);
    
    @Query(value = "SELECT * FROM einv_store_serial e " +
       "WHERE e.store_id = :storeId " +
       "AND e.provider_id = :providerId " +
       "AND e.status = 1 " +
       "ORDER BY e.created_date DESC LIMIT 1", 
       nativeQuery = true)
    Optional<EinvStoreSerialEntity> findLatestActive(
            @Param("storeId") String storeId,
            @Param("providerId") String providerId);
}
