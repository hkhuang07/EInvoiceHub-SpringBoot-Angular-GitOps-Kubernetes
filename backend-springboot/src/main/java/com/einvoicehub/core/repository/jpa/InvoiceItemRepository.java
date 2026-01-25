package com.einvoicehub.core.repository.jpa;

import com.einvoicehub.core.entity.jpa.InvoiceItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface InvoiceItemRepository extends JpaRepository<InvoiceItem, Long> {

    List<InvoiceItem> findByInvoicePayloadIdOrderByLineNumberAsc(Long invoicePayloadId);

    void deleteByInvoicePayloadId(Long invoicePayloadId);

    long countByInvoicePayloadId(Long invoicePayloadId);

    @Query("SELECT COALESCE(SUM(ii.amount), 0) FROM InvoiceItem ii WHERE ii.invoicePayloadId = :invoicePayloadId")
    BigDecimal sumAmountByInvoicePayloadId(@Param("invoicePayloadId") Long invoicePayloadId);

    @Query("SELECT COALESCE(SUM(ii.vatAmount), 0) FROM InvoiceItem ii WHERE ii.invoicePayloadId = :invoicePayloadId")
    BigDecimal sumVatAmountByInvoicePayloadId(@Param("invoicePayloadId") Long invoicePayloadId);

    @Query("SELECT COALESCE(SUM(ii.totalAmount), 0) FROM InvoiceItem ii WHERE ii.invoicePayloadId = :invoicePayloadId")
    BigDecimal sumTotalAmountByInvoicePayloadId(@Param("invoicePayloadId") Long invoicePayloadId);

    List<InvoiceItem> findByInvoicePayloadIdAndItemCode(Long invoicePayloadId, String itemCode);

    @Query(value = "SELECT ii.* FROM invoice_items ii " +
            "INNER JOIN invoice_payloads ip ON ii.invoice_payload_id = ip.id " +
            "WHERE JSON_EXTRACT(ip.extra_data, '$.buyer.taxCode') = :buyerTaxCode " +
            "ORDER BY ii.invoice_payload_id, ii.line_number",
            nativeQuery = true)
    List<InvoiceItem> findByBuyerTaxCode(@Param("buyerTaxCode") String buyerTaxCode);
}
