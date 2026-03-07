package com.einvoicehub.server.domain.repository;

import com.einvoicehub.server.domain.entity.EinvAuditLogEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EinvAuditLogRepository extends JpaRepository<EinvAuditLogEntity, Long>, JpaSpecificationExecutor<EinvAuditLogEntity> {

    List<EinvAuditLogEntity> findByEntityNameAndEntityId(String entityName, String entityId);

    List<EinvAuditLogEntity> findByAction(String action);

    Page<EinvAuditLogEntity> findByAction(String action, Pageable pageable);

    List<EinvAuditLogEntity> findByCreatedBy(String createdBy);

    Page<EinvAuditLogEntity> findByCreatedBy(String createdBy, Pageable pageable);

    List<EinvAuditLogEntity> findByCreatedDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    Page<EinvAuditLogEntity> findByCreatedDateBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    @Query("SELECT l FROM EinvAuditLogEntity l WHERE l.entityName = :entityName AND l.entityId = :entityId AND l.result = 'FAILURE' ORDER BY l.createdDate DESC")
    List<EinvAuditLogEntity> findFailedLogsByEntity(@Param("entityName") String entityName, @Param("entityId") String entityId);

    long countByAction(String action);
    long countByActionAndResult(String action, String result);

    List<EinvAuditLogEntity> findByResult(String result);

    Page<EinvAuditLogEntity> findByResult(String result, Pageable pageable);
}
