package com.einvoicehub.core.domain.repository;

import com.einvoicehub.core.domain.entity.EinvApiCredentialsEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EinvApiCredentialRepository extends JpaRepository<EinvApiCredentialsEntity, Long>,
        JpaSpecificationExecutor<EinvApiCredentialsEntity> {


     //Tìm kiếm thông tin xác thực bằng Client ID công khai.
    Optional<EinvApiCredentialsEntity> findByClientIdAndIsActiveTrue(String clientId);

    @Query("SELECT c FROM EinvApiCredentialsEntity c WHERE " +
            "(:merchantId IS NULL OR c.merchant.id = :merchantId) AND " +
            "(:search IS NULL OR " +
            "    LOWER(c.clientId) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "    LOWER(c.apiKeyPrefix) LIKE LOWER(CONCAT('%', :search, '%'))" +
            ")")
    Page<EinvApiCredentialsEntity> findByFilters(
            @Param("merchantId") Long merchantId,
            @Param("search") String search,
            Pageable pageable);


     // Native Query: Lấy Prefix của API Key mới nhất vừa tạo cho Merchant.
    @Query(value = "SELECT api_key_prefix FROM api_credentials " +
            "WHERE merchant_id = :merchantId ORDER BY created_at DESC LIMIT 1",
            nativeQuery = true)
    String findLatestApiKeyPrefix(@Param("merchantId") Long merchantId);
}