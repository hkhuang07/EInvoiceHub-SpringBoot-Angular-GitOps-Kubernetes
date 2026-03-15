package vn.softz.app.einvoicehub.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softz.app.einvoicehub.domain.entity.EinvInvoiceTypeEntity;
import vn.softz.app.einvoicehub.domain.repository.EinvInvoiceTypeRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
class EinvInvoiceTypeServiceImpl {

    private final EinvInvoiceTypeRepository repository;

    @Transactional(readOnly = true)
    public List<EinvInvoiceTypeEntity> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<EinvInvoiceTypeEntity> findById(Byte id) {
        return repository.findById(id);
    }

    /**
     * Lấy danh sách sắp xếp theo sortOrder (dùng cho dropdown UI).
     */
    @Transactional(readOnly = true)
    public List<EinvInvoiceTypeEntity> findAllSorted() {
        return repository.findAll()
                .stream()
                .sorted(java.util.Comparator.comparingInt(
                        e -> e.getSortOrder() != null ? e.getSortOrder() : 0))
                .toList();
    }
}
