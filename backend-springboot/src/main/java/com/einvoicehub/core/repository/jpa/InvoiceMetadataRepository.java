package com.einvoicehub.core.repository.jpa;

import com.einvoicehub.core.entity.jpa.InvoiceMetadata;
import com.einvoicehub.core.entity.enums.InvoiceStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository cho InvoiceMetadata entity
 */
@Repository
public interface InvoiceMetadataRepository
        extends JpaRepository<InvoiceMetadata, Long>, JpaSpecificationExecutor<InvoiceMetadata> {

    Page<InvoiceMetadata> findByMerchantIdAndIsDeletedFalse(Long merchantId, Pageable pageable);

    List<InvoiceMetadata> findByMerchantIdAndStatus(Long merchantId, InvoiceStatus status);

    Optional<InvoiceMetadata> findByIdAndIsDeletedFalse(Long id);

    Optional<InvoiceMetadata> findByMerchantIdAndClientRequestId(Long merchantId, String clientRequestId);

    Optional<InvoiceMetadata> findByInvoiceNumberAndIsDeletedFalse(String invoiceNumber);

    List<InvoiceMetadata> findByMerchantIdAndIssueDateBetween(
            Long merchantId, LocalDate fromDate, LocalDate toDate);

    List<InvoiceMetadata> findByMerchantIdAndCreatedAtBetween(
            Long merchantId, LocalDateTime fromDate, LocalDateTime toDate);

    @Query("SELECT i FROM InvoiceMetadata i WHERE i.merchant.id = :merchantId " +
            "AND i.status IN :statuses ORDER BY i.createdAt DESC")
    List<InvoiceMetadata> findByMerchantAndStatuses(
            @Param("merchantId") Long merchantId,
            @Param("statuses") List<InvoiceStatus> statuses);

    @Query("SELECT COUNT(i) FROM InvoiceMetadata i WHERE i.merchant.id = :merchantId " +
            "AND i.status = :status AND i.isDeleted = false")
    long countByMerchantAndStatus(
            @Param("merchantId") Long merchantId,
            @Param("status") InvoiceStatus status);

    @Query("SELECT COUNT(i) FROM InvoiceMetadata i WHERE i.merchant.id = :merchantId " +
            "AND i.isDeleted = false AND i.createdAt >= :fromDate")
    long countByMerchantSince(
            @Param("merchantId") Long merchantId,
            @Param("fromDate") LocalDateTime fromDate);

    @Query("SELECT i FROM InvoiceMetadata i WHERE i.merchant.id = :merchantId " +
            "AND i.buyerTaxCode = :taxCode AND i.isDeleted = false " +
            "ORDER BY i.createdAt DESC")
    Page<InvoiceMetadata> findByMerchantAndBuyerTaxCode(
            @Param("merchantId") Long merchantId,
            @Param("taxCode") String taxCode,
            Pageable pageable);

    @Query("SELECT i FROM InvoiceMetadata i WHERE i.merchant.id = :merchantId " +
            "AND (i.invoiceNumber LIKE %:keyword% OR i.buyerName LIKE %:keyword%) " +
            "AND i.isDeleted = false ORDER BY i.createdAt DESC")
    Page<InvoiceMetadata> searchByKeyword(
            @Param("merchantId") Long merchantId,
            @Param("keyword") String keyword,
            Pageable pageable);

    @Query("SELECT i FROM InvoiceMetadata i WHERE i.merchant.id = :merchantId " +
            "AND i.status = :status AND i.createdAt < :before")
    List<InvoiceMetadata> findPendingInvoicesOlderThan(
            @Param("merchantId") Long merchantId,
            @Param("status") InvoiceStatus status,
            @Param("before") LocalDateTime before);

    @Query("SELECT DISTINCT i.providerId FROM InvoiceMetadata i WHERE i.providerId IS NOT NULL")
    List<Long> findDistinctProviderIds();

    @Query("SELECT i FROM InvoiceMetadata i WHERE i.merchant.id = :merchantId " +
            "AND i.provider.id = :providerId ORDER BY i.createdAt DESC")
    List<InvoiceMetadata> findByMerchantAndProvider(
            @Param("merchantId") Long merchantId,
            @Param("providerId") Long providerId);
}