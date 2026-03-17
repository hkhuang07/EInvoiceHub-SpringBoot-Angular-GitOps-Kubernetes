package vn.softz.app.einvoicehub.service.mapping;

import vn.softz.app.einvoicehub.dto.EinvMappingActionDto;

import java.util.List;
import java.util.Optional;


public interface EinvMappingActionService {

    Optional<String> findProviderCmd(String providerId, String hubAction);

    Optional<String> findHubAction(String providerId, String providerCmd);

    String findProviderCmdOrDefault(String providerId, String hubAction, String fallback);

    List<EinvMappingActionDto> findAllByProvider(String providerId);

    EinvMappingActionDto create(EinvMappingActionDto dto);

    EinvMappingActionDto update(Long id, EinvMappingActionDto dto);

    void delete(Long id);
}
