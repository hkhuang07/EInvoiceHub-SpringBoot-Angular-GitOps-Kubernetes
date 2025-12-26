package com.einvoicehub.core.repository.mysql;

import com.einvoicehub.core.entity.mysql.Merchant;
import com.einvoicehub.core.entity.enums.EntityStatus;
import com.einvoicehub.core.entity.enums.SubscriptionPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, Long> {

    Optional<Merchant> findByTaxCodeAndIsDeletedFalse(String taxCode);

    Optional<Merchant> findByIdAndIsDeletedFalse(Long id);

    List<Merchant> findByStatus(EntityStatus status);

    List<Merchant> findBySubscriptionPlan(SubscriptionPlan plan);

    List<Merchant> findByStatusAndSubscriptionPlan(EntityStatus status, SubscriptionPlan plan);

    boolean existsByTaxCode(String taxCode);

    boolean existsByEmail(String email);

    @Query("SELECT m FROM Merchant m WHERE m.isDeleted = false AND m.status = :status " +
            "AND m.subscriptionPlan = :plan")
    List<Merchant> findActiveMerchantsByPlan(
            @Param("status") EntityStatus status,
            @Param("plan") SubscriptionPlan plan);

    @Query("SELECT COUNT(m) FROM Merchant m WHERE m.isDeleted = false AND m.status = :status")
    long countByStatus(@Param("status") EntityStatus status);

    @Query("SELECT m FROM Merchant m WHERE m.createdAt >= :fromDate AND m.createdAt <= :toDate")
    List<Merchant> findMerchantsCreatedBetween(
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate);

    @Query("SELECT m FROM Merchant m LEFT JOIN FETCH m.users WHERE m.id = :id")
    Optional<Merchant> findByIdWithUsers(@Param("id") Long id);

    @Query("SELECT m FROM Merchant m LEFT JOIN FETCH m.providerConfigs WHERE m.id = :id")
    Optional<Merchant> findByIdWithConfigs(@Param("id") Long id);
}