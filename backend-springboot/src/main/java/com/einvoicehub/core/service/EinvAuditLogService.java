package com.einvoicehub.core.service;

import com.einvoicehub.core.domain.entity.EinvAuditLogsEntity;
import com.einvoicehub.core.domain.repository.EinvAuditLogRepository;
import com.einvoicehub.core.dto.response.EinvAuditLogResponse;
import com.einvoicehub.core.exception.ErrorCode;
import com.einvoicehub.core.exception.InvalidDataException;
import com.einvoicehub.core.mapper.EinvHubMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EinvAuditLogService {

    private final EinvAuditLogRepository repository;
    private final EinvHubMapper mapper;

    @Transactional(readOnly = true)
    public List<EinvAuditLogResponse> getAll() {
        log.info("[Audit] Lấy danh sách toàn bộ nhật ký hệ thống");
        return repository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EinvAuditLogResponse getById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new InvalidDataException(ErrorCode.INVALID_DATA, "Không tìm thấy bản ghi nhật ký"));
    }

    @Transactional
    public void log(EinvAuditLogsEntity entity) {
        log.debug("[Audit] Lưu vết hành động: {} trên đối tượng {}", entity.getAction(), entity.getEntityType());

        if (entity.getEntityType() == null || entity.getEntityId() == null) {
            throw new InvalidDataException(ErrorCode.INVALID_DATA, "Dữ liệu nhật ký thiếu thông tin định danh");
        }

        repository.save(entity);
    }

    @Transactional
    public EinvAuditLogResponse update(Long id, String extraNote) {
        log.warn("[Audit] Cập nhật thông tin bổ sung cho log ID: {}", id);
        EinvAuditLogsEntity entity = repository.findById(id)
                .orElseThrow(() -> new InvalidDataException(ErrorCode.INVALID_DATA, "Nhật ký không tồn tại"));

        entity.setUserAgent(entity.getUserAgent() + " | Supplement: " + extraNote);

        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public void delete(Long id) {
        log.error("[Audit] CẢNH BÁO: Thao tác xóa bản ghi nhật ký ID: {}", id);
        if (!repository.existsById(id)) {
            throw new InvalidDataException(ErrorCode.INVALID_DATA, "Bản ghi không tồn tại để xóa");
        }
        // Trong hệ thống tài chính,log chỉ được xóa sau 10 năm .Ở đây ta triển khai hàm xóa cơ bản cho Admin.
        repository.deleteById(id);
    }
}