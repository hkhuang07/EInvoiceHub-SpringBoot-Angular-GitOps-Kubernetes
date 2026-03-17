package vn.softz.app.einvoicehub.service.mapping;

import vn.softz.app.einvoicehub.dto.EinvMappingPaymentMethodDto;

public interface EinvMappingPaymentMethodService
        extends vn.softz.app.einvoicehub.service.mapping.MappingService<EinvMappingPaymentMethodDto, Byte> {

    boolean existsByProviderAndHubMethod(String providerId, Byte paymentMethodId);
}
