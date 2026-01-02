package com.einvoicehub.core.repository.mongodb;

import com.einvoicehub.core.entity.mongodb.InvoicePayload;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface InvoicePayloadRepository extends MongoRepository<InvoicePayload, String> {

    Page<InvoicePayload> findByMerchantIdOrderByCreatedAtDesc(Long merchantId, Pageable pageable);

    List<InvoicePayload> findByMerchantId(Long merchantId);

    Optional<InvoicePayload> findByClientRequestIdAndMerchantId(String clientRequestId, Long merchantId);

    @Query("{ 'merchantId': ?0, 'createdAt': { $gte: ?1, $lte: ?2 } }")
    List<InvoicePayload> findByMerchantIdAndCreatedAtBetween(
            Long merchantId, LocalDateTime fromDate, LocalDateTime toDate);

    @Query("{ 'merchantId': ?0, 'data.buyer.taxCode': ?1 }")
    List<InvoicePayload> findByMerchantIdAndBuyerTaxCode(Long merchantId, String buyerTaxCode);

    long countByMerchantId(Long merchantId);

    void deleteByMerchantId(Long merchantId);
}