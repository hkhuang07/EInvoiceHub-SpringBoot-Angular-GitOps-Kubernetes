package vn.softz.app.einvoicehub.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softz.app.einvoicehub.domain.entity.*;
import vn.softz.app.einvoicehub.domain.repository.*;
import vn.softz.core.exception.BusinessException;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
class EinvUnitServiceImpl {

    private final EinvUnitRepository repository;

    @Transactional(readOnly = true)
    public List<EinvUnitEntity> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<EinvUnitEntity> findByCode(String code) {
        return repository.findByCode(code);
    }

    @Transactional
    public EinvUnitEntity create(String code, String name) {
        if (repository.existsByCode(code)) {
            throw new BusinessException("einv.error.unit_code_exists: " + code);
        }
        EinvUnitEntity entity = new EinvUnitEntity();
        entity.setCode(code);
        entity.setName(name);
        EinvUnitEntity saved = repository.save(entity);
        log.info("[EinvUnit] Created code={}", code);
        return saved;
    }

    @Transactional
    public EinvUnitEntity update(String code, String name) {
        EinvUnitEntity entity = repository.findByCode(code)
                .orElseThrow(() -> new BusinessException("einv.error.unit_not_found: " + code));
        entity.setName(name);
        return repository.save(entity);
    }

    @Transactional
    public void delete(String code) {
        // Hard-delete. TODO: guard FK từ einv_mapping_unit và einv_invoices_detail.unit
        repository.findByCode(code).ifPresent(repository::delete);
        log.info("[EinvUnit] Deleted code={}", code);
    }

    @Transactional(readOnly = true)
    public boolean existsByCode(String code) {
        return repository.existsByCode(code);
    }
}

