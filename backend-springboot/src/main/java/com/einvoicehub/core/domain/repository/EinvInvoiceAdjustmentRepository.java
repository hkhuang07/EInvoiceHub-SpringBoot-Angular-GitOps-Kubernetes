package com.einvoicehub.core.domain.repository;

import com.einvoicehub.core.domain.entity.EinvInvoiceAdjustmentsEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EinvInvoiceAdjustmentRepository extends JpaRepository<EinvInvoiceAdjustmentsEntity, Long>,
        JpaSpecificationExecutor<EinvInvoiceAdjustmentsEntity> {

    @EntityGraph(attributePaths = {"originalInvoice"})
    Optional<EinvInvoiceAdjustmentsEntity> findByOriginalInvoiceId(Long originalInvoiceId);

    @Query("SELECT a FROM EinvInvoiceAdjustmentsEntity a WHERE " +
            "(:merchantId IS NULL OR a.originalInvoice.merchant.id = :merchantId) AND " +
            "(:search IS NULL OR " +
            "    LOWER(a.agreementNumber) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "    LOWER(a.reasonDescription) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "    LOWER(a.originalInvoice.invoiceNumber) LIKE LOWER(CONCAT('%', :search, '%'))" +
            ") " +
            "AND (:status IS NULL OR a.status = :status)")
    Page<EinvInvoiceAdjustmentsEntity> findByFilters(
            @Param("merchantId") Long merchantId,
            @Param("search") String search,
            @Param("status") String status,
            Pageable pageable
    );

    @Query(value = "SELECT a.* FROM invoice_adjustments a " +
            "JOIN invoice_metadata m ON a.original_invoice_id = m.id " +
            "WHERE m.merchant_id = :merchantId " +
            "ORDER BY a.created_at DESC LIMIT 1", nativeQuery = true)
    Optional<EinvInvoiceAdjustmentsEntity> findLatestAdjustment(@Param("merchantId") Long merchantId);
}