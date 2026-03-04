package vn.softz.app.einvoicehub.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softz.app.einvoicehub.domain.entity.EinvInvoiceStatusEntity;
import vn.softz.app.einvoicehub.domain.repository.EinvInvoiceStatusRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EinvInvoiceStatusService {

    private final EinvInvoiceStatusRepository repository;

    @Transactional(readOnly = true)
    public List<EinvInvoiceStatusEntity> getAll() {
        return repository.findAll();
    }
}
