package com.einvoicehub.core.domain.repository;

import com.einvoicehub.core.domain.entity.MerchantEntity;
import com.einvoicehub.core.domain.enums.EntityStatus;
import com.einvoicehub.core.domain.enums.SubscriptionPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MerchantRepository extends JpaRepository<MerchantEntity, Long> {

    Optional<MerchantEntity> findByTaxCodeAndIsDeletedFalse(String taxCode);

    Optional<MerchantEntity> findByIdAndIsDeletedFalse(Long id);

    List<MerchantEntity> findByStatus(EntityStatus status);

    List<MerchantEntity> findBySubscriptionPlan(SubscriptionPlan plan);

    List<MerchantEntity> findByStatusAndSubscriptionPlan(EntityStatus status, SubscriptionPlan plan);

    boolean existsByTaxCode(String taxCode);

    boolean existsByEmail(String email);

    @Query("SELECT m FROM Merchant m WHERE m.isDeleted = false AND m.status = :status " +
            "AND m.subscriptionPlan = :plan")
    List<MerchantEntity> findActiveMerchantsByPlan(
            @Param("status") EntityStatus status,
            @Param("plan") SubscriptionPlan plan);

    @Query("SELECT COUNT(m) FROM Merchant m WHERE m.isDeleted = false AND m.status = :status")
    long countByStatus(@Param("status") EntityStatus status);

    @Query("SELECT m FROM Merchant m WHERE m.createdAt >= :fromDate AND m.createdAt <= :toDate")
    List<MerchantEntity> findMerchantsCreatedBetween(
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate);

    @Query("SELECT m FROM Merchant m LEFT JOIN FETCH m.users WHERE m.id = :id")
    Optional<MerchantEntity> findByIdWithUsers(@Param("id") Long id);

    @Query("SELECT m FROM Merchant m LEFT JOIN FETCH m.providerConfigs WHERE m.id = :id")
    Optional<MerchantEntity> findByIdWithConfigs(@Param("id") Long id);
}