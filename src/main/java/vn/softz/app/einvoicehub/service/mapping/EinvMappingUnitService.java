package vn.softz.app.einvoicehub.service.mapping;

import vn.softz.app.einvoicehub.dto.EinvMappingUnitDto;

public interface EinvMappingUnitService
        extends vn.softz.app.einvoicehub.service.mapping.MappingService<EinvMappingUnitDto, String> {

    boolean existsByProviderAndHubUnit(String providerId, String unitCode);
}
