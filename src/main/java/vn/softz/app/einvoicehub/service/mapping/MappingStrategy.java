package vn.softz.app.einvoicehub.service.mapping;

import java.util.Map;

public interface MappingStrategy {

    Map<String, String> loadMappings(String lid, String providerId);

    String getMappingType();

    default String formatProviderValue(String providerCode, String additionalValue) {
        return providerCode;
    }
}
