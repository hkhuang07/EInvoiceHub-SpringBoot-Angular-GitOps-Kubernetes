package vn.softz.app.einvoicehub.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softz.app.einvoicehub.domain.entity.EinvItemTypeEntity;
import vn.softz.app.einvoicehub.domain.repository.EinvItemTypeRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
class EinvItemTypeServiceImpl {

    private final EinvItemTypeRepository repository;

    @Transactional(readOnly = true)
    public List<EinvItemTypeEntity> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<EinvItemTypeEntity> findById(Byte id) {
        return repository.findById(id);
    }
}
