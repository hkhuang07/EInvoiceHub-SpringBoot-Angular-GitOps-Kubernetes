package com.einvoicehub.core.provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Factory để lấy adapter provider tương ứng
 */
@Component
public class ProviderFactory {

    private final Map<String, InvoiceProvider> providerMap;

    @Autowired
    public ProviderFactory(List<InvoiceProvider> providers) {
        this.providerMap = providers.stream()
                .collect(Collectors.toMap(
                        InvoiceProvider::getProviderCode,
                        Function.identity()
                ));
    }

    /**
     * Lấy provider theo mã
     * @param providerCode Mã provider (VNPT, VIETTEL...)
     * @return InvoiceProvider tương ứng
     * @throws ProviderException nếu không tìm thấy
     */
    public InvoiceProvider getProvider(String providerCode) {
        InvoiceProvider provider = providerMap.get(providerCode.toUpperCase());
        if (provider == null) {
            throw new ProviderException(
                    "Provider not found: " + providerCode,
                    providerCode,
                    "PROVIDER_NOT_FOUND"
            );
        }
        return provider;
    }

    /**
     * Kiểm tra provider có tồn tại không
     * @param providerCode Mã provider
     * @return true nếu provider có sẵn
     */
    public boolean hasProvider(String providerCode) {
        return providerMap.containsKey(providerCode.toUpperCase());
    }

    /**
     * Lấy danh sách provider khả dụng
     * @return Danh sách provider đang hoạt động
     */
    public List<String> getAvailableProviders() {
        return providerMap.values().stream()
                .filter(InvoiceProvider::isAvailable)
                .map(InvoiceProvider::getProviderCode)
                .collect(Collectors.toList());
    }
}