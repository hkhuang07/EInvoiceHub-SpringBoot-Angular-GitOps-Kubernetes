package com.einvoicehub.repository;

import com.einvoicehub.entity.EntityStatus;
import com.einvoicehub.entity.Merchant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Merchant Repository
 * 
 * Repository cho thao tác với bảng merchants
 */
@Repository
public interface MerchantRepository extends JpaRepository<Merchant, Long> {

    /**
     * Tìm merchant theo tax code
     */
    Optional<Merchant> findByTaxCode(String taxCode);

    /**
     * Kiểm tra tax code tồn tại
     */
    boolean existsByTaxCode(String taxCode);

    /**
     * Tìm merchant theo email
     */
    Optional<Merchant> findByEmail(String email);

    /**
     * Tìm merchant theo tên công ty (chứa keyword)
     */
    Page<Merchant> findByCompanyNameContainingIgnoreCase(String companyName, Pageable pageable);

    /**
     * Tìm merchant theo trạng thái
     */
    List<Merchant> findByStatus(EntityStatus status);

    /**
     * Tìm merchant theo trạng thái với phân trang
     */
    Page<Merchant> findByStatus(EntityStatus status, Pageable pageable);

    /**
     * Tìm merchant đang hoạt động
     */
    @Query("SELECT m FROM Merchant m WHERE m.status = 'ACTIVE'")
    List<Merchant> findAllActive();

    /**
     * Tìm merchant đang hoạt động với phân trang
     */
    @Query("SELECT m FROM Merchant m WHERE m.status = 'ACTIVE'")
    Page<Merchant> findAllActive(Pageable pageable);

    /**
     * Tìm merchant có quota còn lại
     */
    @Query("SELECT m FROM Merchant m WHERE m.status = 'ACTIVE' " +
           "AND m.currentQuotaUsage < m.quotaLimit")
    List<Merchant> findMerchantsWithAvailableQuota();

    /**
     * Đếm merchant theo trạng thái
     */
    long countByStatus(EntityStatus status);

    /**
     * Tìm merchant theo subscription plan
     */
    List<Merchant> findBySubscriptionPlan(String subscriptionPlan);

    /**
     * Tìm merchant có activity gần đây
     */
    @Query("SELECT m FROM Merchant m WHERE m.lastActivityAt >= :fromDate ORDER BY m.lastActivityAt DESC")
    List<Merchant> findRecentlyActive(@Param("fromDate") java.time.LocalDateTime fromDate);

    /**
     * Tìm merchant inactive quá lâu (có thể xóa hoặc deactive)
     */
    @Query("SELECT m FROM Merchant m WHERE m.status = 'ACTIVE' " +
           "AND (m.lastActivityAt IS NULL OR m.lastActivityAt < : cutoffDate)")
    List<Merchant> findInactiveMerchants(@Param("cutoffDate") java.time.LocalDateTime cutoffDate);

    /**
     * Tìm merchant theo nhiều điều kiện
     */
    @Query("SELECT m FROM Merchant m WHERE " +
           "(:keyword IS NULL OR LOWER(m.companyName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(m.taxCode) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "AND (:status IS NULL OR m.status = :status) " +
           "AND (:plan IS NULL OR m.subscriptionPlan = :plan)")
    Page<Merchant> searchMerchants(
        @Param("keyword") String keyword,
        @Param("status") EntityStatus status,
        @Param("plan") String plan,
        Pageable pageable
    );
}