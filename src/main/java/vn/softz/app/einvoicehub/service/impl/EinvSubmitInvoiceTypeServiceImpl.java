package vn.softz.app.einvoicehub.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softz.app.einvoicehub.domain.entity.EinvSubmitInvoiceTypeEntity;
import vn.softz.app.einvoicehub.domain.repository.EinvSubmitInvoiceTypeRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
class EinvSubmitInvoiceTypeServiceImpl {

    private final EinvSubmitInvoiceTypeRepository repository;

    @Transactional(readOnly = true)
    public List<EinvSubmitInvoiceTypeEntity> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<EinvSubmitInvoiceTypeEntity> findById(String id) {
        return repository.findById(id);
    }
}
