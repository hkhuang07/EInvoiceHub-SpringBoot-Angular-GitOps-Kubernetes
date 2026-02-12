package com.einvoicehub.core.domain.repository;

import com.einvoicehub.core.domain.entity.MerchantProviderConfigEntity;
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
public interface MerchantProviderConfigRepository extends JpaRepository<MerchantProviderConfigEntity, Long> {
    List<MerchantProviderConfigEntity> findByMerchantId(Long merchantId);
    List<MerchantProviderConfigEntity> findByMerchantIdAndIsActive(Long merchantId, Boolean isActive);
    Optional<MerchantProviderConfigEntity> findByMerchantIdAndProviderId(Long merchantId, Long providerId);
    Optional<MerchantProviderConfigEntity> findByMerchantIdAndProviderProviderCode(Long merchantId, String providerCode);
    Optional<MerchantProviderConfigEntity> findByMerchantIdAndIsDefaultTrue(Long merchantId);

    @Query("SELECT c FROM MerchantProviderConfig c WHERE c.merchant.id = :merchantId " +
            "AND c.provider.id = :providerId AND c.isActive = true")
    Optional<MerchantProviderConfigEntity> findActiveConfig(
            @Param("merchantId") Long merchantId,
            @Param("providerId") Long providerId);

    @Query("SELECT c FROM MerchantProviderConfig c JOIN FETCH c.provider " +
            "WHERE c.merchant.id = :merchantId AND c.isActive = true")
    List<MerchantProviderConfigEntity> findActiveConfigsByMerchant(@Param("merchantId") Long merchantId);

    @Query("SELECT c FROM MerchantProviderConfig c WHERE c.isActive = true " +
            "AND c.isDefault = true AND c.merchant.id = :merchantId")
    Optional<MerchantProviderConfigEntity> findDefaultConfig(@Param("merchantId") Long merchantId);

    boolean existsByMerchantIdAndProviderId(Long merchantId, Long providerId);
}