package com.einvoicehub.core.domain.repository;

import com.einvoicehub.core.domain.entity.MerchantUserEntity;
import com.einvoicehub.core.domain.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MerchantUserRepository extends JpaRepository<MerchantUserEntity, Long> {

    Optional<MerchantUserEntity> findByUsernameAndIsActiveTrue(String username);

    Optional<MerchantUserEntity> findByEmailAndIsActiveTrue(String email);

    List<MerchantUserEntity> findByMerchantId(Long merchantId);

    List<MerchantUserEntity> findByMerchantIdAndIsActive(Long merchantId, Boolean isActive);

    List<MerchantUserEntity> findByMerchantIdAndRole(Long merchantId, UserRole role);

    Optional<MerchantUserEntity> findByMerchantIdAndIsPrimaryTrue(Long merchantId);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    @Query("SELECT u FROM MerchantUser u WHERE u.merchant.id = :merchantId AND u.role = :role")
    List<MerchantUserEntity> findByMerchantAndRole(
            @Param("merchantId") Long merchantId,
            @Param("role") UserRole role);

    @Query("SELECT COUNT(u) FROM MerchantUser u WHERE u.merchant.id = :merchantId AND u.isActive = true")
    long countActiveUsersByMerchant(@Param("merchantId") Long merchantId);

    @Query("SELECT u FROM MerchantUser u WHERE u.merchant.taxCode = :taxCode AND u.isActive = true")
    List<MerchantUserEntity> findActiveUsersByMerchantTaxCode(@Param("taxCode") String taxCode);

    @Query("SELECT u FROM MerchantUser u WHERE u.isActive = true AND u.lockedUntil > :now")
    List<MerchantUserEntity> findLockedUsers(@Param("now") LocalDateTime now);

    @Query("SELECT u FROM MerchantUser u WHERE u.lastLoginAt >= :since")
    List<MerchantUserEntity> findRecentlyActiveUsers(@Param("since") LocalDateTime since);
}