package com.einvoicehub.adapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Provider Factory - Factory Pattern cho Invoice Providers
 * 
 * Quản lý và cung cấp các adapter instances dựa trên provider code
 */
@Component
public class InvoiceProviderFactory {

    private static final Logger logger = LoggerFactory.getLogger(InvoiceProviderFactory.class);

    private final Map<String, InvoiceProvider> providers = new HashMap<>();

    public InvoiceProviderFactory(List<InvoiceProvider> providerList) {
        // Đăng ký tất cả các providers
        for (InvoiceProvider provider : providerList) {
            registerProvider(provider);
        }
        logger.info("Registered {} invoice providers: {}", providers.size(), providers.keySet());
    }

    /**
     * Đăng ký một provider
     */
    public void registerProvider(InvoiceProvider provider) {
        String code = provider.getProviderCode().toUpperCase();
        if (providers.containsKey(code)) {
            logger.warn("Provider {} already registered, overwriting", code);
        }
        providers.put(code, provider);
        logger.debug("Registered provider: {} - {}", code, provider.getProviderName());
    }

    /**
     * Lấy provider theo code
     * 
     * @param providerCode Mã provider (VNPT, VIETTEL, BKAV, MISA)
     * @return InvoiceProvider instance hoặc null nếu không tìm thấy
     */
    public InvoiceProvider getProvider(String providerCode) {
        if (providerCode == null || providerCode.isBlank()) {
            logger.warn("Provider code is null or empty");
            return null;
        }
        
        String code = providerCode.toUpperCase().trim();
        InvoiceProvider provider = providers.get(code);
        
        if (provider == null) {
            logger.warn("Provider not found for code: {}", providerCode);
        } else {
            logger.debug("Found provider: {} for code: {}", provider.getProviderName(), code);
        }
        
        return provider;
    }

    /**
     * Lấy provider với error handling
     * 
     * @param providerCode Mã provider
     * @return Optional chứa provider
     */
    public Optional<InvoiceProvider> getProviderOptional(String providerCode) {
        return Optional.ofNullable(getProvider(providerCode));
    }

    /**
     * Kiểm tra provider có được hỗ trợ không
     */
    public boolean isProviderSupported(String providerCode) {
        return providerCode != null && providers.containsKey(providerCode.toUpperCase().trim());
    }

    /**
     * Lấy danh sách providers được hỗ trợ
     */
    public Map<String, String> getSupportedProviders() {
        Map<String, String> supported = new HashMap<>();
        providers.forEach((code, provider) -> supported.put(code, provider.getProviderName()));
        return supported;
    }

    /**
     * Lấy số lượng providers đã đăng ký
     */
    public int getProviderCount() {
        return providers.size();
    }

    /**
     * Lấy tất cả providers
     */
    public Map<String, InvoiceProvider> getAllProviders() {
        return new HashMap<>(providers);
    }
}