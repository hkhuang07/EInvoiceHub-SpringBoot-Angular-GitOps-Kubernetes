package com.einvoicehub.core.domain.repository;

import com.einvoicehub.core.domain.entity.EinvInvoiceItemEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EinvInvoiceItemRepository extends JpaRepository<EinvInvoiceItemEntity, Long>,
        JpaSpecificationExecutor<EinvInvoiceItemEntity> {

    @EntityGraph(attributePaths = {"vatRate"})
    List<EinvInvoiceItemEntity> findByInvoiceIdOrderByLineNumberAsc(Long invoiceId);

    @Modifying
    @Query("DELETE FROM EinvInvoiceItemEntity i WHERE i.invoice.id = :invoiceId")
    void deleteByInvoiceId(@Param("invoiceId") Long invoiceId);

    @Query(value = "SELECT product_name, SUM(quantity) as total_qty FROM invoice_items i " +
            "JOIN invoice_metadata m ON i.invoice_id = m.id " +
            "WHERE m.merchant_id = :merchantId GROUP BY product_code, product_name " +
            "ORDER BY total_qty DESC LIMIT 10", nativeQuery = true)
    List<Object[]> findTopSellingProducts(@Param("merchantId") Long merchantId);
}
