package vn.softz.app.einvoicehub.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.softz.app.einvoicehub.domain.entity.EinvHubInvoiceEntity;

import java.util.Optional;

@Repository
public interface EinvHubInvoiceRepository extends JpaRepository<EinvHubInvoiceEntity, String>,
        JpaSpecificationExecutor<EinvHubInvoiceEntity> {

    Optional<EinvHubInvoiceEntity> findByPartnerInvoiceId(String partnerInvoiceId);

    boolean existsByPartnerInvoiceId(String partnerInvoiceId);
    
    @EntityGraph(attributePaths = "details")
    Optional<EinvHubInvoiceEntity> findWithDetailsById(String id);

    @EntityGraph(attributePaths = "details")
    Optional<EinvHubInvoiceEntity> findTopByPartnerInvoiceIdOrderByCreatedDateDesc(String partnerInvoiceId);

    Optional<EinvHubInvoiceEntity> findByProviderInvoiceId(String providerInvoiceId);

    Optional<EinvHubInvoiceEntity> findByInvoiceLookupCode(String lookupCode);
    
    @Query("SELECT i FROM EinvHubInvoiceEntity i WHERE " +
        "(:searchString IS NULL OR " +
        "    LOWER(CONCAT(COALESCE(i.invoiceForm,''), COALESCE(i.invoiceSeries,''))) LIKE LOWER(CONCAT('%', :searchString, '%')) OR " +
        "    LOWER(i.invoiceNo) LIKE LOWER(CONCAT('%', :searchString, '%')) OR " +
        "    LOWER(i.buyerFullName) LIKE LOWER(CONCAT('%', :searchString, '%')) OR " +
        "    LOWER(i.buyerTaxCode) LIKE LOWER(CONCAT('%', :searchString, '%'))" +
        ") " +
        "AND (:statusId IS NULL OR i.statusId = :statusId)")
    Page<EinvHubInvoiceEntity> findByFilters(
        @Param("searchString") String searchString,
        @Param("statusId") Integer statusId,
        Pageable pageable
    );
}
