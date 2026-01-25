package com.einvoicehub.core.repository.jpa;

import com.einvoicehub.core.entity.jpa.MerchantUser;
import com.einvoicehub.core.entity.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MerchantUserRepository extends JpaRepository<MerchantUser, Long> {

    Optional<MerchantUser> findByUsernameAndIsActiveTrue(String username);

    Optional<MerchantUser> findByEmailAndIsActiveTrue(String email);

    List<MerchantUser> findByMerchantId(Long merchantId);

    List<MerchantUser> findByMerchantIdAndIsActive(Long merchantId, Boolean isActive);

    List<MerchantUser> findByMerchantIdAndRole(Long merchantId, UserRole role);

    Optional<MerchantUser> findByMerchantIdAndIsPrimaryTrue(Long merchantId);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    @Query("SELECT u FROM MerchantUser u WHERE u.merchant.id = :merchantId AND u.role = :role")
    List<MerchantUser> findByMerchantAndRole(
            @Param("merchantId") Long merchantId,
            @Param("role") UserRole role);

    @Query("SELECT COUNT(u) FROM MerchantUser u WHERE u.merchant.id = :merchantId AND u.isActive = true")
    long countActiveUsersByMerchant(@Param("merchantId") Long merchantId);

    @Query("SELECT u FROM MerchantUser u WHERE u.merchant.taxCode = :taxCode AND u.isActive = true")
    List<MerchantUser> findActiveUsersByMerchantTaxCode(@Param("taxCode") String taxCode);

    @Query("SELECT u FROM MerchantUser u WHERE u.isActive = true AND u.lockedUntil > :now")
    List<MerchantUser> findLockedUsers(@Param("now") LocalDateTime now);

    @Query("SELECT u FROM MerchantUser u WHERE u.lastLoginAt >= :since")
    List<MerchantUser> findRecentlyActiveUsers(@Param("since") LocalDateTime since);
}