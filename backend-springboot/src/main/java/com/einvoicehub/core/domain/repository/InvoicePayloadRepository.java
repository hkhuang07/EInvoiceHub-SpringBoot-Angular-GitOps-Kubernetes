package com.einvoicehub.core.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface InvoicePayloadRepository extends JpaRepository<InvoicePayload, Long> {

    Optional<InvoicePayload> findByClientRequestId(String clientRequestId);

    Optional<InvoicePayload> findByTransactionId(String transactionId);

    Optional<InvoicePayload> findByInvoiceId(Long invoiceId);

    Page<InvoicePayload> findByMerchantIdOrderByCreatedAtDesc(Long merchantId, Pageable pageable);

    List<InvoicePayload> findByMerchantId(Long merchantId);

    Optional<InvoicePayload> findByClientRequestIdAndMerchantId(String clientRequestId, Long merchantId);

    @Query("SELECT ip FROM InvoicePayload ip WHERE ip.merchantId = :merchantId " +
            "AND ip.createdAt >= :fromDate AND ip.createdAt <= :toDate")
    List<InvoicePayload> findByMerchantIdAndCreatedAtBetween(
            @Param("merchantId") Long merchantId,
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate);

    @Query(value = "SELECT * FROM invoice_payloads ip " +
            "WHERE ip.merchant_id = :merchantId " +
            "AND JSON_EXTRACT(ip.extra_data, '$.buyer.taxCode') = :buyerTaxCode",
            nativeQuery = true)
    List<InvoicePayload> findByMerchantIdAndBuyerTaxCode(
            @Param("merchantId") Long merchantId,
            @Param("buyerTaxCode") String buyerTaxCode);

    long countByMerchantId(Long merchantId);

    long countByMerchantIdAndStatus(Long merchantId, String status);

    void deleteByMerchantId(Long merchantId);

    List<InvoicePayload> findByProviderCodeAndStatus(String providerCode, String status);

    List<InvoicePayload> findTop10ByMerchantIdOrderByCreatedAtDesc(Long merchantId);
}
