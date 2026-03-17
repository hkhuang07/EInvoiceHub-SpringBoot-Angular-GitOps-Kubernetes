package vn.softz.app.einvoicehub.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Named;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softz.app.einvoicehub.domain.entity.EinvMappingInvoiceStatusEntity;
import vn.softz.app.einvoicehub.domain.repository.EinvMappingInvoiceStatusRepository;
import vn.softz.app.einvoicehub.dto.EinvMappingInvoiceStatusDto;
import vn.softz.app.einvoicehub.mapper.EinvMappingInvoiceStatusMapper;
import vn.softz.app.einvoicehub.service.EinvMappingInvoiceStatusService;
import vn.softz.app.einvoicehub.exception.BusinessException;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class EinvMappingInvoiceStatusServiceImpl implements EinvMappingInvoiceStatusService {

    private final EinvMappingInvoiceStatusRepository repository;
    private final EinvMappingInvoiceStatusMapper     mapper;

    @Override
    @Transactional
    public EinvMappingInvoiceStatusDto create(EinvMappingInvoiceStatusDto dto) {
        Boolean hubStatus = dto.getInvoiceStatusId() ;
        Byte hubStatusId = booleanToByte(hubStatus);

        if (hubStatusId != null
                && repository.existsByProviderIdAndInvoiceStatusId(dto.getProviderId(), hubStatusId)) {
            throw new BusinessException(
                String.format("einv.error.mapping_duplicate: providerId=%s, statusId=%d",
                              dto.getProviderId(), hubStatusId));
        }

        EinvMappingInvoiceStatusEntity entity = mapper.toEntity(dto);
        repository.saveAndFlush(entity);
        log.info("[mapping-status] Created: providerId={}, hubStatusId={}, providerStatusId={}",
                 dto.getProviderId(), hubStatusId, dto.getProviderInvoiceStatusId());

        return mapper.toDto(entity);
    }

    @Override
    @Transactional
    public EinvMappingInvoiceStatusDto update(String id, EinvMappingInvoiceStatusDto dto) {
        EinvMappingInvoiceStatusEntity entity = findEntityById(id);

        // Chỉ update các trường được phép thay đổi
        if (dto.getProviderInvoiceStatusId() != null) {
            entity.setProviderInvoiceStatusId(dto.getProviderInvoiceStatusId());
        }
        if (dto.getNote() != null) {
            entity.setNote(dto.getNote());
        }
        if (dto.getInactive() != null) {
            entity.setInactive(dto.getInactive() == 1);
        }

        repository.saveAndFlush(entity);
        log.info("[mapping-status] Updated id={}, providerStatusId={}",
                 id, entity.getProviderInvoiceStatusId());

        return mapper.toDto(entity);
    }

    @Override
    @Transactional
    public void delete(String id) {
        EinvMappingInvoiceStatusEntity entity = findEntityById(id);
        entity.setInactive(true);
        repository.save(entity);
        log.info("[mapping-status] Soft-deleted id={}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EinvMappingInvoiceStatusDto> findById(String id) {
        return repository.findById(id).map(mapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EinvMappingInvoiceStatusDto> findAllByProvider(String providerId) {
        return mapper.toDtoList(repository.findByProviderId(providerId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EinvMappingInvoiceStatusDto> findActiveByProvider(String providerId) {
        return mapper.toDtoList(repository.findByProviderIdAndInactiveFalse(providerId));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<String> findProviderStatusId(String providerId, Byte hubStatusId) {
        return repository.findByProviderIdAndInvoiceStatusId(providerId, hubStatusId)
                         .filter(e -> !Boolean.TRUE.equals(e.getInactive()))
                         .map(EinvMappingInvoiceStatusEntity::getProviderInvoiceStatusId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Byte> findHubStatusId(String providerId, String providerStatusId) {
        return repository.findByProviderIdAndProviderInvoiceStatusId(providerId, providerStatusId)
                         .filter(e -> !Boolean.TRUE.equals(e.getInactive()))
                         .map(EinvMappingInvoiceStatusEntity::getInvoiceStatusId);
    }

    @Override
    @Transactional(readOnly = true)
    public String findProviderStatusIdOrDefault(String providerId,
                                                Byte hubStatusId,
                                                String fallback) {
        Optional<String> result = findProviderStatusId(providerId, hubStatusId);
        if (result.isEmpty()) {
            log.warn("[mapping-status] No mapping found Hub→Provider: "
                     + "providerId={}, hubStatusId={} → using fallback={}",
                     providerId, hubStatusId, fallback);
        }
        return result.orElse(fallback);
    }

    @Override
    @Transactional(readOnly = true)
    public Byte findHubStatusIdOrDefault(String providerId,
                                         String providerStatusId,
                                         Byte fallback) {
        Optional<Byte> result = findHubStatusId(providerId, providerStatusId);
        if (result.isEmpty()) {
            log.warn("[mapping-status] No mapping found Provider→Hub: "
                     + "providerId={}, providerStatusId={} → using fallback={}",
                     providerId, providerStatusId, fallback);
        }
        return result.orElse(fallback);
    }

    private EinvMappingInvoiceStatusEntity findEntityById(String id) {
        return repository.findById(id)
                         .orElseThrow(() -> new BusinessException(
                             "einv.error.mapping_status_not_found: id=" + id));
    }

    @Named("booleanToByte")
    private Byte booleanToByte(Boolean value) {
        if (value == null) return null;
        return value ? (byte) 1 : (byte) 0;
    }
}
