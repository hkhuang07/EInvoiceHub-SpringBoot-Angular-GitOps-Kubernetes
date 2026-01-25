package com.einvoicehub.core.repository.jpa;

import com.einvoicehub.core.entity.jpa.ApiCredential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ApiCredentialRepository extends JpaRepository<ApiCredential, Long> {

    Optional<ApiCredential> findByClientIdAndIsActiveTrue(String clientId);

    Optional<ApiCredential> findByClientId(String clientId);

    List<ApiCredential> findByMerchantId(Long merchantId);

    List<ApiCredential> findByMerchantIdAndIsActive(Long merchantId, Boolean isActive);

    Optional<ApiCredential> findByMerchantIdAndIsDefaultTrue(Long merchantId);

    boolean existsByClientId(String clientId);

    @Query("SELECT a FROM ApiCredential a WHERE a.apiKeyHash = :hash AND a.isActive = true")
    Optional<ApiCredential> findByApiKeyHash(@Param("hash") String hash);

    @Query("SELECT a FROM ApiCredential a WHERE a.merchant.id = :merchantId AND a.isActive = true " +
            "AND (a.expiredAt IS NULL OR a.expiredAt > :now)")
    List<ApiCredential> findValidCredentialsByMerchant(
            @Param("merchantId") Long merchantId,
            @Param("now") LocalDateTime now);

    @Modifying
    @Query("UPDATE ApiCredential a SET a.requestCountHour = 0 WHERE a.requestCountResettedAt < :threshold")
    int resetHourlyCounts(@Param("threshold") LocalDateTime threshold);

    @Modifying
    @Query("UPDATE ApiCredential a SET a.requestCountDay = 0, a.requestCountHour = 0, " +
            "a.requestCountResettedAt = :now WHERE a.requestCountResettedAt < :threshold")
    int resetDailyCounts(@Param("threshold") LocalDateTime threshold, @Param("now") LocalDateTime now);

    @Query("SELECT COUNT(a) FROM ApiCredential a WHERE a.merchant.id = :merchantId AND a.isActive = true")
    long countActiveCredentialsByMerchant(@Param("merchantId") Long merchantId);
}