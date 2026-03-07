package com.einvoicehub.server.domain.repository;

import com.einvoicehub.server.domain.entity.EinvMappingTaxTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EinvMappingTaxTypeRepository extends JpaRepository<EinvMappingTaxTypeEntity, String>, JpaSpecificationExecutor<EinvMappingTaxTypeEntity> {

    List<EinvMappingTaxTypeEntity> findByProviderId(String providerId);

    List<EinvMappingTaxTypeEntity> findByTaxTypeId(String taxTypeId);

    Optional<EinvMappingTaxTypeEntity> findByProviderIdAndTaxTypeId(String providerId, String taxTypeId);

    Optional<EinvMappingTaxTypeEntity> findByProviderIdAndProviderTaxTypeId(String providerId, String providerTaxTypeId);

    List<EinvMappingTaxTypeEntity> findByProviderIdAndInactiveFalse(String providerId);

    boolean existsByProviderIdAndTaxTypeId(String providerId, String taxTypeId);
}
