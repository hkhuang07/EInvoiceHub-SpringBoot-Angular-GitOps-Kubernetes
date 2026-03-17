package vn.softz.app.einvoicehub.service.mapping.strategy;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.softz.app.einvoicehub.domain.entity.EinvMappingTaxTypeEntity;
import vn.softz.app.einvoicehub.domain.repository.EinvMappingTaxTypeRepository;
import vn.softz.app.einvoicehub.service.mapping.MappingStrategy;

import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TaxTypeMappingStrategy implements MappingStrategy {

    private final EinvMappingTaxTypeRepository repository;

    @Override
    public Map<String, String> loadMappings(String lid, String providerId) {
        return repository.findAllByProviderIdAndInactiveFalse(providerId)
            .stream()
            .filter(entity -> entity.getProviderTaxTypeId() != null)
            .collect(Collectors.toMap(
                entity -> entity.getTaxTypeId(),
                entity -> formatProviderValue(entity.getProviderTaxTypeId(), entity.getProviderTaxRate())
            ));
    }

    @Override
    public String getMappingType() {
        return "TAX_TYPE";
    }

    @Override
    public String formatProviderValue(String providerCode, String additionalValue) {
        if (providerCode == null) {
            return null;
        }
        if (additionalValue != null && !additionalValue.isEmpty()) {
            return providerCode + "," + additionalValue;
        }
        return providerCode;
    }
}
