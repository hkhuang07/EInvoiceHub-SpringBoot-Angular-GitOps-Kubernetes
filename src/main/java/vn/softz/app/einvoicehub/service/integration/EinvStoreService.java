package vn.softz.app.einvoicehub.service.integration;

import vn.softz.app.einvoicehub.domain.entity.EinvStoreEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface EinvStoreService {

    EinvStoreEntity create(EinvStoreEntity store);

    EinvStoreEntity update(String id, EinvStoreEntity store);

    void deactivate(String id);

    EinvStoreEntity activate(String id);

    Optional<EinvStoreEntity> findById(String id);

    List<EinvStoreEntity> findActiveByTenant(String tenantId);

    Page<EinvStoreEntity> findByTenant(String tenantId, Pageable pageable);

    Optional<EinvStoreEntity> findByIdAndTenant(String id, String tenantId);

    boolean existsByIdAndTenant(String id, String tenantId);
}
