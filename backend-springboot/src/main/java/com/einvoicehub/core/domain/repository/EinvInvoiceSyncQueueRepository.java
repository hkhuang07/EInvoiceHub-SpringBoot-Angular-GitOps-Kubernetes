package com.einvoicehub.core.domain.repository;

import com.einvoicehub.core.domain.entity.EinvInvoiceSyncQueueEntity;
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
public interface EinvInvoiceSyncQueueRepository extends JpaRepository<EinvInvoiceSyncQueueEntity, Long>,
        JpaSpecificationExecutor<EinvInvoiceSyncQueueEntity> {

    @EntityGraph(attributePaths = {"invoice"})
    Optional<EinvInvoiceSyncQueueEntity> findWithInvoiceById(Long id);

    List<EinvInvoiceSyncQueueEntity> findByStatusInOrderByPriorityAscCreatedAtAsc(List<SyncStatus> statuses);

    @Query("SELECT q FROM EinvInvoiceSyncQueueEntity q WHERE " +
            "(:status IS NULL OR q.status = :status) AND " +
            "(:search IS NULL OR LOWER(q.errorMessage) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(q.processedBy) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<EinvInvoiceSyncQueueEntity> findByFilters(
            @Param("status") SyncStatus status,
            @Param("search") String search,
            Pageable pageable
    );

    @Query(value = "SELECT * FROM invoice_sync_queue " +
            "WHERE status = 'RETRYING' AND next_retry_at <= NOW() " +
            "ORDER BY priority ASC LIMIT :batchSize", nativeQuery = true)
    List<EinvInvoiceSyncQueueEntity> findReadyToRetry(@Param("batchSize") int batchSize);
}