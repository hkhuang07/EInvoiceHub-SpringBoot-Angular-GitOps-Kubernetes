package vn.softz.app.einvoicehub.service.mapping.strategy;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.softz.app.einvoicehub.domain.repository.EinvMappingItemTypeRepository;

import vn.softz.app.einvoicehub.service.mapping.MappingStrategy;

import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ItemTypeMappingStrategy implements MappingStrategy {

    private final EinvMappingItemTypeRepository repository;

    @Override
    public Map<String, String> loadMappings(String lid, String providerId) {
        return repository.findAllByProviderIdAndInactiveFalse(providerId)
            .stream()
            .filter(entity -> entity.getProviderItemTypeId() != null)
            .collect(Collectors.toMap(
                entity -> String.valueOf(entity.getItemTypeId()),
                entity -> entity.getProviderItemTypeId()
            ));
    }

    @Override
    public String getMappingType() {
        return "ITEM_TYPE";
    }
}
