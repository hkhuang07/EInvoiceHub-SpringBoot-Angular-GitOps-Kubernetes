package vn.softz.app.einvoicehub.service.catalog;

import vn.softz.app.einvoicehub.domain.entity.EinvAuditLogEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface EinvAuditLogService {

    void log(String action,
             String entityName,
             String entityId,
             String payload,
             String result,
             String errorMsg);


    default void logSuccess(String action, String entityName,
                            String entityId, String payload) {
        log(action, entityName, entityId, payload, "SUCCESS", null);
    }


    default void logFailure(String action, String entityName,
                            String entityId, String payload, String errorMsg) {
        log(action, entityName, entityId, payload, "FAILURE", errorMsg);
    }

    List<EinvAuditLogEntity> findByEntity(String entityName, String entityId);

    Page<EinvAuditLogEntity> findByAction(String action, Pageable pageable);

    List<EinvAuditLogEntity> findByUser(String userId);

    Page<EinvAuditLogEntity> findByDateRange(LocalDateTime from,
                                             LocalDateTime to,
                                             Pageable pageable);

    long countFailuresByActionAndDateRange(String action,
                                           LocalDateTime from,
                                           LocalDateTime to);
}
