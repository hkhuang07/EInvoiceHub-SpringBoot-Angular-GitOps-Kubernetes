package com.einvoicehub.core.domain.repository;

import com.einvoicehub.core.domain.entity.MerchantEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MerchantRepository extends JpaRepository<MerchantEntity, Long>,
        JpaSpecificationExecutor<MerchantEntity> {

    Optional<MerchantEntity> findByTenantId(String tenantId);
    Optional<MerchantEntity> findByTaxCode(String taxCode);

    boolean existsByTaxCodeAndIsDeletedFalse(String taxCode);

    @Query("SELECT m FROM EinvMerchantEntity m WHERE " +
            "(:search IS NULL OR LOWER(m.companyName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(m.taxCode) LIKE LOWER(CONCAT('%', :search, '%'))) " +
            "AND m.isDeleted = false")
    Page<MerchantEntity> searchMerchants(@Param("search") String search, Pageable pageable);

    @Query(value = "SELECT * FROM merchants WHERE is_deleted = 0 ORDER BY created_at DESC LIMIT 1",
            nativeQuery = true)
    Optional<MerchantEntity> findLatestMerchant();
}