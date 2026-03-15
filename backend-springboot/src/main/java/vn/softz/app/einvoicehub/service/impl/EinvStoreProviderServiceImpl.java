package vn.softz.app.einvoicehub.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softz.app.einvoicehub.domain.entity.EinvStoreProviderEntity;
import vn.softz.app.einvoicehub.domain.entity.EinvStoreProviderHistoryEntity;
import vn.softz.app.einvoicehub.domain.repository.EinvStoreProviderHistoryRepository;
import vn.softz.app.einvoicehub.domain.repository.EinvStoreProviderRepository;
import vn.softz.app.einvoicehub.domain.repository.EinvStoreSerialRepository;
import vn.softz.app.einvoicehub.dto.EinvStoreProviderDto;
import vn.softz.app.einvoicehub.dto.EinvValidationResult;
import vn.softz.app.einvoicehub.dto.request.EinvStoreProviderRequest;
import vn.softz.app.einvoicehub.mapper.EinvStoreProviderMapper;
import vn.softz.app.einvoicehub.provider.bkav.BkavSoapClient;
import vn.softz.app.einvoicehub.provider.bkav.constant.BkavCommandType;
import vn.softz.app.einvoicehub.provider.bkav.model.BkavResponse;
import vn.softz.app.einvoicehub.provider.mobifone.MobifoneHttpClient;
import vn.softz.app.einvoicehub.provider.mobifone.model.MobifoneLoginResponse;
import vn.softz.app.einvoicehub.service.EinvStoreProviderService;
import vn.softz.core.audit.TenantAware;
import vn.softz.core.common.Common;
import vn.softz.core.exception.BusinessException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Triển khai {@link EinvStoreProviderService}.
 *
 * <h3>Bảo mật mật khẩu</h3>
 * <ul>
 *   <li>{@code partner_pwd} (mật khẩu đăng nhập NCC như MobiFone) →
 *       mã hóa bằng {@link BCryptPasswordEncoder} trước khi persist.</li>
 *   <li>{@code password_service} (mật khẩu API nội bộ) →
 *       tương tự, mã hóa BCrypt.</li>
 *   <li>DTO trả ra client luôn mask hai trường này thành {@code "***"} để tránh lộ hash.</li>
 * </ul>
 *
 * <h3>Lịch sử thay đổi</h3>
 * Mỗi lần tạo/cập nhật/hủy tích hợp đều ghi một bản ghi
 * {@link EinvStoreProviderHistoryEntity} với {@code actionType} phù hợp.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EinvStoreProviderServiceImpl implements EinvStoreProviderService {

    // ── Repositories ──────────────────────────────────────────────────────────
    private final EinvStoreProviderRepository           repository;
    private final EinvStoreProviderHistoryRepository    historyRepository;
    private final EinvStoreSerialRepository             serialRepository;

    // ── Mappers ───────────────────────────────────────────────────────────────
    private final EinvStoreProviderMapper               mapper;

    // ── Provider Clients ──────────────────────────────────────────────────────
    private final BkavSoapClient                        bkavSoapClient;
    private final MobifoneHttpClient                    mobifoneHttpClient;

    // ── Security ──────────────────────────────────────────────────────────────
    private final BCryptPasswordEncoder                 passwordEncoder;

    // ── Provider Code Constants ───────────────────────────────────────────────
    private static final String PROVIDER_BKAV     = "BKAV";
    private static final String PROVIDER_MOBIFONE = "MOBI";

    // ── Status Constants ──────────────────────────────────────────────────────
    /** 0 = Chưa tích hợp */
    private static final byte STATUS_PENDING     = 0;
    /** 1 = Tích hợp thành công */
    private static final byte STATUS_ACTIVE      = 1;
    /** 8 = Đã hủy tích hợp */
    private static final byte STATUS_DEACTIVATED = 8;

    // ─────────────────────────────────────────────────────────────────────────
    // Query Methods
    // ─────────────────────────────────────────────────────────────────────────

    @Override
    @TenantAware
    @Transactional(readOnly = true)
    public Optional<EinvStoreProviderDto> getConfig() {
        String storeId = resolveCurrentStoreId();
        return repository.findByStoreId(storeId)
                         .map(this::toDtoMasked);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EinvStoreProviderDto> getConfigByStoreId(String storeId) {
        return repository.findByStoreId(storeId)
                         .map(this::toDtoMasked);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EinvStoreProviderDto> getConfigsByTenant(String tenantId) {
        return repository.findByTenantId(tenantId)
                         .stream()
                         .map(this::toDtoMasked)
                         .toList();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Write Methods
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Tạo mới hoặc cập nhật cấu hình NCC.
     *
     * <p>Thứ tự xử lý:
     * <ol>
     *   <li>Upsert entity (tìm theo storeId, hoặc tạo mới).
     *   <li>Map request → entity qua MapStruct (bỏ qua null).
     *   <li><b>Mã hóa mật khẩu</b>:
     *       {@code partnerPwd} và {@code passwordService} được encode bằng BCrypt
     *       <em>chỉ khi</em> client gửi giá trị mới (khác null/blank).
     *   <li>Đặt status = 0 (bắt buộc validate lại sau khi đổi cấu hình).
     *   <li>Lưu entity và ghi history.
     * </ol>
     */
    @Override
    @TenantAware
    @Transactional
    public EinvStoreProviderDto saveConfig(EinvStoreProviderRequest request) {
        String storeId  = resolveCurrentStoreId();
        String tenantId = resolveCurrentTenantId();

        boolean isNew = !repository.findByStoreId(storeId).isPresent();

        EinvStoreProviderEntity entity = repository.findByStoreId(storeId)
                .orElseGet(() -> {
                    EinvStoreProviderEntity e = new EinvStoreProviderEntity();
                    e.setStoreId(storeId);
                    e.setTenantId(tenantId);
                    return e;
                });

        // Map các trường thông thường (MapStruct bỏ qua null)
        mapper.updateEntity(request, entity);

        // ── Mã hóa mật khẩu NCC (partner_pwd) ─────────────────────────────
        // Chỉ encode khi client gửi giá trị mới; giữ nguyên hash cũ nếu blank.
        if (hasValue(request.getPartnerPwd())) {
            String encoded = passwordEncoder.encode(request.getPartnerPwd());
            entity.setPartnerPwd(encoded);
            log.info("[saveConfig] partner_pwd re-encoded for storeId={}", storeId);
        }

        // ── Mã hóa mật khẩu Service API (password_service) ─────────────────
        if (hasValue(request.getPasswordService())) {
            String encoded = passwordEncoder.encode(request.getPasswordService());
            entity.setPasswordService(encoded);
            log.info("[saveConfig] password_service re-encoded for storeId={}", storeId);
        }

        // Override URL riêng cho VNPT
        if ("VNPT".equalsIgnoreCase(request.getProviderId())
                && hasValue(request.getIntegrationUrl())) {
            entity.setIntegrationUrl(request.getIntegrationUrl());
        }

        // Bắt buộc validate lại sau khi thay đổi cấu hình
        entity.setStatus(STATUS_PENDING);
        entity.setIntegratedDate(null);

        repository.saveAndFlush(entity);
        log.info("[saveConfig] {} config for storeId={}, providerId={}",
                isNew ? "Created" : "Updated", storeId, entity.getProviderId());

        // Ghi lịch sử thay đổi
        saveHistory(entity, isNew ? "CREATE" : "UPDATE", entity.getStatus(), null);

        return toDtoMasked(entity);
    }

    /**
     * Xác thực kết nối với NCC.
     *
     * <p>Nếu thành công: cập nhật {@code status = 1} và {@code integratedDate}.
     * Mọi exception đều được bắt và trả về {@link EinvValidationResult#fail}.
     */
    @Override
    @TenantAware
    @Transactional
    public EinvValidationResult validateConfig(EinvStoreProviderRequest request) {
        String providerId = request.getProviderId();
        log.info("[validateConfig] Validating provider={}", providerId);

        try {
            EinvValidationResult result = switch (providerId.toUpperCase()) {
                case PROVIDER_BKAV     -> validateBkav();
                case PROVIDER_MOBIFONE -> validateMobifone();
                default -> EinvValidationResult.fail(
                    "einv.error.provider_not_supported: " + providerId);
            };

            if (result.isSuccess()) {
                String storeId = resolveCurrentStoreId();
                repository.findByStoreId(storeId).ifPresent(entity -> {
                    entity.setStatus(STATUS_ACTIVE);
                    entity.setIntegratedDate(LocalDateTime.now());
                    repository.save(entity);
                    saveHistory(entity, "VALIDATE_SUCCESS", STATUS_ACTIVE, null);
                    log.info("[validateConfig] Integration SUCCESS for storeId={}", storeId);
                });
            }

            return result;

        } catch (Exception e) {
            log.error("[validateConfig] Unexpected error for provider={}: {}", providerId, e.getMessage(), e);
            return EinvValidationResult.fail("einv.error.connection: " + e.getMessage());
        }
    }

    /**
     * Hủy tích hợp NCC của Store đang đăng nhập.
     *
     * <p>Điều kiện tiên quyết: không còn dải ký hiệu {@code status = 1}.
     * Nếu vi phạm → trả về fail, không throw exception để FE hiển thị message thân thiện.
     */
    @Override
    @TenantAware
    @Transactional
    public EinvValidationResult deactivate() {
        String storeId = resolveCurrentStoreId();

        // Guard: còn dải ký hiệu đã được duyệt → không cho hủy
        if (serialRepository.existsByStoreIdAndStatus(storeId, STATUS_ACTIVE)) {
            log.warn("[deactivate] Cannot deactivate: storeId={} has active serials", storeId);
            return EinvValidationResult.fail("einv.error.cannot_cancel_has_approved");
        }

        repository.findByStoreId(storeId).ifPresent(entity -> {
            entity.setStatus(STATUS_DEACTIVATED);
            entity.setIntegratedDate(null);
            repository.save(entity);
            saveHistory(entity, "DEACTIVATE", STATUS_DEACTIVATED, "Hủy tích hợp NCC");
            log.info("[deactivate] Deactivated config id={} for storeId={}", entity.getId(), storeId);
        });

        return EinvValidationResult.ok("einv_config.msg.cancelled");
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isIntegrated(String storeId) {
        return repository.findByStoreId(storeId)
                         .map(e -> STATUS_ACTIVE == (e.getStatus() != null ? e.getStatus() : -1))
                         .orElse(false);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Private: Provider Validation Helpers
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Xác thực BKAV: gọi lệnh GET_UNIT_INFO_BY_TAXCODE để kiểm tra tài khoản.
     */
    private EinvValidationResult validateBkav() {
        String storeId = resolveCurrentStoreId();
        EinvStoreProviderEntity entity = repository.findByStoreId(storeId)
                .orElseThrow(() -> new BusinessException("einv.error.config_not_saved"));

        String taxCode = entity.getTaxCode();
        if (!hasValue(taxCode)) {
            return EinvValidationResult.fail("einv.error.missing_tax_code");
        }

        try {
            BkavResponse result = bkavSoapClient.executeCommand(
                    storeId,
                    BkavCommandType.GET_UNIT_INFO_BY_TAXCODE,
                    taxCode);

            if (result.isSuccess()) {
                log.info("[validateBkav] BKAV connection OK for taxCode={}", taxCode);
                return EinvValidationResult.ok("einv.success.bkav_connected");
            } else {
                log.warn("[validateBkav] BKAV returned error: {}", result.getErrorMessage());
                return EinvValidationResult.fail("einv.error.validation_failed");
            }
        } catch (Exception e) {
            log.error("[validateBkav] SOAP call failed: {}", e.getMessage());
            return EinvValidationResult.fail("einv.error.validation_failed");
        }
    }

    /**
     * Xác thực MobiFone: đăng nhập bằng username/password để lấy token.
     *
     * <p><b>Lưu ý về BCrypt:</b> {@code partnerPwd} trong DB là hash BCrypt.
     * MobiFone API yêu cầu mật khẩu gốc → mật khẩu thực cần được lấy từ
     * request tại thời điểm validate, KHÔNG lấy từ DB (vì đã hash).
     * Nếu client không gửi lại password trong request này, cần cơ chế
     * secure vault (future work). Hiện tại: raise error nếu thiếu.
     *
     * @implNote Để giải quyết vấn đề BCrypt-vs-plaintext, hệ thống có thể
     *     bổ sung cột {@code partner_pwd_encrypted} lưu bằng AES (2-way)
     *     thay vì BCrypt (1-way) cho các trường cần gửi đi plaintext.
     *     BCrypt phù hợp cho xác thực nội bộ, AES/KMS cho secrets gửi đến bên ngoài.
     */
    private EinvValidationResult validateMobifone() {
        String storeId = resolveCurrentStoreId();
        EinvStoreProviderEntity entity = repository.findByStoreId(storeId)
                .orElseThrow(() -> new BusinessException("einv.error.config_not_saved"));

        String username = entity.getPartnerUsr();
        String taxCode  = entity.getTaxCode();

        if (!hasValue(username)) {
            return EinvValidationResult.fail("einv.error.missing_credentials");
        }

        // DESIGN NOTE: partner_pwd trong DB là BCrypt hash → không thể gửi thẳng.
        // Cần client gửi lại plaintext trong request HOẶC dùng AES vault.
        // Tạm thời: yêu cầu endpoint validate nhận lại password trong body.
        // TODO: Replace với AES-encrypted secret vault (phase 2).
        log.warn("[validateMobifone] password strategy: plaintext required from client for API call");
        return EinvValidationResult.fail("einv.error.mobifone_resubmit_password_required");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Private: History Helper
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Ghi lịch sử thay đổi cấu hình NCC.
     *
     * @param entity     entity vừa được thay đổi
     * @param actionType loại hành động: CREATE / UPDATE / VALIDATE_SUCCESS / DEACTIVATE
     * @param status     trạng thái sau thay đổi
     * @param notes      ghi chú tùy chọn
     */
    private void saveHistory(EinvStoreProviderEntity entity,
                             String actionType,
                             Byte status,
                             String notes) {
        try {
            EinvStoreProviderHistoryEntity history = new EinvStoreProviderHistoryEntity();
            history.setTenantId(entity.getTenantId());
            history.setStoreId(entity.getStoreId());
            history.setProviderId(entity.getProviderId());
            history.setActionType(actionType);
            history.setStatus(status);
            history.setNotes(notes);
            historyRepository.save(history);
        } catch (Exception ex) {
            // History failure KHÔNG được rollback transaction chính
            log.warn("[saveHistory] Failed to record history for storeId={}: {}",
                     entity.getStoreId(), ex.getMessage());
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Private: DTO Masking – ẩn mật khẩu trước khi trả ra client
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Chuyển Entity → DTO và mask các trường nhạy cảm bằng {@code "***"}.
     *
     * <p>Các trường được mask: {@code partnerToken}, {@code partnerPwd},
     * {@code passwordService}.
     */
    private EinvStoreProviderDto toDtoMasked(EinvStoreProviderEntity entity) {
        EinvStoreProviderDto dto = mapper.toDto(entity);
        // Che các trường credential – client không cần thấy hash
        if (dto.getPartnerToken()    != null) dto.setPartnerToken("***");
        if (dto.getPartnerPwd()      != null) dto.setPartnerPwd("***");
        if (dto.getPasswordService() != null) dto.setPasswordService("***");
        return dto;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Private: Context Helpers
    // ─────────────────────────────────────────────────────────────────────────

    private String resolveCurrentStoreId() {
        return Common.getCurrentUser()
                     .map(u -> u.getLocId())
                     .orElseThrow(() -> new BusinessException("einv.error.no_store_context"));
    }

    private String resolveCurrentTenantId() {
        return Common.getCurrentUser()
                     .map(u -> u.getTenantId())
                     .orElseThrow(() -> new BusinessException("einv.error.no_tenant_context"));
    }

    private boolean hasValue(String s) {
        return s != null && !s.isBlank();
    }
}
