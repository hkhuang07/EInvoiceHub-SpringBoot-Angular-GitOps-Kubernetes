package com.einvoicehub.core.domain.repository;

import com.einvoicehub.core.domain.entity.EinvMappingActionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface EinvMappingActionRepository extends JpaRepository<EinvMappingActionEntity, Long>, JpaSpecificationExecutor<EinvMappingActionEntity> {

    Optional<EinvMappingActionEntity> findByProviderIdAndHubAction(String providerId, String hubAction);
}