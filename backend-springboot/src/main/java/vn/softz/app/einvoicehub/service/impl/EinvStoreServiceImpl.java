package vn.softz.app.einvoicehub.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softz.app.einvoicehub.domain.entity.EinvStoreEntity;
import vn.softz.app.einvoicehub.domain.repository.EinvStoreRepository;
import vn.softz.app.einvoicehub.domain.repository.MerchantRepository;
import vn.softz.app.einvoicehub.service.integration.EinvStoreService;
import vn.softz.core.exception.BusinessException;

import java.util.List;
import java.util.Optional;

/**
 * Triển khai {@link EinvStoreService}.
 *
 * <h3>Tenant Isolation</h3>
 * <p>Mọi thao tác đọc đều có thể truyền vào {@code tenantId} để đảm bảo
 * Store X của Tenant A không thể bị truy cập bởi Tenant B.
 * Phương thức {@link #findByIdAndTenant} là guard quan trọng nhất,
 * nên được gọi trước khi thực hiện bất kỳ thao tác nào với Store.
 *
 * <h3>Deactivate Guard</h3>
 * <p>Không cho phép deactivate Store khi còn hóa đơn đang ở trạng thái
 * xử lý trong {@code einv_sync_queue}. (TODO: kiểm tra queue trong phase 2).
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EinvStoreServiceImpl implements EinvStoreService {

    private final EinvStoreRepository  storeRepository;
    private final MerchantRepository   merchantRepository;

    @Override
    @Transactional
    public EinvStoreEntity create(EinvStoreEntity store) {
        // Validate tenant tồn tại và đang active
        boolean tenantActive = merchantRepository.findByTenantId(store.getTenantId())
                .map(m -> Boolean.TRUE.equals(m.getIsActive()))
                .orElse(false);
        if (!tenantActive) {
            throw new BusinessException(
                "einv.error.tenant_not_active: " + store.getTenantId());
        }
        store.setIsActive(true);
        EinvStoreEntity saved = storeRepository.save(store);
        log.info("[EinvStore] Created id={}, tenantId={}, name={}",
                 saved.getId(), saved.getTenantId(), saved.getStoreName());
        return saved;
    }

    @Override
    @Transactional
    public EinvStoreEntity update(String id, EinvStoreEntity incoming) {
        EinvStoreEntity existing = findEntityById(id);
        // Không đổi tenantId
        if (incoming.getStoreName() != null) existing.setStoreName(incoming.getStoreName());
        if (incoming.getTaxCode()   != null) existing.setTaxCode(incoming.getTaxCode());
        EinvStoreEntity saved = storeRepository.save(existing);
        log.info("[EinvStore] Updated id={}", id);
        return saved;
    }

    @Override
    @Transactional
    public void deactivate(String id) {
        EinvStoreEntity entity = findEntityById(id);
        // TODO [Phase 2]: Kiểm tra EinvSyncQueueRepository – không cho deactivate
        //   nếu còn queue PENDING/PROCESSING thuộc storeId này.
        entity.setIsActive(false);
        storeRepository.save(entity);
        log.info("[EinvStore] Deactivated id={}", id);
    }

    @Override
    @Transactional
    public EinvStoreEntity activate(String id) {
        EinvStoreEntity entity = findEntityById(id);
        entity.setIsActive(true);
        EinvStoreEntity saved = storeRepository.save(entity);
        log.info("[EinvStore] Activated id={}", id);
        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EinvStoreEntity> findById(String id) {
        return storeRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EinvStoreEntity> findActiveByTenant(String tenantId) {
        return storeRepository.findByTenantIdAndIsActiveTrue(tenantId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EinvStoreEntity> findByTenant(String tenantId, Pageable pageable) {
        return storeRepository.findByTenantId(tenantId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EinvStoreEntity> findByIdAndTenant(String id, String tenantId) {
        return storeRepository.findById(id)
                .filter(s -> tenantId.equals(s.getTenantId()));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByIdAndTenant(String id, String tenantId) {
        return storeRepository.findById(id)
                .map(s -> tenantId.equals(s.getTenantId()))
                .orElse(false);
    }

    // ── Private ───────────────────────────────────────────────────────────────

    private EinvStoreEntity findEntityById(String id) {
        return storeRepository.findById(id)
                .orElseThrow(() -> new BusinessException(
                    "einv.error.store_not_found: id=" + id));
    }
}
