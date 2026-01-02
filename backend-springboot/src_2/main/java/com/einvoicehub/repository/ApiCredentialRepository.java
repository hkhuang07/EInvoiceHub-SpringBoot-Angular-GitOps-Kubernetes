package com.einvoicehub.repository;

import com.einvoicehub.entity.ApiCredential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Api Credential Repository
 * 
 * Repository cho thao tác với bảng api_credentials
 */
@Repository
public interface ApiCredentialRepository extends JpaRepository<ApiCredential, Long> {

    /**
     * Tìm credential theo client ID
     */
    Optional<ApiCredential> findByClientId(String clientId);

    /**
     * Tìm credential theo API key
     */
    Optional<ApiCredential> findByApiKey(String apiKey);

    /**
     * Tìm credential theo client ID và API key
     */
    Optional<ApiCredential> findByClientIdAndApiKey(String clientId, String apiKey);

    /**
     * Kiểm tra credential tồn tại theo client ID
     */
    boolean existsByClientId(String clientId);

    /**
     * Kiểm tra credential tồn tại theo API key
     */
    boolean existsByApiKey(String apiKey);

    /**
     * Tìm tất cả credentials của một merchant
     */
    @Query("SELECT ac FROM ApiCredential ac WHERE ac.merchantId = :merchantId")
    java.util.List<ApiCredential> findByMerchantId(@Param("merchantId") Long merchantId);

    /**
     * Tìm credentials đang hoạt động của một merchant
     */
    @Query("SELECT ac FROM ApiCredential ac WHERE ac.merchantId = :merchantId AND ac.active = true")
    java.util.List<ApiCredential> findActiveByMerchantId(@Param("merchantId") Long merchantId);

    /**
     * Đếm số credentials đang hoạt động của một merchant
     */
    @Query("SELECT COUNT(ac) FROM ApiCredential ac WHERE ac.merchantId = :merchantId AND ac.active = true")
    int countActiveByMerchantId(@Param("merchantId") Long merchantId);
}