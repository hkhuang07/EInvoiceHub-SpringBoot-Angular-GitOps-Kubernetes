package com.einvoicehub.core.domain.repository;

import com.einvoicehub.core.domain.entity.EinvInvoiceTemplateEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EinvInvoiceTemplateRepository extends JpaRepository<EinvInvoiceTemplateEntity, Long>,
        JpaSpecificationExecutor<EinvInvoiceTemplateEntity> {

    @EntityGraph(attributePaths = {"invoiceType", "registration"})
    Optional<EinvInvoiceTemplateEntity> findWithDetailsById(Long id);

    Optional<EinvInvoiceTemplateEntity> findByMerchantIdAndTemplateCodeAndSymbolCode(Long merchantId, String templateCode, String symbolCode);

    @Query("SELECT t FROM EinvInvoiceTemplateEntity t WHERE t.merchant.id = :merchantId " +
            "AND t.isActive = true AND (:search IS NULL OR " +
            "LOWER(t.templateCode) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(t.symbolCode) LIKE LOWER(CONCAT('%', :search, '%')))")
    List<EinvInvoiceTemplateEntity> searchTemplates(@Param("merchantId") Long merchantId, @Param("search") String search);

    @Query(value = "SELECT * FROM invoice_template WHERE merchant_id = :merchantId " +
            "AND is_active = 1 ORDER BY created_at DESC LIMIT 1", nativeQuery = true)
    Optional<EinvInvoiceTemplateEntity> findLatestTemplate(@Param("merchantId") Long merchantId);
    boolean existsByInvoiceTypeId(Long invoiceTypeId);
    boolean existsByRegistrationId(Long registrationId);
}