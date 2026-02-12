package com.einvoicehub.core.domain.repository;

import com.einvoicehub.core.domain.entity.ProviderTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ProviderTransactionRepository extends JpaRepository<ProviderTransaction, Long> {

    Page<ProviderTransaction> findByMerchantIdOrderByTimestampDesc(Long merchantId, Pageable pageable);

    List<ProviderTransaction> findByInvoiceMetadataIdOrderByTimestampDesc(Long invoiceMetadataId);

    List<ProviderTransaction> findByMerchantIdAndTimestampBetween(
            Long merchantId, LocalDateTime fromDate, LocalDateTime toDate);

    List<ProviderTransaction> findByStatus(ProviderTransaction.TransactionStatus status);

    @Query("SELECT pt FROM ProviderTransaction pt WHERE pt.merchantId = :merchantId " +
            "AND pt.status = :status AND pt.timestamp < :before")
    List<ProviderTransaction> findFailedTransactionsOlderThan(
            @Param("merchantId") Long merchantId,
            @Param("status") ProviderTransaction.TransactionStatus status,
            @Param("before") LocalDateTime before);

    List<ProviderTransaction> findByInvoiceMetadataIdAndTransactionType(
            Long invoiceMetadataId,
            ProviderTransaction.TransactionType transactionType);

    long countByMerchantIdAndStatus(Long merchantId, ProviderTransaction.TransactionStatus status);

    @Query("SELECT pt FROM ProviderTransaction pt WHERE pt.merchantId = :merchantId " +
            "ORDER BY pt.timestamp DESC")
    List<ProviderTransaction> findRecentByMerchant(@Param("merchantId") Long merchantId);

    List<ProviderTransaction> findByTransactionId(String transactionId);

    List<ProviderTransaction> findByProviderCodeAndStatus(
            String providerCode,
            ProviderTransaction.TransactionStatus status);

    long countByStatus(ProviderTransaction.TransactionStatus status);

    @Query("SELECT pt FROM ProviderTransaction pt WHERE pt.status = 'RETRYING' " +
            "AND pt.nextRetryAt <= :now ORDER BY pt.nextRetryAt ASC")
    List<ProviderTransaction> findTransactionsForRetry(@Param("now") LocalDateTime now);

    @Query("SELECT pt.status, COUNT(pt) FROM ProviderTransaction pt " +
            "WHERE pt.merchantId = :merchantId GROUP BY pt.status")
    List<Object[]> getTransactionStatsByMerchant(@Param("merchantId") Long merchantId);
}
