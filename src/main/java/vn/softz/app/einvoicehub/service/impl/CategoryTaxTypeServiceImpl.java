package vn.softz.app.einvoicehub.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softz.app.einvoicehub.domain.entity.CategoryTaxType;
import vn.softz.app.einvoicehub.domain.repository.CategoryTaxTypeRepository;
import vn.softz.app.einvoicehub.service.catalog.CategoryTaxTypeService;
import vn.softz.core.exception.BusinessException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryTaxTypeServiceImpl implements CategoryTaxTypeService {

    private final CategoryTaxTypeRepository repository;

    private static final List<BigDecimal> ALLOWED_VAT_RATES = List.of(
        BigDecimal.ZERO,
        new BigDecimal("3.5"),
        new BigDecimal("5"),
        new BigDecimal("7"),
        new BigDecimal("8"),
        new BigDecimal("10")
    );

    @Override
    @Transactional
    public CategoryTaxType create(CategoryTaxType entity) {
        // Validate trùng tên
        if (repository.findByTaxName(entity.getTaxName()).isPresent()) {
            throw new BusinessException(
                "einv.error.tax_type_name_exists: " + entity.getTaxName());
        }
        // Validate tỷ lệ VAT hợp lệ
        if (entity.getVat() != null && !isValidVat(entity.getVat())) {
            throw new BusinessException(
                "einv.error.invalid_vat_rate: " + entity.getVat());
        }
        CategoryTaxType saved = repository.save(entity);
        log.info("[CategoryTaxType] Created id={}, name={}", saved.getId(), saved.getTaxName());
        return saved;
    }

    @Override
    @Transactional
    public CategoryTaxType update(String id, CategoryTaxType entity) {
        CategoryTaxType existing = findEntityById(id);
        // Validate tên nếu đổi
        if (entity.getTaxName() != null
                && !entity.getTaxName().equals(existing.getTaxName())
                && repository.findByTaxName(entity.getTaxName()).isPresent()) {
            throw new BusinessException(
                "einv.error.tax_type_name_exists: " + entity.getTaxName());
        }
        if (entity.getVat() != null && !isValidVat(entity.getVat())) {
            throw new BusinessException(
                "einv.error.invalid_vat_rate: " + entity.getVat());
        }
        // Patch update (null-safe)
        if (entity.getTaxName()   != null) existing.setTaxName(entity.getTaxName());
        if (entity.getTaxNameEn() != null) existing.setTaxNameEn(entity.getTaxNameEn());
        if (entity.getDescription()!= null) existing.setDescription(entity.getDescription());
        if (entity.getVat()       != null) existing.setVat(entity.getVat());

        CategoryTaxType saved = repository.save(existing);
        log.info("[CategoryTaxType] Updated id={}", id);
        return saved;
    }

    @Override
    @Transactional
    public void delete(String id) {
        // Hard-delete (không có field inactive)
        // TODO: Kiểm tra FK tham chiếu từ einv_mapping_tax_type và einv_invoices_detail
        // trước khi xóa để tránh ConstraintViolationException từ DB.
        CategoryTaxType entity = findEntityById(id);
        repository.delete(entity);
        log.info("[CategoryTaxType] Deleted id={}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CategoryTaxType> findById(String id) {
        return repository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryTaxType> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CategoryTaxType> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CategoryTaxType> findByTaxName(String taxName) {
        return repository.findByTaxName(taxName);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CategoryTaxType> findByVat(BigDecimal vat) {
        return repository.findByVat(vat);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryTaxType> findAllWithVat() {
        return repository.findAll().stream()
                         .filter(t -> t.getVat() != null)
                         .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByTaxName(String taxName) {
        return repository.findByTaxName(taxName).isPresent();
    }

    // ── Private ───────────────────────────────────────────────────────────────

    private CategoryTaxType findEntityById(String id) {
        return repository.findById(id)
                         .orElseThrow(() -> new BusinessException(
                             "einv.error.tax_type_not_found: " + id));
    }

    private boolean isValidVat(BigDecimal vat) {
        return ALLOWED_VAT_RATES.stream()
                                .anyMatch(r -> r.compareTo(vat) == 0);
    }
}
