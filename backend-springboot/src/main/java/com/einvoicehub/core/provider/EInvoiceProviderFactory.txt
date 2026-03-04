package com.einvoicehub.core.provider;

import com.einvoicehub.core.exception.ErrorCode;
import com.einvoicehub.core.exception.InvalidDataException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EInvoiceProviderFactory {

    private final List<EInvoiceProvider> providers;
    private Map<String, EInvoiceProvider> providerMap;

    @PostConstruct
    public void init() {
        providerMap = providers.stream()
                .collect(Collectors.toMap(
                        provider -> provider.getProviderType().toUpperCase(),
                        Function.identity()
                ));
        log.info("[Factory] Đã đăng ký {} nhà cung cấp hóa đơn: {}",
                providerMap.size(), providerMap.keySet());
    }

    public EInvoiceProvider getProvider(String providerCode) {
        if (providerCode == null) {
            throw new InvalidDataException(ErrorCode.INVALID_DATA, "Mã nhà cung cấp không được để trống");
        }

        return Optional.ofNullable(providerMap.get(providerCode.toUpperCase()))
                .orElseThrow(() -> {
                    log.error("[Factory] Không tìm thấy Adapter cho nhà cung cấp: {}", providerCode);
                    return new InvalidDataException(ErrorCode.INVALID_DATA,
                            "Hệ thống hiện chưa hỗ trợ tích hợp với nhà cung cấp: " + providerCode);
                });
    }
}