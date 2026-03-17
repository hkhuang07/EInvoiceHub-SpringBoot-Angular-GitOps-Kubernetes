package vn.softz.app.einvoicehub.service.mapping;

import vn.softz.app.einvoicehub.dto.EinvMappingTaxTypeDto;

import java.math.BigDecimal;
import java.util.Optional;

public interface EinvMappingTaxTypeService
        extends vn.softz.app.einvoicehub.service.mapping.MappingService<EinvMappingTaxTypeDto, String> {

    Optional<String> findProviderTaxCodeByRate(String providerId, BigDecimal vatRate);

    boolean existsByProviderAndHubTaxType(String providerId, String taxTypeId);
}
