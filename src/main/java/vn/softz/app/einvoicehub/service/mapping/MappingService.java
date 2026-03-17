package vn.softz.app.einvoicehub.service.mapping;

import java.util.List;
import java.util.Optional;


public interface MappingService<D, HID> {

    D create(D dto);

    D update(String id, D dto);

    void delete(String id);

    Optional<D> findById(String id);

    List<D> findAllByProvider(String providerId);

    List<D> findActiveByProvider(String providerId);

    Optional<String> findProviderCode(String providerId, HID hubId);

    Optional<HID> findHubId(String providerId, String providerCode);

    String findProviderCodeOrDefault(String providerId, HID hubId, String fallback);

    HID findHubIdOrDefault(String providerId, String providerCode, HID fallback);
}
