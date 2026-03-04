package com.einvoicehub.core.domain.repository;

import com.einvoicehub.core.domain.entity.EinvSyncQueueEntity;
import com.einvoicehub.core.domain.entity.enums.SyncStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EinvSyncQueueRepository extends JpaRepository<EinvSyncQueueEntity, Long>, JpaSpecificationExecutor<EinvSyncQueueEntity> {

    List<EinvSyncQueueEntity> findByStatus(String status);
    List<EinvSyncQueueEntity> findByInvoiceId(Long invoiceId);

    @EntityGraph(attributePaths = {"invoice"})
    Optional<EinvSyncQueueEntity> findWithInvoiceById(Long id);

    List<EinvSyncQueueEntity> findByStatusInOrderByPriorityAscCreatedAtAsc(List<SyncStatus> statuses);

    @Query("SELECT q FROM EinvSyncQueueEntity q WHERE " +
            "(:status IS NULL OR q.status = :status) AND " +
            "(:search IS NULL OR LOWER(q.errorMessage) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(q.processedBy) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<EinvSyncQueueEntity> findByFilters(
            @Param("status") SyncStatus status,
            @Param("search") String search,
            Pageable pageable
    );

    @Query(value = "SELECT * FROM invoice_sync_queue " +
            "WHERE status = 'RETRYING' AND next_retry_at <= NOW() " +
            "ORDER BY priority ASC LIMIT :batchSize", nativeQuery = true)
    List<EinvSyncQueueEntity> findReadyToRetry(@Param("batchSize") int batchSize);
}
