package vn.softz.app.einvoicehub.service;

import vn.softz.app.einvoicehub.dto.EinvMappingInvoiceStatusDto;

import java.util.List;
import java.util.Optional;

public interface EinvMappingInvoiceStatusService {

    EinvMappingInvoiceStatusDto create(EinvMappingInvoiceStatusDto dto);

    EinvMappingInvoiceStatusDto update(String id, EinvMappingInvoiceStatusDto dto);

    void delete(String id);

    Optional<EinvMappingInvoiceStatusDto> findById(String id);

    List<EinvMappingInvoiceStatusDto> findAllByProvider(String providerId);

    List<EinvMappingInvoiceStatusDto> findActiveByProvider(String providerId);

    Optional<String> findProviderStatusId(String providerId, Byte hubStatusId);

    Optional<Byte> findHubStatusId(String providerId, String providerStatusId);

    String findProviderStatusIdOrDefault(String providerId, Byte hubStatusId, String fallback);

    Byte findHubStatusIdOrDefault(String providerId, String providerStatusId, Byte fallback);
}
