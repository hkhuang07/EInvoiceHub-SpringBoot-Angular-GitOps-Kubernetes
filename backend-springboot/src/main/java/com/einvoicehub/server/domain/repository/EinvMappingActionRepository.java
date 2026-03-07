package com.einvoicehub.server.domain.repository;

import com.einvoicehub.server.domain.entity.EinvMappingActionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface EinvMappingActionRepository extends JpaRepository<EinvMappingActionEntity, Long>, JpaSpecificationExecutor<EinvMappingActionEntity> {

    List<EinvMappingActionEntity> findByProviderId(String providerId);

    List<EinvMappingActionEntity> findByHubAction(String hubAction);

    Optional<EinvMappingActionEntity> findByProviderIdAndHubAction(String providerId, String hubAction);

    Optional<EinvMappingActionEntity> findByProviderIdAndProviderCmd(String providerId, String providerCmd);

    boolean existsByProviderIdAndHubAction(String providerId, String hubAction);

    long countByProviderId(String providerId);
}
