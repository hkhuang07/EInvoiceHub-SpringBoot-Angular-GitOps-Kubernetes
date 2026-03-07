package com.einvoicehub.server.domain.repository;

import com.einvoicehub.server.domain.entity.EinvMappingPaymentMethodEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EinvMappingPaymentMethodRepository extends JpaRepository<EinvMappingPaymentMethodEntity, String>, JpaSpecificationExecutor<EinvMappingPaymentMethodEntity> {

    List<EinvMappingPaymentMethodEntity> findByProviderId(String providerId);

    List<EinvMappingPaymentMethodEntity> findByPaymentMethodId(Byte paymentMethodId);

    Optional<EinvMappingPaymentMethodEntity> findByProviderIdAndPaymentMethodId(String providerId, Byte paymentMethodId);

    Optional<EinvMappingPaymentMethodEntity> findByProviderIdAndProviderPaymentMethodId(String providerId, String providerPaymentMethodId);

    List<EinvMappingPaymentMethodEntity> findByProviderIdAndInactiveFalse(String providerId);

    boolean existsByProviderIdAndPaymentMethodId(String providerId, Byte paymentMethodId);
}
