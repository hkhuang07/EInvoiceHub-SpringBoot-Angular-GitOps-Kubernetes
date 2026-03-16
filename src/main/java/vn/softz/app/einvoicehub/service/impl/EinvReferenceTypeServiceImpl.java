package vn.softz.app.einvoicehub.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softz.app.einvoicehub.domain.entity.EinvReferenceTypeEntity;
import vn.softz.app.einvoicehub.domain.repository.EinvReferenceTypeRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
class EinvReferenceTypeServiceImpl {

    private final EinvReferenceTypeRepository repository;

    @Transactional(readOnly = true)
    public List<EinvReferenceTypeEntity> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<EinvReferenceTypeEntity> findById(Byte id) {
        return repository.findById(id);
    }
}
