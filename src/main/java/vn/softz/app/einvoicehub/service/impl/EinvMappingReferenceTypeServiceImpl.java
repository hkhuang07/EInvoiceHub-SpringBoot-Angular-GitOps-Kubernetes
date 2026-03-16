package vn.softz.app.einvoicehub.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softz.app.einvoicehub.domain.entity.EinvMappingReferenceTypeEntity;
import vn.softz.app.einvoicehub.domain.repository.EinvMappingReferenceTypeRepository;
import vn.softz.app.einvoicehub.dto.EinvMappingReferenceTypeDto;
import vn.softz.app.einvoicehub.mapper.EinvMappingReferenceTypeMapper;
import vn.softz.app.einvoicehub.service.mapping.EinvMappingReferenceTypeService;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class EinvMappingReferenceTypeServiceImpl
        extends AbstractMappingServiceImpl<EinvMappingReferenceTypeDto, Byte>
        implements EinvMappingReferenceTypeService {

    private final EinvMappingReferenceTypeRepository repository;
    private final EinvMappingReferenceTypeMapper      mapper;

    @Override protected String mappingName() { return "ReferenceType"; }

    @Override
    protected List<EinvMappingReferenceTypeDto> doFindByProvider(String p) {
        return mapper.toDtoList(repository.findByProviderId(p));
    }

    @Override
    protected List<EinvMappingReferenceTypeDto> doFindActive(String p) {
        return mapper.toDtoList(
            repository.findByProviderId(p).stream()
                      .filter(e -> !Boolean.TRUE.equals(e.getInactive())).toList());
    }

    @Override
    protected Optional<EinvMappingReferenceTypeDto> doFindById(String id) {
        return repository.findById(id).map(mapper::toDto);
    }

    @Override
    protected Optional<String> doLookupProviderCode(String providerId, Byte hubId) {
        return repository.findByProviderIdAndReferenceTypeId(providerId, hubId)
                         .filter(e -> !Boolean.TRUE.equals(e.getInactive()))
                         .map(EinvMappingReferenceTypeEntity::getProviderReferenceTypeId);
    }

    @Override
    protected Optional<Byte> doLookupHubId(String providerId, String providerCode) {
        return repository.findByProviderIdAndProviderReferenceTypeId(providerId, providerCode)
                         .filter(e -> !Boolean.TRUE.equals(e.getInactive()))
                         .map(EinvMappingReferenceTypeEntity::getReferenceTypeId);
    }

    @Override
    @Transactional
    protected EinvMappingReferenceTypeDto doCreate(EinvMappingReferenceTypeDto dto) {
        if (existsByProviderAndHubReferenceType(dto.getProviderId(), dto.getReferenceTypeId())) {
            throw duplicateMapping(dto.getProviderId(), dto.getReferenceTypeId());
        }
        EinvMappingReferenceTypeEntity entity = mapper.toEntity(dto);
        repository.saveAndFlush(entity);
        log.info("[mapping-ReferenceType] Created: providerId={}, hubRefTypeId={}",
                 dto.getProviderId(), dto.getReferenceTypeId());
        return mapper.toDto(entity);
    }

    @Override
    @Transactional
    protected EinvMappingReferenceTypeDto doUpdate(String id, EinvMappingReferenceTypeDto dto) {
        EinvMappingReferenceTypeEntity entity = repository.findById(id)
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
    public boolean existsByProviderAndHubReferenceType(String providerId, Byte referenceTypeId) {
        return repository.findByProviderIdAndReferenceTypeId(providerId, referenceTypeId).isPresent();
    }
}
