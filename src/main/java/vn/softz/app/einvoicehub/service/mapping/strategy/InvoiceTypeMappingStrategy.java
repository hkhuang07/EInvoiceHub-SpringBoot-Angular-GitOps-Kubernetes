package vn.softz.app.einvoicehub.service.mapping.strategy;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.softz.app.einvoicehub.domain.repository.EinvMappingInvoiceTypeRepository;
import vn.softz.app.einvoicehub.service.mapping.MappingStrategy;

import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class InvoiceTypeMappingStrategy implements MappingStrategy {

    private final EinvMappingInvoiceTypeRepository repository;

    @Override
    public Map<String, String> loadMappings(String lid, String providerId) {
        return repository.findAllByProviderIdAndInactiveFalse(providerId)
            .stream()
            .filter(entity -> entity.getProviderInvoiceTypeId() != null)
            .collect(Collectors.toMap(
                entity -> String.valueOf(entity.getInvoiceTypeId()),
                entity -> entity.getProviderInvoiceTypeId()
            ));
    }

    @Override
    public String getMappingType() {
        return "INVOICE_TYPE";
    }
}
