package com.einvoicehub.core.domain.repository;

import com.einvoicehub.core.domain.entity.EinvMappingInvoiceTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EinvMappingInvoiceTypeRepository extends JpaRepository<EinvMappingInvoiceTypeEntity, Long>, JpaSpecificationExecutor<EinvMappingInvoiceTypeEntity> {

    Optional<EinvMappingInvoiceTypeEntity> findByProviderIdAndSystemCode(String providerId, String systemCode);
    Optional<EinvMappingInvoiceTypeEntity> findByProviderIdAndInvoiceTypeId(String providerId, Integer invoiceTypeId);
}