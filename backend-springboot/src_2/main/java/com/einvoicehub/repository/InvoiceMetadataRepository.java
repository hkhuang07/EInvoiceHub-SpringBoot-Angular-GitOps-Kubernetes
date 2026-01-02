package com.einvoicehub.repository;

import com.einvoicehub.entity.InvoiceMetadata;
import com.einvoicehub.entity.InvoiceStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Invoice Metadata Repository
 * 
 * Repository cho thao tác với bảng invoice_metadata
 */
@Repository
public interface InvoiceMetadataRepository extends JpaRepository<InvoiceMetadata, Long> {

    /**
     * Tìm theo client request ID
     */
    Optional<InvoiceMetadata> findByClientRequestId(String clientRequestId);

    /**
     * Tìm theo provider transaction ID
     */
    Optional<InvoiceMetadata> findByProviderTransactionId(String providerTransactionId);

    /**
     * Tìm theo số hóa đơn
     */
    Optional<InvoiceMetadata> findByInvoiceNumber(String invoiceNumber);

    /**
     * Tìm tất cả hóa đơn của một merchant
     */
    Page<InvoiceMetadata> findByMerchantId(Long merchantId, Pageable pageable);

    /**
     * Tìm hóa đơn theo merchant và trạng thái
     */
    Page<InvoiceMetadata> findByMerchantIdAndStatus(Long merchantId, InvoiceStatus status, Pageable pageable);

    /**
     * Tìm hóa đơn theo merchant và provider code
     */
    Page<InvoiceMetadata> findByMerchantIdAndProviderCode(Long merchantId, String providerCode, Pageable pageable);

    /**
     * Tìm hóa đơn theo merchant trong khoảng thời gian
     */
    @Query("SELECT im FROM InvoiceMetadata im WHERE im.merchantId = :merchantId " +
           "AND im.createdAt BETWEEN :startDate AND :endDate ORDER BY im.createdAt DESC")
    List<InvoiceMetadata> findByMerchantIdAndDateRange(
        @Param("merchantId") Long merchantId,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
    );

    /**
     * Đếm số hóa đơn theo trạng thái của một merchant
     */
    long countByMerchantIdAndStatus(Long merchantId, InvoiceStatus status);

    /**
     * Đếm tổng số hóa đơn của một merchant
     */
    long countByMerchantId(Long merchantId);

    /**
     * Tìm hóa đơn cần retry (failed và chưa vượt quá số lần retry tối đa)
     */
    @Query("SELECT im FROM InvoiceMetadata im WHERE im.status = 'FAILED' " +
           "AND im.retryCount < :maxRetries ORDER BY im.updatedAt ASC")
    List<InvoiceMetadata> findInvoicesForRetry(@Param("maxRetries") int maxRetries, Pageable pageable);

    /**
     * Tìm hóa đơn theo invoice pattern và serial
     */
    Optional<InvoiceMetadata> findByInvoicePatternAndInvoiceSerial(String invoicePattern, String invoiceSerial);

    /**
     * Kiểm tra client request ID tồn tại
     */
    boolean existsByClientRequestId(String clientRequestId);

    /**
     * Tìm hóa đơn mới nhất của một merchant
     */
    Optional<InvoiceMetadata> findFirstByMerchantIdOrderByCreatedAtDesc(Long merchantId);

    /**
     * Thống kê số lượng hóa đơn theo trạng thái
     */
    @Query("SELECT im.status, COUNT(im) FROM InvoiceMetadata im " +
           "WHERE im.merchantId = :merchantId " +
           "AND im.createdAt >= :fromDate " +
           "GROUP BY im.status")
    List<Object[]> countByStatusAndMerchantId(
        @Param("merchantId") Long merchantId,
        @Param("fromDate") LocalDateTime fromDate
    );

    /**
     * Tìm hóa đơn theo buyer tax code
     */
    Page<InvoiceMetadata> findByMerchantIdAndBuyerTaxCodeContaining(
        Long merchantId, String buyerTaxCode, Pageable pageable
    );
}