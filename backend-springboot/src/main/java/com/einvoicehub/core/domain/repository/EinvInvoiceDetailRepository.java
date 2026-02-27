package com.einvoicehub.core.domain.repository;

import com.einvoicehub.core.domain.entity.EinvInvoiceDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EinvInvoiceDetailRepository extends JpaRepository<EinvInvoiceDetailEntity, Long>,
        JpaSpecificationExecutor<EinvInvoiceDetailEntity> {

    List<EinvInvoiceDetailEntity> findByInvoiceIdOrderByLineNoAsc(Long invoiceId);

    @Modifying
    @Query("DELETE FROM EinvInvoiceDetailEntity d WHERE d.invoice.id = :invoiceId")
    void deleteByInvoiceId(@Param("invoiceId") Long invoiceId);

    @Query(value = "SELECT item_name, SUM(quantity) as total_qty FROM einv_invoices_detail d " +
            "JOIN einv_invoices i ON d.doc_id = i.id " +
            "WHERE i.tenant_id = :tenantId GROUP BY item_id, item_name " +
            "ORDER BY total_qty DESC LIMIT 10", nativeQuery = true)
    List<Object[]> findTopSellingProducts(@Param("tenantId") Long tenantId);

    boolean existsByTaxTypeId(String taxTypeId);

    void deleteByInvoiceId(String invoiceId);


}