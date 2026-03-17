package vn.softz.app.einvoicehub.service.mapping;

import vn.softz.app.einvoicehub.dto.EinvMappingItemTypeDto;

public interface EinvMappingItemTypeService
        extends vn.softz.app.einvoicehub.service.mapping.MappingService<EinvMappingItemTypeDto, Byte> {

    boolean existsByProviderAndHubItemType(String providerId, Byte itemTypeId);
}
