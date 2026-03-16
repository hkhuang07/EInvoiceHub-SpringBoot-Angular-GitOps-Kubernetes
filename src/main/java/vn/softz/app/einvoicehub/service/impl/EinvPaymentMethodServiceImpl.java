package vn.softz.app.einvoicehub.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softz.app.einvoicehub.domain.entity.EinvPaymentMethodEntity;
import vn.softz.app.einvoicehub.domain.repository.EinvPaymentMethodRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
class EinvPaymentMethodServiceImpl {

    private final EinvPaymentMethodRepository repository;

    @Transactional(readOnly = true)
    public List<EinvPaymentMethodEntity> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<EinvPaymentMethodEntity> findById(Byte id) {
        return repository.findById(id);
    }
}
