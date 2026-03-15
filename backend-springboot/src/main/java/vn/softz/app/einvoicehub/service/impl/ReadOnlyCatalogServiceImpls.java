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

// ============================================================================
// FILE: ReadOnlyCatalogServiceImpls.java
// Gom các catalog service đơn giản (read-only hoặc CRUD nhỏ) vào một file
// để giảm số lượng file. Tách ra nếu cần bổ sung logic phức tạp sau này.
// ============================================================================

// ── 1. EinvInvoiceStatusServiceImpl ─────────────────────────────────────────

// ── 2. EinvInvoiceTypeServiceImpl ────────────────────────────────────────────

// ── 3. EinvTaxStatusServiceImpl ──────────────────────────────────────────────

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

// ── 4. EinvPaymentMethodServiceImpl ─────────────────────────────────────────

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

// ── 5. EinvUnitServiceImpl ───────────────────────────────────────────────────

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

// ── 6. EinvItemTypeServiceImpl ───────────────────────────────────────────────

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

// ── 7. EinvReceiveTypeServiceImpl ────────────────────────────────────────────

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

// ── 8. EinvReferenceTypeServiceImpl ─────────────────────────────────────────

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

// ── 9. EinvSubmitInvoiceTypeServiceImpl ──────────────────────────────────────

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
