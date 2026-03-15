package vn.softz.app.einvoicehub.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softz.app.einvoicehub.domain.entity.EinvMappingInvoiceTypeEntity;
import vn.softz.app.einvoicehub.domain.repository.EinvMappingInvoiceTypeRepository;
import vn.softz.app.einvoicehub.dto.EinvMappingInvoiceTypeDto;
import vn.softz.app.einvoicehub.mapper.EinvMappingInvoiceTypeMapper;
import vn.softz.app.einvoicehub.service.mapping.EinvMappingInvoiceTypeService;

import java.util.List;
import java.util.Optional;

/**
 * Triển khai {@link EinvMappingInvoiceTypeService}.
 *
 * <h3>Mapping mẫu</h3>
 * <pre>
 *   Hub invoiceTypeId ↔ NCC providerInvoiceTypeId
 *   ───────────────────────────────────────────────
 *   1 (HĐ GTGT)      ↔  BKAV: "1"  | MobiFone: "HD"
 *   2 (HĐ bán hàng)  ↔  BKAV: "2"  | MobiFone: "BH"
 * </pre>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EinvMappingInvoiceTypeServiceImpl
        extends AbstractMappingServiceImpl<EinvMappingInvoiceTypeDto, Byte>
        implements EinvMappingInvoiceTypeService {

    private final EinvMappingInvoiceTypeRepository repository;
    private final EinvMappingInvoiceTypeMapper      mapper;

    @Override
    protected String mappingName() { return "InvoiceType"; }

    // ── Template Method Implementations ──────────────────────────────────────

    @Override
    protected List<EinvMappingInvoiceTypeDto> doFindByProvider(String providerId) {
        return mapper.toDtoList(repository.findByProviderId(providerId));
    }

    @Override
    protected List<EinvMappingInvoiceTypeDto> doFindActive(String providerId) {
        return mapper.toDtoList(
            repository.findByProviderId(providerId).stream()
                      .filter(e -> !Boolean.TRUE.equals(e.getInactive()))
                      .toList());
    }

    @Override
    protected Optional<EinvMappingInvoiceTypeDto> doFindById(String id) {
        return repository.findById(id).map(mapper::toDto);
    }

    @Override
    protected Optional<String> doLookupProviderCode(String providerId, Byte hubId) {
        return repository.findByProviderIdAndInvoiceTypeId(providerId, hubId)
                         .filter(e -> !Boolean.TRUE.equals(e.getInactive()))
                         .map(EinvMappingInvoiceTypeEntity::getProviderInvoiceTypeId);
    }

    @Override
    protected Optional<Byte> doLookupHubId(String providerId, String providerCode) {
        return repository.findByProviderIdAndProviderInvoiceTypeId(providerId, providerCode)
                         .filter(e -> !Boolean.TRUE.equals(e.getInactive()))
                         .map(EinvMappingInvoiceTypeEntity::getInvoiceTypeId);
    }

    @Override
    @Transactional
    protected EinvMappingInvoiceTypeDto doCreate(EinvMappingInvoiceTypeDto dto) {
        if (repository.existsByProviderIdAndInvoiceTypeId(dto.getProviderId(), dto.getInvoiceTypeId())) {
            throw duplicateMapping(dto.getProviderId(), dto.getInvoiceTypeId());
        }
        EinvMappingInvoiceTypeEntity entity = mapper.toEntity(dto);
        repository.saveAndFlush(entity);
        log.info("[mapping-InvoiceType] Created: providerId={}, hubTypeId={}, providerTypeId={}",
                 dto.getProviderId(), dto.getInvoiceTypeId(), dto.getProviderInvoiceTypeId());
        return mapper.toDto(entity);
    }

    @Override
    @Transactional
    protected EinvMappingInvoiceTypeDto doUpdate(String id, EinvMappingInvoiceTypeDto dto) {
        EinvMappingInvoiceTypeEntity entity = repository.findById(id)
                .orElseThrow(() -> notFound(id));
        mapper.partialUpdate(entity, dto);
        repository.saveAndFlush(entity);
        log.info("[mapping-InvoiceType] Updated id={}", id);
        return mapper.toDto(entity);
    }

    @Override
    @Transactional
    protected void doSoftDelete(String id) {
        repository.findById(id).ifPresent(e -> {
            e.setInactive(true);
            repository.save(e);
            log.info("[mapping-InvoiceType] Soft-deleted id={}", id);
        });
    }

    // ── Extra methods from interface ─────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public List<EinvMappingInvoiceTypeDto> findByHubInvoiceType(Byte invoiceTypeId) {
        return mapper.toDtoList(repository.findByInvoiceTypeId(invoiceTypeId));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByProviderAndHubType(String providerId, Byte invoiceTypeId) {
        return repository.existsByProviderIdAndInvoiceTypeId(providerId, invoiceTypeId);
    }
}
