package vn.softz.app.einvoicehub.domain.repository;

import vn.softz.app.einvoicehub.domain.entity.EinvSyncQueueEntity;
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
public interface EinvSyncQueueRepository extends JpaRepository<EinvSyncQueueEntity, String>, JpaSpecificationExecutor<EinvSyncQueueEntity> {

    List<EinvSyncQueueEntity> findByTenantId(String tenantId);

    List<EinvSyncQueueEntity> findByProviderId(String providerId);

    List<EinvSyncQueueEntity> findByInvoiceId(String invoiceId);

    List<EinvSyncQueueEntity> findByStatus(String status);


    Page<EinvSyncQueueEntity> findByStatus(String status, Pageable pageable);

    List<EinvSyncQueueEntity> findByTenantIdAndStatus(String tenantId, String status);

    List<EinvSyncQueueEntity> findByProviderIdAndStatus(String providerId, String status);

    @Query("SELECT q FROM EinvSyncQueueEntity q WHERE q.status = 'PENDING' AND q.nextRetryAt <= :now")
    List<EinvSyncQueueEntity> findPendingForRetry(@Param("now") LocalDateTime now);

    @Query("SELECT q FROM EinvSyncQueueEntity q WHERE q.status = 'PENDING' AND q.nextRetryAt <= :now")
    Page<EinvSyncQueueEntity> findPendingForRetry(@Param("now") LocalDateTime now, Pageable pageable);

    long countByStatus(String status);

    long countByProviderIdAndStatus(String providerId, String status);
    List<EinvSyncQueueEntity> findBySyncTypeAndStatus(String syncType, String status);
}
