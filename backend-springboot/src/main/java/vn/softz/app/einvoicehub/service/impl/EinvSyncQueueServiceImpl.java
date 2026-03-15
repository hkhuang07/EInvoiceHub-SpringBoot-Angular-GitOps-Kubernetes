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

/**
 * Triển khai {@link EinvSyncQueueService}.
 *
 * <h3>Retry Strategy</h3>
 * <pre>
 *   Lần 1 thất bại → nextRetryAt = now + 1 phút
 *   Lần 2 thất bại → nextRetryAt = now + 5 phút
 *   Lần 3 thất bại → status = FAILED (không retry nữa, cần xử lý thủ công)
 * </pre>
 *
 * <h3>Concurrency Guard</h3>
 * <p>Hàm {@link #markAsProcessing} đổi status PENDING → PROCESSING trước
 * khi Worker bắt đầu xử lý, tránh nhiều Worker cùng xử lý một entry.
 * Kết hợp với DB-level locking hoặc Optimistic Locking nếu cần HA cao.
 *
 * <h3>Tích hợp với Scheduler</h3>
 * <pre>{@code
 * // Ví dụ Scheduler (Spring @Scheduled):
 * @Scheduled(fixedDelay = 30_000)
 * public void processQueue() {
 *     List<EinvSyncQueueEntity> batch = syncQueueService.pollPendingEntries(50);
 *     batch.forEach(entry -> {
 *         syncQueueService.markAsProcessing(entry.getId());
 *         try {
 *             invoiceService.signInvoice(buildRequest(entry));
 *             syncQueueService.markAsSuccess(entry.getId(), null);
 *         } catch (Exception ex) {
 *             syncQueueService.markAsFailed(entry.getId(), ex.getMessage(), null);
 *         }
 *     });
 * }
 * }</pre>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EinvSyncQueueServiceImpl implements EinvSyncQueueService {

    private final EinvSyncQueueRepository repository;

    // Retry delay (phút) theo số lần thất bại
    private static final int[] RETRY_DELAY_MINUTES = {1, 5, 15};

    // ── Worker Interface ──────────────────────────────────────────────────────

    /**
     * Poll batch PENDING entry đã đến lúc retry.
     * Query: status='PENDING' AND nextRetryAt <= now(), giới hạn batchSize.
     */
    @Override
    @Transactional(readOnly = true)
    public List<EinvSyncQueueEntity> pollPendingEntries(int batchSize) {
        return repository.findPendingReadyForRetry(LocalDateTime.now(),
                org.springframework.data.domain.PageRequest.of(0, batchSize));
    }

    /**
     * PENDING → PROCESSING: đánh dấu "đang xử lý" để tránh duplicate processing.
     */
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

    /**
     * PROCESSING → SUCCESS: ghi nhận xử lý thành công.
     *
     * @param queueId      ID của queue entry
     * @param cqtMessageId ID thông điệp CQT (nếu có, để tra cứu sau)
     */
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

    /**
     * PROCESSING → FAILED / re-PENDING: ghi lỗi và lên lịch retry (nếu còn lượt).
     *
     * <p>Backoff schedule: lần 1 = +1min, lần 2 = +5min, lần 3 = FAILED vĩnh viễn.
     */
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

    // ── Query Methods ─────────────────────────────────────────────────────────

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

    /**
     * Retry thủ công: đặt lại PENDING và reset attemptCount để Worker nhận lại.
     *
     * <p>Dùng khi admin muốn thử lại một entry đã FAILED sau khi
     * sửa cấu hình NCC hoặc xử lý lỗi hệ thống.
     */
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

    // ── Private ───────────────────────────────────────────────────────────────

    private EinvSyncQueueEntity findEntityById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new BusinessException(
                    "einv.error.sync_queue_not_found: id=" + id));
    }
}
