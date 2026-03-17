package vn.softz.app.einvoicehub.service.mapping.strategy;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.softz.app.einvoicehub.domain.entity.EinvMappingPaymentMethodEntity;
import vn.softz.app.einvoicehub.domain.repository.EinvMappingPaymentMethodRepository;
import vn.softz.app.einvoicehub.service.mapping.MappingStrategy;

import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PaymentMethodMappingStrategy implements MappingStrategy {

    private final EinvMappingPaymentMethodRepository repository;

    @Override
    public Map<String, String> loadMappings(String lid, String providerId) {
        return repository.findAllByProviderIdAndInactiveFalse(providerId)
            .stream()
            .filter(entity -> entity.getProviderPaymentMethodId() != null)
            .collect(Collectors.toMap(
                entity -> String.valueOf(entity.getPaymentMethodId()),
                entity -> entity.getProviderPaymentMethodId()
            ));
    }

    @Override
    public String getMappingType() {
        return "PAYMENT_METHOD";
    }
}
