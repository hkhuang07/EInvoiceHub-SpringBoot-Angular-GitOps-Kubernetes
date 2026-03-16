package vn.softz.app.einvoicehub.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softz.app.einvoicehub.domain.entity.EinvTaxStatusEntity;
import vn.softz.app.einvoicehub.domain.repository.EinvTaxStatusRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
class EinvTaxStatusServiceImpl {

    private final EinvTaxStatusRepository repository;

    @Transactional(readOnly = true)
    public List<EinvTaxStatusEntity> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<EinvTaxStatusEntity> findById(Byte id) {
        return repository.findById(id);
    }
}
