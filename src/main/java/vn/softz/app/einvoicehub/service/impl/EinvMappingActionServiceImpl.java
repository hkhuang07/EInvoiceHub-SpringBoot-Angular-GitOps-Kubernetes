package vn.softz.app.einvoicehub.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softz.app.einvoicehub.domain.entity.EinvMappingActionEntity;
import vn.softz.app.einvoicehub.domain.repository.EinvMappingActionRepository;
import vn.softz.app.einvoicehub.dto.EinvMappingActionDto;
import vn.softz.app.einvoicehub.mapper.EinvMappingActionMapper;
import vn.softz.app.einvoicehub.service.mapping.EinvMappingActionService;
import vn.softz.core.exception.BusinessException;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class EinvMappingActionServiceImpl implements EinvMappingActionService {

    private final EinvMappingActionRepository repository;
    private final EinvMappingActionMapper      mapper;

    // ── Lookup ────────────────────────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public Optional<String> findProviderCmd(String providerId, String hubAction) {
        return repository.findByProviderIdAndHubAction(providerId, hubAction)
                         .map(EinvMappingActionEntity::getProviderCmd);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<String> findHubAction(String providerId, String providerCmd) {
        return repository.findByProviderIdAndProviderCmd(providerId, providerCmd)
                         .map(EinvMappingActionEntity::getHubAction);
    }

    @Override
    @Transactional(readOnly = true)
    public String findProviderCmdOrDefault(String providerId, String hubAction, String fallback) {
        Optional<String> result = findProviderCmd(providerId, hubAction);
        if (result.isEmpty()) {
            log.warn("[mapping-Action] Hub→Provider not found: providerId={}, hubAction={} → fallback={}",
                     providerId, hubAction, fallback);
        }
        return result.orElse(fallback);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EinvMappingActionDto> findAllByProvider(String providerId) {
        return mapper.toDtoList(repository.findByProviderId(providerId));
    }

    @Override
    @Transactional
    public EinvMappingActionDto create(EinvMappingActionDto dto) {
        if (repository.existsByProviderIdAndHubAction(dto.getProviderId(), dto.getHubAction())) {
            throw new BusinessException(
                String.format("einv.error.mapping_action_duplicate: providerId=%s, hubAction=%s",
                              dto.getProviderId(), dto.getHubAction()));
        }
        EinvMappingActionEntity entity = mapper.toEntity(dto);
        repository.saveAndFlush(entity);
        log.info("[mapping-Action] Created: providerId={}, hubAction={} → cmd={}",
                 dto.getProviderId(), dto.getHubAction(), dto.getProviderCmd());
        return mapper.toDto(entity);
    }

    @Override
    @Transactional
    public EinvMappingActionDto update(Long id, EinvMappingActionDto dto) {
        EinvMappingActionEntity entity = repository.findById(id)
                .orElseThrow(() -> new BusinessException(
                    "einv.error.mapping_action_not_found: id=" + id));
        // Chỉ update providerCmd và description
        if (dto.getProviderCmd()  != null) entity.setProviderCmd(dto.getProviderCmd());
        if (dto.getDescription()  != null) entity.setDescription(dto.getDescription());
        repository.saveAndFlush(entity);
        log.info("[mapping-Action] Updated id={}", id);
        return mapper.toDto(entity);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.findById(id).ifPresent(e -> {
            repository.delete(e);
            log.info("[mapping-Action] Deleted id={}", id);
        });
    }
}
