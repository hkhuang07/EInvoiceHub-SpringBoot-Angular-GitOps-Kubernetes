package vn.softz.app.einvoicehub.service.mapping.strategy;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
//import vn.softz.app.einvoicehub.domain.entity.mapping.EinvMappingInvoiceStatusEntity;
//import vn.softz.app.einvoicehub.domain.repository.mapping.EinvMappingInvoiceStatusRepository;
import vn.softz.app.einvoicehub.mapper.EinvMappingInvoiceStatusMapper;
import vn.softz.app.einvoicehub.domain.repository.EinvMappingInvoiceStatusRepository;

import vn.softz.app.einvoicehub.service.mapping.MappingStrategy;

import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class InvoiceStatusMappingStrategy implements MappingStrategy {

    private final EinvMappingInvoiceStatusRepository repository;

    @Override
    public Map<String, String> loadMappings(String lid, String providerId) {
        return repository.findAllByProviderIdAndInactiveFalse(providerId)
            .stream()
            .filter(entity -> entity.getProviderInvoiceStatusId() != null)
            .collect(Collectors.toMap(
                entity -> String.valueOf(entity.getInvoiceStatusId()),
                entity -> entity.getProviderInvoiceStatusId()
            ));
    }

    @Override
    public String getMappingType() {
        return "INVOICE_STATUS";
    }
}
