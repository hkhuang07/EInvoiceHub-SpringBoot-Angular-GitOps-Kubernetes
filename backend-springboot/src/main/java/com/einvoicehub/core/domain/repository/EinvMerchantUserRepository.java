package com.einvoicehub.core.domain.repository;

import com.einvoicehub.core.domain.entity.EinvMerchantUserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EinvMerchantUserRepository extends JpaRepository<EinvMerchantUserEntity, Long>,
        JpaSpecificationExecutor<EinvMerchantUserEntity> {

    @EntityGraph(attributePaths = {"merchant"})
    Optional<EinvMerchantUserEntity> findByUsername(String username);

    Optional<EinvMerchantUserEntity> findByEmail(String email);

    @Query("SELECT u FROM EinvMerchantUserEntity u WHERE u.merchant.id = :merchantId " +
            "AND (:search IS NULL OR LOWER(u.username) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(u.fullName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<EinvMerchantUserEntity> findByMerchantAndFilters(
            @Param("merchantId") Long merchantId,
            @Param("search") String search,
            Pageable pageable);

    boolean existsByMerchantIdAndIsPrimaryTrue(Long merchantId);
}