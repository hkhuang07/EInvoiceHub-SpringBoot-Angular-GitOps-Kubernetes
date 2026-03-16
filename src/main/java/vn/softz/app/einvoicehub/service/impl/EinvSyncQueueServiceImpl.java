package vn.softz.app.einvoicehub.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softz.app.einvoicehub.domain.entity.EinvSyncQueueEntity;
import vn.softz.app.einvoicehub.domain.repository.EinvSyncQueueRepository;
import vn.softz.app.einvoicehub.service.core.EinvSyncQueueService;
import vn.softz.core.exception.BusinessException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class EinvSyncQueueServiceImpl implements EinvSyncQueueService {

    private final EinvSyncQueueRepository repository;

    private static final int[] RETRY_DELAY_MINUTES = {1, 5, 15};

    @Override
    @Transactional(readOnly = true)
    public List<EinvSyncQueueEntity> pollPendingEntries(int batchSize) {
        return repository.findPendingReadyForRetry(LocalDateTime.now(),
                org.springframework.data.domain.PageRequest.of(0, batchSize));
    }

    @Override
    @Transactional
    public void markAsProcessing(String queueId) {
        repository.findById(queueId).ifPresent(entry -> {
            if ("PENDING".equals(entry.getStatus())) {
                entry.setStatus("PROCESSING");
                repository.save(entry);
                log.debug("[SyncQueue] {} → PROCESSING", queueId);
            }
        });
    }

    @Override
    @Transactional
    public void markAsSuccess(String queueId, String cqtMessageId) {
        EinvSyncQueueEntity entry = findEntityById(queueId);
        entry.setStatus("SUCCESS");
        entry.setCqtMessageId(cqtMessageId);
        entry.setLastError(null);
        entry.setNextRetryAt(null);
        repository.save(entry);
        log.info("[SyncQueue] {} → SUCCESS, cqtMessageId={}", queueId, cqtMessageId);
    }

    @Override
    @Transactional
    public void markAsFailed(String queueId, String errorMessage, String errorCode) {
        EinvSyncQueueEntity entry = findEntityById(queueId);

        byte attempts = (byte) (entry.getAttemptCount() + 1);
        entry.setAttemptCount(attempts);
        entry.setLastError(errorMessage);
        entry.setErrorCode(errorCode);

        if (attempts >= entry.getMaxAttempts()) {
            // Hết lượt retry → FAILED vĩnh viễn, cần xử lý thủ công
            entry.setStatus("FAILED");
            entry.setNextRetryAt(null);
            log.warn("[SyncQueue] {} → FAILED (maxAttempts={} reached). Error: {}",
                     queueId, entry.getMaxAttempts(), errorMessage);
        } else {
            // Còn lượt → schedule retry với backoff
            int delayMinutes = attempts <= RETRY_DELAY_MINUTES.length
                    ? RETRY_DELAY_MINUTES[attempts - 1]
                    : RETRY_DELAY_MINUTES[RETRY_DELAY_MINUTES.length - 1];
            entry.setStatus("PENDING");
            entry.setNextRetryAt(LocalDateTime.now().plusMinutes(delayMinutes));
            log.warn("[SyncQueue] {} → PENDING retry #{} in {}min. Error: {}",
                     queueId, attempts, delayMinutes, errorMessage);
        }

        repository.save(entry);
    }


    @Override
    @Transactional(readOnly = true)
    public List<EinvSyncQueueEntity> findByInvoiceId(String invoiceId) {
        return repository.findByInvoiceId(invoiceId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EinvSyncQueueEntity> findPendingByTenant(String tenantId) {
        return repository.findByTenantIdAndStatus(tenantId, "PENDING");
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EinvSyncQueueEntity> findFailed(Pageable pageable) {
        return repository.findByStatus("FAILED", pageable);
    }

    @Override
    @Transactional
    public void retryManually(String queueId) {
        EinvSyncQueueEntity entry = findEntityById(queueId);
        if (!"FAILED".equals(entry.getStatus())) {
            throw new BusinessException(
                "einv.error.queue_not_failed: only FAILED entries can be manually retried");
        }
        entry.setStatus("PENDING");
        entry.setAttemptCount((byte) 0);
        entry.setNextRetryAt(LocalDateTime.now());
        entry.setLastError(null);
        repository.save(entry);
        log.info("[SyncQueue] {} manually reset to PENDING for retry", queueId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EinvSyncQueueEntity> findById(String id) {
        return repository.findById(id);
    }

    private EinvSyncQueueEntity findEntityById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new BusinessException(
                    "einv.error.sync_queue_not_found: id=" + id));
    }
}
