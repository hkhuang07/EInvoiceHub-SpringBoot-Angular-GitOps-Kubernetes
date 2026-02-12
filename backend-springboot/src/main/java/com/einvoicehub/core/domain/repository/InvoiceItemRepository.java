package com.einvoicehub.core.domain.repository;

import com.einvoicehub.core.domain.entity.InvoiceItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface InvoiceItemEntityRepository extends JpaRepository<InvoiceItemEntity, Long> {

    /**
     * Lấy danh sách chi tiết theo ID hóa đơn, sắp xếp theo số thứ tự dòng
     * Ánh xạ từ findByInvoiceIdOrderByLineNo của CBHD
     */
    List<InvoiceItemEntity> findByInvoiceIdOrderByLineNumberAsc(Long invoiceId);

    /**
     * Xóa toàn bộ chi tiết của một hóa đơn
     * Ánh xạ từ deleteByInvoiceId của CBHD
     */
    void deleteByInvoiceId(Long invoiceId);

    /**
     * Đếm số lượng dòng hàng hóa trong một hóa đơn
     */
    long countByInvoiceId(Long invoiceId);

    /**
     * Tính tổng tiền trước thuế của các dòng hàng hóa
     */
    @Query("SELECT COALESCE(SUM(ii.amount), 0) FROM InvoiceItemEntity ii WHERE ii.invoice.id = :invoiceId")
    BigDecimal sumAmountByInvoiceId(@Param("invoiceId") Long invoiceId);

    /**
     * Tính tổng tiền thuế của các dòng hàng hóa
     */
    @Query("SELECT COALESCE(SUM(ii.taxAmount), 0) FROM InvoiceItemEntity ii WHERE ii.invoice.id = :invoiceId")
    BigDecimal sumTaxAmountByInvoiceId(@Param("invoiceId") Long invoiceId);

    /**
     * Tính tổng tiền thanh toán của các dòng hàng hóa
     */
    @Query("SELECT COALESCE(SUM(ii.totalAmount), 0) FROM InvoiceItemEntity ii WHERE ii.invoice.id = :invoiceId")
    BigDecimal sumTotalAmountByInvoiceId(@Param("invoiceId") Long invoiceId);

    /**
     * Tìm kiếm sản phẩm theo mã trong một hóa đơn cụ thể
     */
    List<InvoiceItemEntity> findByInvoiceIdAndProductCode(Long invoiceId, String productCode);

    /**
     * Tìm tất cả chi tiết hàng hóa dựa trên mã số thuế người mua (Join với bảng Metadata)
     */
    @Query(value = "SELECT ii.* FROM invoice_items ii " +
            "INNER JOIN invoices_metadata im ON ii.invoice_id = im.id " +
            "WHERE im.buyer_tax_code = :buyerTaxCode " +
            "ORDER BY ii.invoice_id, ii.line_number",
            nativeQuery = true)
    List<InvoiceItemEntity> findByBuyerTaxCode(@Param("buyerTaxCode") String buyerTaxCode);
}