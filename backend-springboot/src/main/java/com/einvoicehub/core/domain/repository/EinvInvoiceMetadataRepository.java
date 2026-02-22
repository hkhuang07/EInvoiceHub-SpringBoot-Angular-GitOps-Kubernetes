package com.einvoicehub.core.domain.repository;

import com.einvoicehub.core.domain.entity.EinvInvoiceMetadataEntity;
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
public interface EinvInvoiceMetadataRepository extends JpaRepository<EinvInvoiceMetadataEntity, Long>,
        JpaSpecificationExecutor<EinvInvoiceMetadataEntity> {

    Optional<EinvInvoiceMetadataEntity> findByPartnerInvoiceId(String partnerInvoiceId);

    Optional<EinvInvoiceMetadataEntity> findByLookupCode(String lookupCode);

    Optional<EinvInvoiceMetadataEntity> findByInvoiceNumber(String invoiceNumber);

    @EntityGraph(attributePaths = {"items", "invoiceStatus", "merchant"})
    Optional<EinvInvoiceMetadataEntity> findWithDetailsById(Long id);

    @EntityGraph(attributePaths = {"invoiceStatus"})
    Optional<EinvInvoiceMetadataEntity> findTopByPartnerInvoiceIdOrderByCreatedAtDesc(String partnerInvoiceId);

    @Query("SELECT i FROM EinvInvoiceMetadataEntity i WHERE " +
            "(:merchantId IS NULL OR i.merchant.id = :merchantId) AND " +
            "(:search IS NULL OR " +
            "    LOWER(CONCAT(COALESCE(i.templateCode,''), COALESCE(i.symbolCode,''))) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "    LOWER(i.invoiceNumber) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "    LOWER(i.buyerFullName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "    LOWER(i.buyerTaxCode) LIKE LOWER(CONCAT('%', :search, '%'))" +
            ") " +
            "AND (:statusId IS NULL OR i.invoiceStatus.id = :statusId) " +
            "AND i.isDeleted = false")
    Page<EinvInvoiceMetadataEntity> findByFilters(
            @Param("merchantId") Long merchantId,
            @Param("search") String search,
            @Param("statusId") Integer statusId,
            Pageable pageable
    );

    @Query(value = "SELECT * FROM invoice_metadata " +
            "WHERE merchant_id = :merchantId AND status_id = 5 " +
            "ORDER BY issue_date DESC, created_at DESC LIMIT 1",
            nativeQuery = true)
    Optional<EinvInvoiceMetadataEntity> findLatestSuccessInvoice(@Param("merchantId") Long merchantId);

    boolean existsByLookupCode(String lookupCode);

    boolean existsByInvoiceTypeId(Long invoiceTypeId);
    boolean existsByInvoiceTypeCode(String invoiceTypeCode);

    boolean existsByStatusId(Integer statusId);

    boolean existsByPaymentMethodId(Long paymentMethodId);
    boolean existsByPaymentMethod(String paymentMethod);

    boolean existsByMerchantId(Long merchantId);
    boolean existsByProviderId(Long providerId);
    boolean existsByProviderConfigId(Long providerConfigId);

    boolean existsByTemplateId(Long templateId);
}
