package vn.softz.app.einvoicehub.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import vn.softz.app.einvoicehub.domain.entity.EinvAuditLogEntity;
import vn.softz.app.einvoicehub.domain.repository.EinvAuditLogRepository;
import vn.softz.app.einvoicehub.service.core.EinvAuditLogService;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Triển khai {@link EinvAuditLogService}.
 *
 * <h3>Thiết kế quan trọng: REQUIRES_NEW Propagation</h3>
 * <p>Hàm {@link #log} dùng {@code Propagation.REQUIRES_NEW} để chạy
 * trong transaction <em>riêng biệt</em>. Điều này đảm bảo:
 * <ul>
 *   <li>Ngay cả khi transaction nghiệp vụ chính bị rollback, bản ghi audit
 *       vẫn được ghi vào DB (immutable evidence).
 *   <li>Ngược lại, lỗi ghi audit không rollback transaction chính.
 * </ul>
 *
 * <h3>Ví dụ flow</h3>
 * <pre>
 *   submitInvoice() [TX_MAIN]
 *     │
 *     ├─ save invoice → OK
 *     ├─ save queue   → OK
 *     │
 *     ├─ auditLogService.log(...)  ← [TX_NEW – commit riêng]
 *     │       └─ nếu lỗi → chỉ warn, TX_MAIN không bị ảnh hưởng
 *     │
 *     └─ commit TX_MAIN
 * </pre>
 *
 * <h3>Append-only</h3>
 * <p>Không có hàm update/delete. Mọi write đều là INSERT mới.
 * Dữ liệu audit là bằng chứng pháp lý, không được sửa xóa.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EinvAuditLogServiceImpl implements EinvAuditLogService {

    private final EinvAuditLogRepository repository;

    /**
     * Ghi audit log trong transaction độc lập (REQUIRES_NEW).
     *
     * <p>Method này <b>không bao giờ throw exception</b> ra ngoài.
     * Mọi lỗi đều được catch và log warn để đảm bảo nghiệp vụ chính
     * không bị ảnh hưởng bởi lỗi ghi audit phụ.
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void log(String action,
                    String entityName,
                    String entityId,
                    String payload,
                    String result,
                    String errorMsg) {
        try {
            EinvAuditLogEntity entry = new EinvAuditLogEntity();
            entry.setAction(action);
            entry.setEntityName(entityName);
            entry.setEntityId(entityId);
            entry.setPayload(payload);
            entry.setResult(result);
            entry.setErrorMsg(errorMsg);
            repository.save(entry);

            log.debug("[AuditLog] action={} entity={}/{} result={}",
                      action, entityName, entityId, result);
        } catch (Exception ex) {
            // KHÔNG propagate – audit failure không được crash luồng nghiệp vụ
            log.warn("[AuditLog] WRITE FAILED action={} entity={}/{} cause={}",
                     action, entityName, entityId, ex.getMessage());
        }
    }

    // ── Query Methods (read-only, cho dashboard/admin) ────────────────────────

    @Override
    @Transactional(readOnly = true)
    public List<EinvAuditLogEntity> findByEntity(String entityName, String entityId) {
        return repository.findByEntityNameAndEntityId(entityName, entityId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EinvAuditLogEntity> findByAction(String action, Pageable pageable) {
        return repository.findByAction(action, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EinvAuditLogEntity> findByUser(String userId) {
        return repository.findByCreatedBy(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EinvAuditLogEntity> findByDateRange(LocalDateTime from,
                                                    LocalDateTime to,
                                                    Pageable pageable) {
        return repository.findByCreatedDateBetween(from, to, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public long countFailuresByActionAndDateRange(String action,
                                                  LocalDateTime from,
                                                  LocalDateTime to) {
        return repository.countByActionAndResultAndCreatedDateBetween(
                action, "FAILURE", from, to);
    }
}
