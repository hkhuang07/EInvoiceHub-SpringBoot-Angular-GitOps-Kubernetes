package vn.softz.app.einvoicehub.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softz.app.einvoicehub.domain.entity.EinvInvoiceStatusEntity;
import vn.softz.app.einvoicehub.domain.repository.EinvInvoiceStatusRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
class EinvInvoiceStatusServiceImpl {

    private final EinvInvoiceStatusRepository repository;

    @Transactional(readOnly = true)
    public List<EinvInvoiceStatusEntity> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<EinvInvoiceStatusEntity> findById(Byte id) {
        return repository.findById(id);
    }
}
