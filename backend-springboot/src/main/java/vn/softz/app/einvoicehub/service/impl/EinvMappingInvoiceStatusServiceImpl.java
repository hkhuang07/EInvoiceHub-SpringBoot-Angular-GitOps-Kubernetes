package vn.softz.app.einvoicehub.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softz.app.einvoicehub.domain.entity.EinvMappingInvoiceStatusEntity;
import vn.softz.app.einvoicehub.domain.repository.EinvMappingInvoiceStatusRepository;
import vn.softz.app.einvoicehub.dto.EinvMappingInvoiceStatusDto;
import vn.softz.app.einvoicehub.mapper.EinvMappingInvoiceStatusMapper;
import vn.softz.app.einvoicehub.service.EinvMappingInvoiceStatusService;
import vn.softz.core.exception.BusinessException;

import java.util.List;
import java.util.Optional;

/**
 * Triển khai {@link EinvMappingInvoiceStatusService}.
 *
 * <h3>Thiết kế Mapping</h3>
 * <pre>
 *   DB: einv_mapping_invoice_status
 *   ┌────────────────────┬──────────────────────────────────────┬───────────────────────────┐
 *   │  provider_id       │  invoice_status_id (Hub)             │  provider_invoice_status_id│
 *   ├────────────────────┼──────────────────────────────────────┼───────────────────────────┤
 *   │  UUID_BKAV         │  2 (Đã phát hành)                    │  "5"                      │
 *   │  UUID_BKAV         │  3 (Đã hủy)                          │  "4"                      │
 *   │  UUID_MOBI         │  2 (Đã phát hành)                    │  "SIGNED"                 │
 *   │  UUID_MOBI         │  3 (Đã hủy)                          │  "CANCELLED"              │
 *   └────────────────────┴──────────────────────────────────────┴───────────────────────────┘
 * </pre>
 *
 * <h3>Quy tắc Validation</h3>
 * <ul>
 *   <li>Không được có hai bản ghi cùng {@code (providerId, invoiceStatusId)}.
 *       Ràng buộc này đảm bảo mapping Hub→Provider là hàm (1-1 theo chiều đó).</li>
 *   <li>Chiều ngược (Provider→Hub) được phép nhiều-nhiều nhưng {@code findHubStatusId}
 *       sẽ lấy bản ghi active đầu tiên (theo created_date).</li>
 *   <li>Soft-delete: set {@code inactive = true}, không xóa vật lý để giữ lịch sử.</li>
 * </ul>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EinvMappingInvoiceStatusServiceImpl implements EinvMappingInvoiceStatusService {

    private final EinvMappingInvoiceStatusRepository repository;
    private final EinvMappingInvoiceStatusMapper     mapper;

    // ─────────────────────────────────────────────────────────────────────────
    // CRUD
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Tạo mapping mới.
     *
     * <p>Kiểm tra duplicate theo {@code (providerId, invoiceStatusId)} để đảm bảo
     * mỗi trạng thái Hub chỉ map về một mã NCC duy nhất cho cùng NCC.
     */
    @Override
    @Transactional
    public EinvMappingInvoiceStatusDto create(EinvMappingInvoiceStatusDto dto) {
        // Validate: Không trùng (providerId, hubStatusId)
        Byte hubStatusId = dto.getInvoiceStatusId() != null
                ? dto.getInvoiceStatusId().byteValue() : null;

        if (hubStatusId != null
                && repository.existsByProviderIdAndInvoiceStatusId(dto.getProviderId(), hubStatusId)) {
            throw new BusinessException(
                String.format("einv.error.mapping_duplicate: providerId=%s, statusId=%d",
                              dto.getProviderId(), hubStatusId));
        }

        EinvMappingInvoiceStatusEntity entity = mapper.toEntity(dto);
        repository.saveAndFlush(entity);
        log.info("[mapping-status] Created: providerId={}, hubStatusId={}, providerStatusId={}",
                 dto.getProviderId(), hubStatusId, dto.getProviderInvoiceStatusId());

        return mapper.toDto(entity);
    }

    /**
     * Cập nhật mapping. Chỉ cho phép sửa {@code providerInvoiceStatusId},
     * {@code note}, và {@code inactive}.
     *
     * <p>Không được đổi {@code providerId} hay {@code invoiceStatusId} vì
     * sẽ phá vỡ unique constraint logic → phải xóa + tạo mới.
     */
    @Override
    @Transactional
    public EinvMappingInvoiceStatusDto update(String id, EinvMappingInvoiceStatusDto dto) {
        EinvMappingInvoiceStatusEntity entity = findEntityById(id);

        // Chỉ update các trường được phép thay đổi
        if (dto.getProviderInvoiceStatusId() != null) {
            entity.setProviderInvoiceStatusId(dto.getProviderInvoiceStatusId());
        }
        if (dto.getNote() != null) {
            entity.setNote(dto.getNote());
        }
        if (dto.getInactive() != null) {
            entity.setInactive(dto.getInactive() == 1);
        }

        repository.saveAndFlush(entity);
        log.info("[mapping-status] Updated id={}, providerStatusId={}",
                 id, entity.getProviderInvoiceStatusId());

        return mapper.toDto(entity);
    }

    /**
     * Soft-delete: đặt {@code inactive = true}.
     * Không hard-delete để giữ lịch sử ánh xạ cho audit.
     */
    @Override
    @Transactional
    public void delete(String id) {
        EinvMappingInvoiceStatusEntity entity = findEntityById(id);
        entity.setInactive(true);
        repository.save(entity);
        log.info("[mapping-status] Soft-deleted id={}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EinvMappingInvoiceStatusDto> findById(String id) {
        return repository.findById(id).map(mapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EinvMappingInvoiceStatusDto> findAllByProvider(String providerId) {
        return mapper.toDtoList(repository.findByProviderId(providerId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EinvMappingInvoiceStatusDto> findActiveByProvider(String providerId) {
        return mapper.toDtoList(repository.findByProviderIdAndInactiveFalse(providerId));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Core Mapping Logic – Two-Way Lookup
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Hub → Provider: tìm mã trạng thái bên NCC từ mã Hub.
     *
     * <p>Ví dụ: Hub status 2 ("Đã phát hành") → BKAV "5".
     *
     * <p>Chỉ trả về bản ghi có {@code inactive = false}.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<String> findProviderStatusId(String providerId, Byte hubStatusId) {
        return repository.findByProviderIdAndInvoiceStatusId(providerId, hubStatusId)
                         .filter(e -> !Boolean.TRUE.equals(e.getInactive()))
                         .map(EinvMappingInvoiceStatusEntity::getProviderInvoiceStatusId);
    }

    /**
     * Provider → Hub: tìm mã trạng thái Hub từ mã bên NCC.
     *
     * <p>Ví dụ: BKAV trả về "5" → Hub status 2.
     *
     * <p>Dùng khi xử lý callback/polling để cập nhật {@code einv_invoices.status_id}.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Byte> findHubStatusId(String providerId, String providerStatusId) {
        return repository.findByProviderIdAndProviderInvoiceStatusId(providerId, providerStatusId)
                         .filter(e -> !Boolean.TRUE.equals(e.getInactive()))
                         .map(EinvMappingInvoiceStatusEntity::getInvoiceStatusId);
    }

    /**
     * Hub → Provider với fallback.
     *
     * <p>Khi không có mapping, trả về {@code fallback} thay vì exception để
     * luồng xử lý không bị ngắt. Log warn để theo dõi gap cấu hình.
     */
    @Override
    @Transactional(readOnly = true)
    public String findProviderStatusIdOrDefault(String providerId,
                                                Byte hubStatusId,
                                                String fallback) {
        Optional<String> result = findProviderStatusId(providerId, hubStatusId);
        if (result.isEmpty()) {
            log.warn("[mapping-status] No mapping found Hub→Provider: "
                     + "providerId={}, hubStatusId={} → using fallback={}",
                     providerId, hubStatusId, fallback);
        }
        return result.orElse(fallback);
    }

    /**
     * Provider → Hub với fallback.
     *
     * <p>Khi không có mapping, trả về {@code fallback}.
     * Thường dùng fallback = 1 (Mới tạo) để không mất hóa đơn.
     */
    @Override
    @Transactional(readOnly = true)
    public Byte findHubStatusIdOrDefault(String providerId,
                                         String providerStatusId,
                                         Byte fallback) {
        Optional<Byte> result = findHubStatusId(providerId, providerStatusId);
        if (result.isEmpty()) {
            log.warn("[mapping-status] No mapping found Provider→Hub: "
                     + "providerId={}, providerStatusId={} → using fallback={}",
                     providerId, providerStatusId, fallback);
        }
        return result.orElse(fallback);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Private Helpers
    // ─────────────────────────────────────────────────────────────────────────

    private EinvMappingInvoiceStatusEntity findEntityById(String id) {
        return repository.findById(id)
                         .orElseThrow(() -> new BusinessException(
                             "einv.error.mapping_status_not_found: id=" + id));
    }
}
