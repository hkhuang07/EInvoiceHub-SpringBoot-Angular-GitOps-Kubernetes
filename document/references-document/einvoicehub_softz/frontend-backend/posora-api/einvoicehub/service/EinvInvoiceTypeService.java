package vn.softz.app.einvoicehub.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softz.app.einvoicehub.domain.entity.EinvInvoiceTypeEntity;
import vn.softz.app.einvoicehub.domain.repository.EinvInvoiceTypeRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EinvInvoiceTypeService {

    private final EinvInvoiceTypeRepository repository;

    @Transactional(readOnly = true)
    public List<EinvInvoiceTypeEntity> getAll() {
        return repository.findAll();
    }
}
