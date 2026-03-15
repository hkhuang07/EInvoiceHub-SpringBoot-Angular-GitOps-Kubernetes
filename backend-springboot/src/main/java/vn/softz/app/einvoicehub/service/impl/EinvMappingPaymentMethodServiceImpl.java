package vn.softz.app.einvoicehub.service.impl;

// ────────────────────────────────────────────────────────────────────────────
// FILE: EinvMappingPaymentMethodServiceImpl.java
// ────────────────────────────────────────────────────────────────────────────

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softz.app.einvoicehub.domain.entity.EinvMappingPaymentMethodEntity;
import vn.softz.app.einvoicehub.domain.repository.EinvMappingPaymentMethodRepository;
import vn.softz.app.einvoicehub.dto.EinvMappingPaymentMethodDto;
import vn.softz.app.einvoicehub.mapper.EinvMappingPaymentMethodMapper;
import vn.softz.app.einvoicehub.service.mapping.EinvMappingPaymentMethodService;

import java.util.List;
import java.util.Optional;

/**
 * Mapping Phương thức thanh toán Hub ↔ NCC.
 *
 * <pre>
 *   Hub paymentMethodId (Byte: 1=TM, 2=CK…) ↔ NCC code
 *   BKAV: 1→"TM", 2→"CK", 3→"TMCK"
 *   MobiFone: 1→"CASH", 2→"TRANSFER"
 * </pre>
 */
@Slf4j
@Service
@RequiredArgsConstructor
class EinvMappingPaymentMethodServiceImpl
        extends AbstractMappingServiceImpl<EinvMappingPaymentMethodDto, Byte>
        implements EinvMappingPaymentMethodService {

    private final EinvMappingPaymentMethodRepository repository;
    private final EinvMappingPaymentMethodMapper      mapper;

    @Override protected String mappingName() { return "PaymentMethod"; }

    @Override
    protected List<EinvMappingPaymentMethodDto> doFindByProvider(String p) {
        return mapper.toDtoList(repository.findByProviderId(p));
    }

    @Override
    protected List<EinvMappingPaymentMethodDto> doFindActive(String p) {
        return mapper.toDtoList(
            repository.findByProviderId(p).stream()
                      .filter(e -> !Boolean.TRUE.equals(e.getInactive())).toList());
    }

    @Override
    protected Optional<EinvMappingPaymentMethodDto> doFindById(String id) {
        return repository.findById(id).map(mapper::toDto);
    }

    @Override
    protected Optional<String> doLookupProviderCode(String providerId, Byte hubId) {
        return repository.findByProviderIdAndPaymentMethodId(providerId, hubId)
                         .filter(e -> !Boolean.TRUE.equals(e.getInactive()))
                         .map(EinvMappingPaymentMethodEntity::getProviderPaymentMethodId);
    }

    @Override
    protected Optional<Byte> doLookupHubId(String providerId, String providerCode) {
        return repository.findByProviderIdAndProviderPaymentMethodId(providerId, providerCode)
                         .filter(e -> !Boolean.TRUE.equals(e.getInactive()))
                         .map(EinvMappingPaymentMethodEntity::getPaymentMethodId);
    }

    @Override
    @Transactional
    protected EinvMappingPaymentMethodDto doCreate(EinvMappingPaymentMethodDto dto) {
        if (existsByProviderAndHubMethod(dto.getProviderId(), dto.getPaymentMethodId())) {
            throw duplicateMapping(dto.getProviderId(), dto.getPaymentMethodId());
        }
        EinvMappingPaymentMethodEntity entity = mapper.toEntity(dto);
        repository.saveAndFlush(entity);
        log.info("[mapping-PaymentMethod] Created: providerId={}, hubMethodId={}",
                 dto.getProviderId(), dto.getPaymentMethodId());
        return mapper.toDto(entity);
    }

    @Override
    @Transactional
    protected EinvMappingPaymentMethodDto doUpdate(String id, EinvMappingPaymentMethodDto dto) {
        EinvMappingPaymentMethodEntity entity = repository.findById(id)
                .orElseThrow(() -> notFound(id));
        mapper.partialUpdate(entity, dto);
        repository.saveAndFlush(entity);
        return mapper.toDto(entity);
    }

    @Override
    @Transactional
    protected void doSoftDelete(String id) {
        repository.findById(id).ifPresent(e -> { e.setInactive(true); repository.save(e); });
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByProviderAndHubMethod(String providerId, Byte paymentMethodId) {
        return repository.findByProviderIdAndPaymentMethodId(providerId, paymentMethodId).isPresent();
    }
}
