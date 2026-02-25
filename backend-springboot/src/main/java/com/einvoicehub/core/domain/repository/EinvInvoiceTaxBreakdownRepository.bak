package com.einvoicehub.core.domain.repository;

import com.einvoicehub.core.domain.entity.EinvInvoiceTaxBreakDownEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EinvInvoiceTaxBreakdownRepository extends JpaRepository<EinvInvoiceTaxBreakDownEntity, Long>,
        JpaSpecificationExecutor<EinvInvoiceTaxBreakDownEntity> {

    List<EinvInvoiceTaxBreakDownEntity> findByInvoiceId(Long invoiceId);

    @Modifying
    @Query("DELETE FROM EinvInvoiceTaxBreakDownEntity t WHERE t.invoice.id = :invoiceId")
    void deleteByInvoiceId(@Param("invoiceId") Long invoiceId);

    boolean existsByVatRateId(Long vateRateId);

}