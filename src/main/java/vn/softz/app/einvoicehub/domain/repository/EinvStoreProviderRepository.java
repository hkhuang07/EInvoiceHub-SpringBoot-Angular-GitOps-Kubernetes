package vn.softz.app.einvoicehub.domain.repository;

import vn.softz.app.einvoicehub.domain.entity.EinvStoreProviderEntity;
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

    Optional<EinvStoreProviderEntity> findByStoreId(String storeId);

    List<EinvStoreProviderEntity> findByProviderId(String providerId);

    List<EinvStoreProviderEntity> findByProviderIdAndStatus(String providerId, Byte status);

    Optional<EinvStoreProviderEntity> findByStoreIdAndProviderId(String storeId, String providerId);

    List<EinvStoreProviderEntity> findByTenantIdAndStatus(String tenantId, Byte status);

    List<EinvStoreProviderEntity> findByStoreIdAndStatus(String tenantId, Byte status);

    Page<EinvStoreProviderEntity> findByStatus(Byte status, Pageable pageable);

    boolean existsByStoreIdAndProviderId(String storeId, String providerId);

    long countByStoreId(String storeId);

    long countByProviderId(String providerId);





}
