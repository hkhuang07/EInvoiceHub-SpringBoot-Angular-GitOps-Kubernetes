package com.einvoicehub.core.domain.repository;

import com.einvoicehub.core.domain.entity.EinvSubmitInvoiceTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EinvSubmitInvoiceTypeRepository extends JpaRepository<EinvSubmitInvoiceTypeEntity, String> {
}