package com.einvoicehub.core.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.einvoicehub.core.domain.entity.EinvInvoiceStatusEntity;

import java.util.Optional;
import java.util.List;

@Repository
public interface EinvInvoiceStatusRepository extends JpaRepository<EinvInvoiceStatusEntity, Integer>,
        JpaSpecificationExecutor<EinvInvoiceStatusEntity> {

    Optional<EinvInvoiceStatusEntity> findByName(String name);

    @Query("SELECT s FROM EinvInvoiceStatusEntity s WHERE " +
            "(:keyword IS NULL OR LOWER(s.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(s.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<EinvInvoiceStatusEntity> searchByKeyword(@Param("keyword") String keyword);
}