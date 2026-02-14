package com.einvoicehub.core.domain.repository;

import com.einvoicehub.core.domain.entity.EinvMerchantProviderConfigEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EinvMerchantProviderConfigRepository extends JpaRepository<EinvMerchantProviderConfigEntity, Long>,
        JpaSpecificationExecutor<EinvMerchantProviderConfigEntity> {

    @EntityGraph(attributePaths = {"provider"})
    Optional<EinvMerchantProviderConfigEntity> findByMerchantIdAndIsDefaultTrueAndIsActiveTrue(Long merchantId);

    @Query("SELECT c FROM EinvMerchantProviderConfigEntity c WHERE " +
            "(:merchantId IS NULL OR c.merchant.id = :merchantId) AND " +
            "(:search IS NULL OR LOWER(c.taxCode) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(c.partnerId) LIKE LOWER(CONCAT('%', :search, '%')))")
    List<EinvMerchantProviderConfigEntity> searchConfigs(@Param("merchantId") Long merchantId, @Param("search") String search);

    boolean existsByMerchantIdAndProviderId(Long merchantId, Long providerId);
}