package vn.softz.app.einvoicehub.service.mapping.strategy;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.softz.app.einvoicehub.domain.entity.EinvMappingReferenceTypeEntity;
import vn.softz.app.einvoicehub.domain.repository.EinvMappingReferenceTypeRepository;
import vn.softz.app.einvoicehub.service.mapping.MappingStrategy;

import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ReferenceTypeMappingStrategy implements MappingStrategy {

    private final EinvMappingReferenceTypeRepository repository;

    @Override
    public Map<String, String> loadMappings(String lid, String providerId) {
        return repository.findAllByProviderIdAndInactiveFalse(providerId)
            .stream()
            .filter(entity -> entity.getProviderReferenceTypeId() != null)
            .collect(Collectors.toMap(
                entity -> String.valueOf(entity.getReferenceTypeId()),
                entity -> entity.getProviderReferenceTypeId()
            ));
    }

    @Override
    public String getMappingType() {
        return "REFERENCE_TYPE";
    }
}
