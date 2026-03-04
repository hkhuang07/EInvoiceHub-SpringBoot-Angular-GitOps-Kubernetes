package com.einvoicehub.core.domain.repository;

import com.einvoicehub.core.domain.entity.EinvProviderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EinvProviderRepository extends JpaRepository<EinvProviderEntity, String>, JpaSpecificationExecutor<EinvProviderEntity> {

    Optional<EinvProviderEntity> findByProviderCode(String providerCode);

    boolean existsByProviderCodeAndIsInactiveFalse(String providerCode);

    Optional<EinvProviderEntity> findByIdAndInactiveFalse(String id);

    List<EinvProviderEntity> findByInactiveFalse();

}
