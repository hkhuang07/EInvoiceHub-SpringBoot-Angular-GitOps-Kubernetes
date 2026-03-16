package vn.softz.app.einvoicehub.domain.repository;

import vn.softz.app.einvoicehub.domain.entity.EinvInvoiceEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EinvInvoiceRepository extends JpaRepository<EinvInvoiceEntity, String>, JpaSpecificationExecutor<EinvInvoiceEntity> {

    Optional<EinvInvoiceEntity> findByPartnerInvoiceIdAndTenantId(String partnerInvoiceId, String tenantId);

    boolean existsByPartnerInvoiceIdAndTenantId(String partnerInvoiceId, String tenantId);

    @EntityGraph(attributePaths = {"details"})
    Optional<EinvInvoiceEntity> findWithDetailsById(String id);

    @EntityGraph(attributePaths = {"details"})
    Optional<EinvInvoiceEntity> findWithDetailsByPartnerInvoiceIdAndTenantId(String partnerInvoiceId, String tenantId);

    Optional<EinvInvoiceEntity> findByInvoiceNo(String invoiceNo);

    List<EinvInvoiceEntity> findByBuyerTaxCode(String buyerTaxCode);

    Page<EinvInvoiceEntity> findByBuyerTaxCode(String buyerTaxCode, Pageable pageable);

    List<EinvInvoiceEntity> findByStatusId(Byte statusId);

    Page<EinvInvoiceEntity> findByStatusId(Byte statusId, Pageable pageable);

    Page<EinvInvoiceEntity> findByTenantId(String tenantId, Pageable pageable);

    List<EinvInvoiceEntity> findByStoreId(String storeId);

    Page<EinvInvoiceEntity> findByStoreId(String storeId, Pageable pageable);

    List<EinvInvoiceEntity> findByProviderId(String providerId);

    @Query("SELECT i FROM EinvInvoiceEntity i WHERE " +
           "(:invoiceNo IS NULL OR i.invoiceNo = :invoiceNo) AND " +
           "(:buyerTaxCode IS NULL OR i.buyerTaxCode = :buyerTaxCode) AND " +
           "(:statusId IS NULL OR i.statusId = :statusId)")
    Page<EinvInvoiceEntity> searchInvoices(
            @Param("invoiceNo") String invoiceNo,
            @Param("buyerTaxCode") String buyerTaxCode,
            @Param("statusId") Byte statusId,
            Pageable pageable);

    List<EinvInvoiceEntity> findByInvoiceDateBetween(java.time.LocalDateTime startDate, java.time.LocalDateTime endDate);

    Page<EinvInvoiceEntity> findByInvoiceDateBetween(java.time.LocalDateTime startDate, java.time.LocalDateTime endDate, Pageable pageable);

    List<EinvInvoiceEntity> findByTenantIdAndStatusId(String tenantId, Byte statusId);

    Page<EinvInvoiceEntity> findByTenantIdAndStatusId(String tenantId, Byte statusId, Pageable pageable);

    List<EinvInvoiceEntity> findByProviderIdAndStatusId(String providerId, Byte statusId);

    Optional<EinvInvoiceEntity> findByInvoiceLookupCode(String invoiceLookupCode);

    List<EinvInvoiceEntity> findByIsDeletedTrue();

    List<EinvInvoiceEntity> findByIsDraftTrue();

    long countByTenantIdAndStatusId(String tenantId, Byte statusId);

    long countByTenantIdAndInvoiceDateBetween(String tenantId, java.time.LocalDateTime startDate, java.time.LocalDateTime endDate);
}
