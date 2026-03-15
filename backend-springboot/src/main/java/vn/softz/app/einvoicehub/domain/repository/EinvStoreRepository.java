package vn.softz.app.einvoicehub.domain.repository;

import vn.softz.app.einvoicehub.domain.entity.EinvStoreEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EinvStoreRepository extends JpaRepository<EinvStoreEntity, String>, JpaSpecificationExecutor<EinvStoreEntity> {

    List<EinvStoreEntity> findByTenantId(String tenantId);

    Page<EinvStoreEntity> findByTenantId(String tenantId, Pageable pageable);

    List<EinvStoreEntity> findByTenantIdAndIsActiveTrue(String tenantId);

    Optional<EinvStoreEntity> findByStoreName(String storeName);

    Optional<EinvStoreEntity> findByTaxCode(String taxCode);

    boolean existsByTenantIdAndStoreName(String tenantId, String storeName);

    List<EinvStoreEntity> findByTenantIdAndStoreNameContainingIgnoreCase(String tenantId, String storeName);

    long countByTenantId(String tenantId);

    long countByTenantIdAndIsActiveTrue(String tenantId);
}
