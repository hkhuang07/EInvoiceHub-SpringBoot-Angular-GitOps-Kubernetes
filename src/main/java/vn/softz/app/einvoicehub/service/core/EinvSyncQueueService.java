package vn.softz.app.einvoicehub.service.core;

import vn.softz.app.einvoicehub.domain.entity.EinvSyncQueueEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;


public interface EinvSyncQueueService {

    List<EinvSyncQueueEntity> pollPendingEntries(int batchSize);

    void markAsProcessing(String queueId);

    void markAsSuccess(String queueId, String cqtMessageId);
    
    void markAsFailed(String queueId, String errorMessage, String errorCode);

    List<EinvSyncQueueEntity> findByInvoiceId(String invoiceId);

    List<EinvSyncQueueEntity> findPendingByTenant(String tenantId);

    Page<EinvSyncQueueEntity> findFailed(Pageable pageable);

    /**@param queueId ID của queue entry cần retry
     * @throws vn.softz.app.einvoicehub.exception.BusinessException nếu entry không ở trạng thái FAILED*/
    void retryManually(String queueId);

    Optional<EinvSyncQueueEntity> findById(String id);
}
