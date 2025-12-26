package com.einvoicehub.core.repository.mongodb;

import com.einvoicehub.core.entity.mongodb.ProviderTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository cho ProviderTransaction MongoDB collection
 */
@Repository
public interface ProviderTransactionRepository extends MongoRepository<ProviderTransaction, String> {

    Page<ProviderTransaction> findByMerchantIdOrderByTimestampDesc(Long merchantId, Pageable pageable);

    List<ProviderTransaction> findByInvoiceMetadataIdOrderByTimestampDesc(Long invoiceMetadataId);

    List<ProviderTransaction> findByMerchantIdAndTimestampBetween(
            Long merchantId, LocalDateTime fromDate, LocalDateTime toDate);

    List<ProviderTransaction> findByStatus(ProviderTransaction.TransactionStatus status);

    @Query("{ 'merchantId': ?0, 'status': ?1, 'timestamp': { $lt: ?2 } }")
    List<ProviderTransaction> findFailedTransactionsOlderThan(
            Long merchantId,
            ProviderTransaction.TransactionStatus status,
            LocalDateTime before);

    @Query("{ 'invoiceMetadataId': ?0, 'transactionType': ?1 }")
    List<ProviderTransaction> findByInvoiceMetadataIdAndTransactionType(
            Long invoiceMetadataId,
            ProviderTransaction.TransactionType transactionType);

    long countByMerchantIdAndStatus(Long merchantId, ProviderTransaction.TransactionStatus status);

    @Query(value = "{ 'merchantId': ?0 }", sort = "{ 'timestamp': -1 }")
    List<ProviderTransaction> findRecentByMerchant(Long merchantId);
}