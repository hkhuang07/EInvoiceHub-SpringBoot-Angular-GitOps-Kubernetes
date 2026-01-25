package com.einvoicehub.core.provider;

import com.einvoicehub.core.provider.exception.ProviderException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class ProviderFactory {
    private final Map<String, InvoiceProvider> providerMap;

    @Autowired
    public ProviderFactory(List<InvoiceProvider> providers) {
        this.providerMap = providers.stream()
                .collect(Collectors.toUnmodifiableMap(
                        p -> p.getProviderCode().toUpperCase(),
                        Function.identity()
                ));
    }

    public InvoiceProvider getProvider(String providerCode) {
        if (providerCode == null) throw new ProviderException("Provider code cannot be null", "SYSTEM", "NULL_CODE");

        InvoiceProvider provider = providerMap.get(providerCode.toUpperCase());
        if (provider == null) {
            throw new ProviderException("Unsupported provider: " + providerCode, providerCode, "PROVIDER_NOT_FOUND");
        }
        return provider;
    }

    public boolean hasProvider(String providerCode) {
        return providerCode != null && providerMap.containsKey(providerCode.toUpperCase());
    }

    public List<String> getAvailableProviders() {
        return providerMap.values().stream()
                .filter(InvoiceProvider::isAvailable)
                .map(InvoiceProvider::getProviderCode)
                .collect(Collectors.toList());
    }
}