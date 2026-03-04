package com.einvoicehub.core.domain.repository;

import com.einvoicehub.core.domain.entity.EinvMappingPaymentMethodEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EinvMappingPaymentMethodRepository extends JpaRepository<EinvMappingPaymentMethodEntity, Long>, JpaSpecificationExecutor<EinvMappingPaymentMethodEntity> {

    Optional<EinvMappingPaymentMethodEntity> findByProviderIdAndSystemCode(String providerId, String systemCode);
}