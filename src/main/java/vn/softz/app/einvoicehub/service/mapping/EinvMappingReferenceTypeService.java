package vn.softz.app.einvoicehub.service.mapping;

import vn.softz.app.einvoicehub.dto.EinvMappingReferenceTypeDto;


public interface EinvMappingReferenceTypeService
        extends vn.softz.app.einvoicehub.service.mapping.MappingService<EinvMappingReferenceTypeDto, Byte> {

    boolean existsByProviderAndHubReferenceType(String providerId, Byte referenceTypeId);
}
