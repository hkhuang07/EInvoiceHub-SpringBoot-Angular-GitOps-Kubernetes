package com.einvoicehub.core.repository.jpa;

import com.einvoicehub.core.entity.jpa.MerchantProviderConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository cho MerchantProviderConfig entity
 */
@Repository
public interface MerchantProviderConfigRepository extends JpaRepository<MerchantProviderConfig, Long> {

    List<MerchantProviderConfig> findByMerchantId(Long merchantId);

    List<MerchantProviderConfig> findByMerchantIdAndIsActive(Long merchantId, Boolean isActive);

    Optional<MerchantProviderConfig> findByMerchantIdAndProviderId(Long merchantId, Long providerId);

    Optional<MerchantProviderConfig> findByMerchantIdAndIsDefaultTrue(Long merchantId);

    @Query("SELECT c FROM MerchantProviderConfig c WHERE c.merchant.id = :merchantId " +
            "AND c.provider.id = :providerId AND c.isActive = true")
    Optional<MerchantProviderConfig> findActiveConfig(
            @Param("merchantId") Long merchantId,
            @Param("providerId") Long providerId);

    @Query("SELECT c FROM MerchantProviderConfig c JOIN FETCH c.provider " +
            "WHERE c.merchant.id = :merchantId AND c.isActive = true")
    List<MerchantProviderConfig> findActiveConfigsByMerchant(@Param("merchantId") Long merchantId);

    @Query("SELECT c FROM MerchantProviderConfig c WHERE c.isActive = true " +
            "AND c.isDefault = true AND c.merchant.id = :merchantId")
    Optional<MerchantProviderConfig> findDefaultConfig(@Param("merchantId") Long merchantId);

    boolean existsByMerchantIdAndProviderId(Long merchantId, Long providerId);
}