package com.einvoicehub.server.domain.repository;

import com.einvoicehub.server.domain.entity.CategoryTaxType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository cho bảng category_tax_type - Danh mục Loại thuế
 */
@Repository
public interface CategoryTaxTypeRepository extends JpaRepository<CategoryTaxType, String>, JpaSpecificationExecutor<CategoryTaxType> {

    /**
     * Tìm loại thuế theo tên
     */
    Optional<CategoryTaxType> findByTaxName(String taxName);

    /**
     * Tìm loại thuế theo tên tiếng Anh
     */
    Optional<CategoryTaxType> findByTaxNameEn(String taxNameEn);

    /**
     * Tìm loại thuế theo VAT rate
     */
    Optional<CategoryTaxType> findByVat(java.math.BigDecimal vat);

    /**
     * Tìm kiếm nhiều ID
     */
    List<CategoryTaxType> findByIdIn(List<String> ids);

    /**
     * Tìm kiếm theo VAT rate (có thể null)
     */
    List<CategoryTaxType> findByVatIsNull();

    /**
     * Tìm kiếm theo VAT rate không null
     */
    List<CategoryTaxType> findByVatIsNotNull();

    /**
     * Kiểm tra tồn tại theo tên
     */
    boolean existsByTaxName(String taxName);

    /**
     * Kiểm tra tồn tại theo VAT rate
     */
    boolean existsByVat(java.math.BigDecimal vat);
}
