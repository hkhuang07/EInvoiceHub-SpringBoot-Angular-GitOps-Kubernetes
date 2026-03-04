package com.einvoicehub.core.domain.repository;

import com.einvoicehub.core.domain.entity.EinvInvoicePayloadEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EinvInvoicePayloadRepository extends JpaRepository<EinvInvoicePayloadEntity, Long>, JpaSpecificationExecutor<EinvInvoicePayloadEntity> {

    @EntityGraph(attributePaths = {"invoice"})
    Optional<EinvInvoicePayloadEntity> findByInvoiceId(Long invoiceId);

}