package com.einvoicehub.core.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.einvoicehub.core.domain.entity.EinvInvoiceTypeEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface EinvInvoiceTypeRepository extends JpaRepository<EinvInvoiceTypeEntity, Long>,
        JpaSpecificationExecutor<EinvInvoiceTypeEntity> {

    List<EinvInvoiceTypeEntity> findByIsActiveTrueOrderByDisplayOrderAsc();

    Optional<EinvInvoiceTypeEntity> findByTypeCode(String typeCode);

    @Query("SELECT t FROM EinvInvoiceTypeEntity t WHERE " +
            "(:search IS NULL OR LOWER(t.typeCode) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(t.typeName) LIKE LOWER(CONCAT('%', :search, '%'))) " +
            "AND t.isActive = true " +
            "ORDER BY t.displayOrder ASC")
    List<EinvInvoiceTypeEntity> searchActiveTypes(@Param("search") String search);
}