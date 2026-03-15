package vn.softz.app.einvoicehub.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softz.app.einvoicehub.domain.entity.EinvProviderEntity;
import vn.softz.app.einvoicehub.domain.repository.EinvProviderRepository;
import vn.softz.app.einvoicehub.domain.repository.EinvStoreProviderRepository;
import vn.softz.app.einvoicehub.service.catalog.EinvProviderService;
import vn.softz.core.exception.BusinessException;

import java.util.List;
import java.util.Optional;

/**
 * Triển khai {@link EinvProviderService}.
 *
 * <h3>Soft-delete strategy</h3>
 * <p>NCC sử dụng {@code inactive = true} thay vì xóa vật lý vì:
 * <ol>
 *   <li>NCC có FK tham chiếu từ nhiều bảng ({@code einv_store_provider},
 *       {@code einv_invoices}, các bảng mapping…).
 *   <li>Cần giữ lịch sử tích hợp để audit.
 * </ol>
 *
 * <h3>Guard khi deactivate</h3>
 * <p>Không được deactivate NCC nếu còn Store đang tích hợp
 * ({@code einv_store_provider.status = 1}).
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EinvProviderServiceImpl implements EinvProviderService {

    private final EinvProviderRepository     repository;
    private final EinvStoreProviderRepository storeProviderRepository;

    @Override
    @Transactional
    public EinvProviderEntity create(EinvProviderEntity entity) {
        // Validate unique providerCode
        if (repository.findByProviderCode(entity.getProviderCode()).isPresent()) {
            throw new BusinessException(
                "einv.error.provider_code_exists: " + entity.getProviderCode());
        }
        entity.setInactive(false);
        EinvProviderEntity saved = repository.save(entity);
        log.info("[EinvProvider] Created id={}, code={}", saved.getId(), saved.getProviderCode());
        return saved;
    }

    @Override
    @Transactional
    public EinvProviderEntity update(String id, EinvProviderEntity entity) {
        EinvProviderEntity existing = findEntityById(id);
        // Không được đổi providerCode
        if (entity.getProviderName()    != null) existing.setProviderName(entity.getProviderName());
        if (entity.getIntegrationUrl()  != null) existing.setIntegrationUrl(entity.getIntegrationUrl());
        if (entity.getLookupUrl()       != null) existing.setLookupUrl(entity.getLookupUrl());
        EinvProviderEntity saved = repository.save(existing);
        log.info("[EinvProvider] Updated id={}", id);
        return saved;
    }

    @Override
    @Transactional
    public void delete(String id) {
        // Soft-delete thay vì hard-delete (FK safety)
        deactivate(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EinvProviderEntity> findById(String id) {
        return repository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EinvProviderEntity> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EinvProviderEntity> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EinvProviderEntity> findAllActive() {
        return repository.findByInactiveFalse();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EinvProviderEntity> findByCode(String providerCode) {
        return repository.findByProviderCode(providerCode);
    }

    @Override
    @Transactional
    public EinvProviderEntity activate(String id) {
        EinvProviderEntity entity = findEntityById(id);
        entity.setInactive(false);
        EinvProviderEntity saved = repository.save(entity);
        log.info("[EinvProvider] Activated id={}", id);
        return saved;
    }

    @Override
    @Transactional
    public EinvProviderEntity deactivate(String id) {
        EinvProviderEntity entity = findEntityById(id);
        // Guard: còn Store đang tích hợp NCC này
        boolean hasActiveIntegrations = !storeProviderRepository
                .findByProviderIdAndStatus(id, (byte) 1)
                .isEmpty();
        if (hasActiveIntegrations) {
            throw new BusinessException(
                "einv.error.provider_has_active_integrations: " + entity.getProviderCode());
        }
        entity.setInactive(true);
        EinvProviderEntity saved = repository.save(entity);
        log.info("[EinvProvider] Deactivated id={}", id);
        return saved;
    }

    // ── Private ───────────────────────────────────────────────────────────────

    private EinvProviderEntity findEntityById(String id) {
        return repository.findById(id)
                         .orElseThrow(() -> new BusinessException(
                             "einv.error.provider_not_found: " + id));
    }
}
