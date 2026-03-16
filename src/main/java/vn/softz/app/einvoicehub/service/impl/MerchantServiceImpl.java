package vn.softz.app.einvoicehub.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softz.app.einvoicehub.domain.entity.EinvStoreEntity;
import vn.softz.app.einvoicehub.domain.entity.MerchantEntity;
import vn.softz.app.einvoicehub.domain.repository.EinvStoreRepository;
import vn.softz.app.einvoicehub.domain.repository.MerchantRepository;
import vn.softz.app.einvoicehub.service.integration.MerchantService;
import vn.softz.core.exception.BusinessException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MerchantServiceImpl implements MerchantService {

    private final MerchantRepository merchantRepository;
    private final EinvStoreRepository storeRepository;

    @Override
    @Transactional
    public MerchantEntity create(MerchantEntity merchant) {
        if (merchantRepository.existsByTaxCode(merchant.getTaxCode())) {
            throw new BusinessException(
                "einv.error.merchant_tax_code_exists: " + merchant.getTaxCode());
        }
        if (merchant.getTenantId() == null || merchant.getTenantId().isBlank()) {
            merchant.setTenantId(UUID.randomUUID().toString());
        } else if (merchantRepository.existsByTenantId(merchant.getTenantId())) {
            throw new BusinessException(
                "einv.error.merchant_tenant_id_exists: " + merchant.getTenantId());
        }
        merchant.setIsActive(true);
        MerchantEntity saved = merchantRepository.save(merchant);
        log.info("[Merchant] Created id={}, tenantId={}, taxCode={}",
                 saved.getId(), saved.getTenantId(), saved.getTaxCode());
        return saved;
    }

    @Override
    @Transactional
    public MerchantEntity update(Long id, MerchantEntity incoming) {
        MerchantEntity existing = findEntityById(id);
        // Không đổi tenantId và taxCode
        if (incoming.getCompanyName() != null) existing.setCompanyName(incoming.getCompanyName());
        MerchantEntity saved = merchantRepository.save(existing);
        log.info("[Merchant] Updated id={}", id);
        return saved;
    }

    @Override
    @Transactional
    public void deactivate(Long id) {
        MerchantEntity merchant = findEntityById(id);
        merchant.setIsActive(false);
        merchantRepository.save(merchant);

        List<EinvStoreEntity> stores = storeRepository.findByTenantId(merchant.getTenantId());
        stores.forEach(s -> s.setIsActive(false));
        storeRepository.saveAll(stores);

        log.info("[Merchant] Deactivated id={}, tenantId={} → {} stores also deactivated",
                 id, merchant.getTenantId(), stores.size());
    }

    @Override
    @Transactional
    public MerchantEntity activate(Long id) {
        MerchantEntity entity = findEntityById(id);
        entity.setIsActive(true);
        MerchantEntity saved = merchantRepository.save(entity);
        log.info("[Merchant] Activated id={}", id);
        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MerchantEntity> findById(Long id) {
        return merchantRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MerchantEntity> findByTenantId(String tenantId) {
        return merchantRepository.findByTenantId(tenantId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MerchantEntity> findByTaxCode(String taxCode) {
        return merchantRepository.findByTaxCode(taxCode);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MerchantEntity> findAllActive() {
        return merchantRepository.findByIsActiveTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MerchantEntity> findAllActive(Pageable pageable) {
        return merchantRepository.findByIsActiveTrue(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MerchantEntity> searchByName(String companyName, Pageable pageable) {
        return merchantRepository.findByCompanyNameContainingIgnoreCase(companyName, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByTenantId(String tenantId) {
        return merchantRepository.existsByTenantId(tenantId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByTaxCode(String taxCode) {
        return merchantRepository.existsByTaxCode(taxCode);
    }

    private MerchantEntity findEntityById(Long id) {
        return merchantRepository.findById(id)
                .orElseThrow(() -> new BusinessException(
                    "einv.error.merchant_not_found: id=" + id));
    }
}
