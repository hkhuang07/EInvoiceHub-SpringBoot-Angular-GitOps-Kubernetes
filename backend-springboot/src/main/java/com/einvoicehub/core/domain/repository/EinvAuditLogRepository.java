package com.einvoicehub.core.domain.repository;

import com.einvoicehub.core.domain.entity.EinvAuditLogEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EinvAuditLogRepository extends JpaRepository<EinvAuditLogEntity, Long>,
        JpaSpecificationExecutor<EinvAuditLogEntity> {

    List<EinvAuditLogEntity> findByEntityNameAndEntityId(String entityName, String entityId);

    Page<EinvAuditLogEntity> findByEntityTypeAndEntityIdOrderByCreatedAtDesc(
            String entityType, Long entityId, Pageable pageable);

    @Query("SELECT l FROM EinvAuditLogsEntity l WHERE " +
            "(:merchantId IS NULL OR l.merchant.id = :merchantId) AND " +
            "(:entityType IS NULL OR l.entityType = :entityType) AND " +
            "(:action IS NULL OR l.action = :action) AND " +
            "(:search IS NULL OR LOWER(l.requestId) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            " LOWER(l.ipAddress) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<EinvAuditLogEntity> searchAuditLogs(
            @Param("merchantId") Long merchantId,
            @Param("entityType") String entityType,
            @Param("action") String action,
            @Param("search") String search,
            Pageable pageable);

    @Query(value = "SELECT COUNT(*) FROM audit_logs " +
            "WHERE ip_address = :ip AND created_at >= CURDATE()",
            nativeQuery = true)
    long countActionsTodayByIp(@Param("ip") String ipAddress);
}