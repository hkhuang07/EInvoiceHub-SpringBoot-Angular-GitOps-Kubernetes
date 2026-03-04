package vn.softz.app.einvoicehub.provider;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EInvoiceProviderFactory {

    private final List<EInvoiceProvider> providers;
    private Map<String, EInvoiceProvider> providerMap;

    @PostConstruct
    public void init() {
        providerMap = providers.stream()
                .collect(Collectors.toMap(EInvoiceProvider::getProviderType, Function.identity()));
    }

    public EInvoiceProvider getProvider(String providerType) {
        return Optional.ofNullable(providerMap.get(providerType))
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy nhà cung cấp hóa đơn: " + providerType));
    }
}
