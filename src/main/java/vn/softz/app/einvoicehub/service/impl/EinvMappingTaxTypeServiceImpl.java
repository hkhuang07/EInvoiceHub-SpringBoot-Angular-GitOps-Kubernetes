package vn.softz.app.einvoicehub.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softz.app.einvoicehub.domain.entity.EinvMappingTaxTypeEntity;
import vn.softz.app.einvoicehub.domain.repository.EinvMappingTaxTypeRepository;
import vn.softz.app.einvoicehub.dto.EinvMappingTaxTypeDto;
import vn.softz.app.einvoicehub.mapper.EinvMappingTaxTypeMapper;
import vn.softz.app.einvoicehub.service.mapping.EinvMappingTaxTypeService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class EinvMappingTaxTypeServiceImpl
        extends AbstractMappingServiceImpl<EinvMappingTaxTypeDto, String>
        implements EinvMappingTaxTypeService {

    private final EinvMappingTaxTypeRepository repository;
    private final EinvMappingTaxTypeMapper      mapper;

    @Override
    protected String mappingName() { return "TaxType"; }

    @Override
    protected List<EinvMappingTaxTypeDto> doFindByProvider(String providerId) {
        return mapper.toDtoList(repository.findByProviderId(providerId));
    }

    @Override
    protected List<EinvMappingTaxTypeDto> doFindActive(String providerId) {
        return mapper.toDtoList(repository.findByProviderIdAndInactiveFalse(providerId));
    }

    @Override
    protected Optional<EinvMappingTaxTypeDto> doFindById(String id) {
        return repository.findById(id).map(mapper::toDto);
    }

    @Override
    protected Optional<String> doLookupProviderCode(String providerId, String hubTaxTypeId) {
        return repository.findByProviderIdAndTaxTypeId(providerId, hubTaxTypeId)
                         .filter(e -> !Boolean.TRUE.equals(e.getInactive()))
                         .map(EinvMappingTaxTypeEntity::getProviderTaxTypeId);
    }

    @Override
    protected Optional<String> doLookupHubId(String providerId, String providerTaxTypeId) {
        return repository.findByProviderIdAndProviderTaxTypeId(providerId, providerTaxTypeId)
                         .filter(e -> !Boolean.TRUE.equals(e.getInactive()))
                         .map(EinvMappingTaxTypeEntity::getTaxTypeId);
    }

    @Override
    @Transactional
    protected EinvMappingTaxTypeDto doCreate(EinvMappingTaxTypeDto dto) {
        if (repository.existsByProviderIdAndTaxTypeId(dto.getProviderId(), dto.getTaxTypeId())) {
            throw duplicateMapping(dto.getProviderId(), dto.getTaxTypeId());
        }
        EinvMappingTaxTypeEntity entity = mapper.toEntity(dto);
        repository.saveAndFlush(entity);
        log.info("[mapping-TaxType] Created: providerId={}, hubTaxTypeId={}, providerCode={}",
                 dto.getProviderId(), dto.getTaxTypeId(), dto.getProviderTaxTypeId());
        return mapper.toDto(entity);
    }

    @Override
    @Transactional
    protected EinvMappingTaxTypeDto doUpdate(String id, EinvMappingTaxTypeDto dto) {
        EinvMappingTaxTypeEntity entity = repository.findById(id)
                .orElseThrow(() -> notFound(id));
        mapper.partialUpdate(entity, dto);
        repository.saveAndFlush(entity);
        log.info("[mapping-TaxType] Updated id={}", id);
        return mapper.toDto(entity);
    }

    @Override
    @Transactional
    protected void doSoftDelete(String id) {
        repository.findById(id).ifPresent(e -> {
            e.setInactive(true);
            repository.save(e);
            log.info("[mapping-TaxType] Soft-deleted id={}", id);
        });
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<String> findProviderTaxCodeByRate(String providerId, BigDecimal vatRate) {
        // Tìm tất cả mapping của NCC, lọc theo category_tax_type.vat
        // TODO: Bổ sung JOIN query hoặc dùng cache nếu cần hiệu năng cao
        return repository.findByProviderId(providerId).stream()
                .filter(e -> !Boolean.TRUE.equals(e.getInactive()))
                .filter(e -> {
                    // Cần join sang category_tax_type để lấy vat rate
                    // Tạm thời: lookup qua providerTaxRate nếu NCC lưu rate dạng String
                    if (e.getProviderTaxRate() == null) return false;
                    try {
                        BigDecimal r = new BigDecimal(e.getProviderTaxRate()
                                .replace("%", "").trim());
                        return r.compareTo(vatRate) == 0;
                    } catch (NumberFormatException ex) {
                        return false; // VD: "KCT", "KKKH" → không so sánh được
                    }
                })
                .map(EinvMappingTaxTypeEntity::getProviderTaxTypeId)
                .findFirst();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByProviderAndHubTaxType(String providerId, String taxTypeId) {
        return repository.existsByProviderIdAndTaxTypeId(providerId, taxTypeId);
    }
}
