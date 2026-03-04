package com.einvoicehub.core.domain.repository;

import com.einvoicehub.core.domain.entity.EinvInvoiceEntity;
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
public interface EinvInvoiceRepository extends JpaRepository<EinvInvoiceEntity, Long>,
        JpaSpecificationExecutor<EinvInvoiceEntity> {

    Optional<EinvInvoiceEntity> findByInvoiceLookupCode(String invoiceLookupCode);

    Optional<EinvInvoiceEntity> findByInvoiceNo(String invoiceNo);

    @Override
    @EntityGraph(attributePaths = {"details", "payload"})
    Optional<EinvInvoiceEntity> findById(Long id);

    @EntityGraph(attributePaths = {"details"})
    Optional<EinvInvoiceEntity> findByPartnerInvoiceId(String partnerInvoiceId);

    @EntityGraph(attributePaths = "details")
    Optional<EinvInvoiceEntity> findWithDetailsById(String id);



    @Query("SELECT i FROM EinvInvoiceEntity i WHERE " +
            "(:tenantId IS NULL OR i.tenantId = :tenantId) AND " +
            "(:search IS NULL OR " +
            "    LOWER(CONCAT(COALESCE(i.invoiceForm,''), COALESCE(i.invoiceSeries,''))) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "    LOWER(i.invoiceNo) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "    LOWER(i.BuyerName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "    LOWER(i.buyerTaxCode) LIKE LOWER(CONCAT('%', :search, '%'))" +
            ") " +
            "AND (:statusId IS NULL OR i.statusId = :statusId)")

    Page<EinvInvoiceEntity> findByFilters(
            @Param("tenantId") Long tenantId,
            @Param("search") String search,
            @Param("statusId") Integer statusId,
            Pageable pageable
    );

    @Query(value = "SELECT * FROM einv_invoices " +
            "WHERE tenant_id = :tenantId AND status_id = 2 " + // 2: Đã phát hành
            "ORDER BY invoice_date DESC, created_at DESC LIMIT 1",
            nativeQuery = true)
    Optional<EinvInvoiceEntity> findLatestPublishedInvoice(@Param("tenantId") Long tenantId);

    boolean existsByStoreIdAndPartnerInvoiceId(String storeId, String partnerInvoiceId);

    boolean existsByInvoiceLookupCode(String invoiceLookupCode);

    boolean existsByStatusId(Integer statusId);
}