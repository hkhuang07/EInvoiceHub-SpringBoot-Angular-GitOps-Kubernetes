package vn.softz.app.einvoicehub.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softz.app.einvoicehub.domain.entity.EinvMappingItemTypeEntity;
import vn.softz.app.einvoicehub.domain.repository.EinvMappingItemTypeRepository;
import vn.softz.app.einvoicehub.dto.EinvMappingItemTypeDto;
import vn.softz.app.einvoicehub.mapper.EinvMappingItemTypeMapper;
import vn.softz.app.einvoicehub.service.mapping.EinvMappingItemTypeService;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class EinvMappingItemTypeServiceImpl
        extends AbstractMappingServiceImpl<EinvMappingItemTypeDto, Byte>
        implements EinvMappingItemTypeService {

    private final EinvMappingItemTypeRepository repository;
    private final EinvMappingItemTypeMapper      mapper;

    @Override protected String mappingName() { return "ItemType"; }

    @Override
    protected List<EinvMappingItemTypeDto> doFindByProvider(String p) {
        return mapper.toDtoList(repository.findByProviderId(p));
    }

    @Override
    protected List<EinvMappingItemTypeDto> doFindActive(String p) {
        return mapper.toDtoList(
            repository.findByProviderId(p).stream()
                      .filter(e -> !Boolean.TRUE.equals(e.getInactive())).toList());
    }

    @Override
    protected Optional<EinvMappingItemTypeDto> doFindById(String id) {
        return repository.findById(id).map(mapper::toDto);
    }

    @Override
    protected Optional<String> doLookupProviderCode(String providerId, Byte hubId) {
        return repository.findByProviderIdAndItemTypeId(providerId, hubId)
                         .filter(e -> !Boolean.TRUE.equals(e.getInactive()))
                         .map(EinvMappingItemTypeEntity::getProviderItemTypeId);
    }

    @Override
    protected Optional<Byte> doLookupHubId(String providerId, String providerCode) {
        return repository.findByProviderIdAndProviderItemTypeId(providerId, providerCode)
                         .filter(e -> !Boolean.TRUE.equals(e.getInactive()))
                         .map(EinvMappingItemTypeEntity::getItemTypeId);
    }

    @Override
    @Transactional
    protected EinvMappingItemTypeDto doCreate(EinvMappingItemTypeDto dto) {
        if (existsByProviderAndHubItemType(dto.getProviderId(), dto.getItemTypeId())) {
            throw duplicateMapping(dto.getProviderId(), dto.getItemTypeId());
        }
        EinvMappingItemTypeEntity entity = mapper.toEntity(dto);
        repository.saveAndFlush(entity);
        log.info("[mapping-ItemType] Created: providerId={}, hubItemTypeId={}",
                 dto.getProviderId(), dto.getItemTypeId());
        return mapper.toDto(entity);
    }

    @Override
    @Transactional
    protected EinvMappingItemTypeDto doUpdate(String id, EinvMappingItemTypeDto dto) {
        EinvMappingItemTypeEntity entity = repository.findById(id)
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
    public boolean existsByProviderAndHubItemType(String providerId, Byte itemTypeId) {
        return repository.findByProviderIdAndItemTypeId(providerId, itemTypeId).isPresent();
    }
}
