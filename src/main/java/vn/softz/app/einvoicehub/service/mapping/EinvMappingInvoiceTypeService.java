package vn.softz.app.einvoicehub.service.mapping;

import vn.softz.app.einvoicehub.dto.EinvMappingInvoiceTypeDto;

import java.util.List;

public interface EinvMappingInvoiceTypeService extends vn.softz.app.einvoicehub.service.mapping.MappingService<EinvMappingInvoiceTypeDto, Byte> {

    List<EinvMappingInvoiceTypeDto> findByHubInvoiceType(Byte invoiceTypeId);

    boolean existsByProviderAndHubType(String providerId, Byte invoiceTypeId);
}
