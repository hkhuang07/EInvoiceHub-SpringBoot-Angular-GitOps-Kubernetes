package vn.softz.app.einvoicehub.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softz.app.einvoicehub.domain.entity.EinvReceiveTypeEntity;
import vn.softz.app.einvoicehub.domain.repository.EinvReceiveTypeRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
class EinvReceiveTypeServiceImpl {

    private final EinvReceiveTypeRepository repository;

    @Transactional(readOnly = true)
    public List<EinvReceiveTypeEntity> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<EinvReceiveTypeEntity> findById(Byte id) {
        return repository.findById(id);
    }
}
