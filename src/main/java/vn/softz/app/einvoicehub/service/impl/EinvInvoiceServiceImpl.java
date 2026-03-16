package vn.softz.app.einvoicehub.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softz.app.einvoicehub.domain.entity.EinvAuditLogEntity;
import vn.softz.app.einvoicehub.domain.entity.EinvInvoiceEntity;
import vn.softz.app.einvoicehub.domain.entity.EinvInvoicePayloadEntity;
import vn.softz.app.einvoicehub.domain.entity.EinvSyncQueueEntity;
import vn.softz.app.einvoicehub.domain.repository.EinvAuditLogRepository;
import vn.softz.app.einvoicehub.domain.repository.EinvInvoicePayloadRepository;
import vn.softz.app.einvoicehub.domain.repository.EinvInvoiceRepository;
import vn.softz.app.einvoicehub.domain.repository.EinvStoreProviderRepository;
import vn.softz.app.einvoicehub.domain.repository.EinvStoreSerialRepository;
import vn.softz.app.einvoicehub.domain.repository.EinvSyncQueueRepository;
import vn.softz.app.einvoicehub.dto.EinvInvoiceDto;
import vn.softz.app.einvoicehub.dto.request.GetInvoicesRequest;
import vn.softz.app.einvoicehub.dto.request.SignInvoicesRequest;
import vn.softz.app.einvoicehub.dto.request.SubmitInvoiceRequest;
import vn.softz.app.einvoicehub.dto.response.GetInvoicesResponse;
import vn.softz.app.einvoicehub.dto.response.ListInvoicesResponse;
import vn.softz.app.einvoicehub.dto.response.SignInvoiceResponse;
import vn.softz.app.einvoicehub.dto.response.SubmitInvoiceResponse;
import vn.softz.app.einvoicehub.mapper.EinvInvoiceMapper;
import vn.softz.app.einvoicehub.service.EinvInvoiceService;
import vn.softz.app.einvoicehub.service.EinvMappingInvoiceStatusService;
import vn.softz.core.exception.BusinessException;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class EinvInvoiceServiceImpl implements EinvInvoiceService {

    private final EinvInvoiceRepository           invoiceRepository;
    private final EinvInvoicePayloadRepository    payloadRepository;
    private final EinvSyncQueueRepository         syncQueueRepository;
    private final EinvAuditLogRepository          auditLogRepository;
    private final EinvStoreSerialRepository       serialRepository;
    private final EinvStoreProviderRepository     storeProviderRepository;

    private final EinvMappingInvoiceStatusService mappingStatusService;

    private final EinvInvoiceMapper               invoiceMapper;
    private final ObjectMapper                    objectMapper;

    private static final byte INV_STATUS_NEW        = 1;  // Mới tạo
    private static final byte INV_STATUS_PUBLISHED  = 2;  // Đã phát hành
    private static final byte INV_STATUS_CANCELLED  = 3;  // Đã hủy

    private static final String QUEUE_PENDING    = "PENDING";
    private static final String QUEUE_PROCESSING = "PROCESSING";
    private static final String QUEUE_SUCCESS    = "SUCCESS";
    private static final String QUEUE_FAILED     = "FAILED";

    private static final String SYNC_SUBMIT     = "SUBMIT";
    private static final String SYNC_SIGN       = "SIGN";
    private static final String SYNC_GET_STATUS = "GET_STATUS";

    private static final String AUDIT_SUCCESS = "SUCCESS";
    private static final String AUDIT_FAILURE = "FAILURE";

    // 1. submitInvoice – Nhận yêu cầu từ POS, lưu vào Hub + SyncQueue
    @Override
    @Transactional
    public SubmitInvoiceResponse submitInvoice(SubmitInvoiceRequest request) {
        log.info("[submitInvoice] START tenantId={}, storeId={}, partnerInvoiceId={}",
                 request.getTenantId(), request.getStoreId(), request.getPartnerInvoiceId());

        // ── B1: Validate đầu vào ─────────────────────────────────────────────
        validateSubmitRequest(request);

        // ── B2: Map Request → Entity và lưu hóa đơn ─────────────────────────
        EinvInvoiceEntity invoice = buildInvoiceEntity(request);
        invoiceRepository.saveAndFlush(invoice);
        log.debug("[submitInvoice] Invoice saved: id={}", invoice.getId());

        // ── B3: Lưu các dòng hàng hóa (detail lines) ─────────────────────────
        // TODO: saveInvoiceDetails(invoice.getId(), request.getDetails());
        // → Map SubmitInvoiceDetailRequest → EinvInvoiceDetailEntity
        // → Set tenantId, storeId, docId từ invoice
        log.debug("[submitInvoice] [STUB] Invoice details pending implementation");

        // ── B4: Lưu payload thô để debug & audit ─────────────────────────────
        saveRawPayload(invoice.getId(), request);

        // ── B5: Đẩy vào SyncQueue để Worker xử lý bất đồng bộ ───────────────
        EinvSyncQueueEntity queueEntry = buildSyncQueueEntry(
                invoice, SYNC_SUBMIT, request.getTenantId());
        syncQueueRepository.save(queueEntry);
        log.info("[submitInvoice] Enqueued syncQueue id={} for invoiceId={}",
                 queueEntry.getId(), invoice.getId());

        // ── B6: Ghi AuditLog ─────────────────────────────────────────────────
        createAuditLog("SUBMIT_INVOICE", "einv_invoices", invoice.getId(),
                       toJson(request), AUDIT_SUCCESS, null);

        log.info("[submitInvoice] END invoiceId={}", invoice.getId());

        // Trả về response
        return SubmitInvoiceResponse.builder()
                .invoiceId(invoice.getId())
                .partnerInvoiceId(request.getPartnerInvoiceId())
                .statusId(INV_STATUS_NEW)
                .message("einv.success.invoice_queued")
                .build();
    }

    
    // 2. signInvoice – Ký số hóa đơn qua Provider API [STUB]
    @Override
    @Transactional
    public SignInvoiceResponse signInvoice(SignInvoicesRequest request) {
        log.info("[signInvoice] STUB - invoiceIds={}", request.getInvoiceIds());

        // TODO [B1]: Load và validate hóa đơn
        // List<EinvInvoiceEntity> invoices = invoiceRepository.findAllById(request.getInvoiceIds());
        // invoices.removeIf(inv -> inv.getStatusId() != INV_STATUS_NEW);

        // TODO [B2]: Load config NCC
        // EinvStoreProviderEntity providerConfig = storeProviderRepository
        //     .findByStoreIdAndProviderId(storeId, providerId)
        //     .orElseThrow(() -> new BusinessException("einv.error.provider_not_configured"));

        // TODO [B3]: Build & map provider request
        // Sử dụng EinvMappingService để convert:
        //   taxTypeId      → mappingService.getProviderCode(lid, providerId, TAX_TYPE, hubCode)
        //   paymentMethod  → mappingService.getProviderCode(lid, providerId, PAYMENT_METHOD, hubCode)
        //   invoiceStatus  → mappingStatusService.findProviderStatusId(providerId, hubStatus)

        // TODO [B4]: Gọi Provider API theo switch(providerId):
        //   case BKAV:     bkavSoapClient.executeCommand(storeId, cmd_102, bkavRequest)
        //   case MOBIFONE: mobifoneHttpClient.submitInvoice(storeId, mobiRequest)
        //   case VNPT:     vnptWebServiceClient.publishInvoice(vnptRequest)

        // TODO [B5]: Xử lý response, update invoice + syncQueue

        // TODO [B6]: createAuditLog("SIGN_INVOICE", ...)

        log.warn("[signInvoice] Not yet implemented – returning stub response");
        return SignInvoiceResponse.builder()
                .message("einv.stub.sign_invoice_pending")
                .build();
    }

    
    // 3. handleCallback – Tiếp nhận phản hồi từ NCC/CQT [STUB]
    @Override
    @Transactional
    public void handleCallback(String invoiceId, String callbackPayload, String providerId) {
        log.info("[handleCallback] STUB - invoiceId={}, providerId={}", invoiceId, providerId);

        // TODO [B1]: Verify callback signature/token

        // TODO [B2]: Parse callbackPayload
        // CallbackData data = objectMapper.readValue(callbackPayload, CallbackData.class);

        // TODO [B3]: Map provider status → Hub status
        // Byte hubStatusId = mappingStatusService.findHubStatusIdOrDefault(
        //     providerId, data.getProviderStatusId(), INV_STATUS_NEW);

        // TODO [B4]: Update einv_invoices
        // EinvInvoiceEntity invoice = invoiceRepository.findById(invoiceId)
        //     .orElseThrow(() -> new BusinessException("einv.error.invoice_not_found: " + invoiceId));
        // invoice.setStatusId(hubStatusId);
        // invoice.setTaxAuthorityCode(data.getTaxAuthorityCode());
        // invoice.setInvoiceLookupCode(data.getLookupCode());
        // invoiceRepository.save(invoice);

        // TODO [B5]: Update sync queue
        // syncQueueRepository.findTopByInvoiceIdOrderByCreatedDateDesc(invoiceId)
        //     .ifPresent(q -> { q.setStatus(QUEUE_SUCCESS); syncQueueRepository.save(q); });

        // TODO [B6]: Update payload raw
        // payloadRepository.findById(invoiceId).ifPresent(p -> {
        //     p.setResponseJson(callbackPayload);
        //     payloadRepository.save(p);
        // });

        // TODO [B7]: Audit log
        // createAuditLog("HANDLE_CALLBACK", "einv_invoices", invoiceId,
        //                callbackPayload, AUDIT_SUCCESS, null);

        log.warn("[handleCallback] Not yet implemented for invoiceId={}", invoiceId);
    }


    // 4. Query Methods
    @Override
    @Transactional(readOnly = true)
    public ListInvoicesResponse getInvoices(GetInvoicesRequest request) {
        log.info("[getInvoices] STUB - tenantId={}", request.getTenantId());
        // TODO: Implement JpaSpecification filter từ GetInvoicesRequest fields:
        //   tenantId, storeId, providerId, statusId, fromDate, toDate, keyword
        //   → invoiceRepository.findAll(spec, pageable)
        //   → Map Page<EinvInvoiceEntity> → ListInvoicesResponse (kèm pagination meta)
        log.warn("[getInvoices] Not yet implemented");
        return ListInvoicesResponse.builder().build();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EinvInvoiceDto> getInvoiceById(String invoiceId) {
        return invoiceRepository.findWithDetailsById(invoiceId)
                                .map(invoiceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public GetInvoicesResponse getInvoiceFromProvider(GetInvoicesRequest request) {
        log.info("[getInvoiceFromProvider] STUB - invoiceId={}", request.getInvoiceId());
        // TODO: Gọi Provider API với lệnh GET_INVOICE (BKAV cmd=800)
        //   → Cập nhật trạng thái nếu khác DB (on-demand sync)
        log.warn("[getInvoiceFromProvider] Not yet implemented");
        return GetInvoicesResponse.builder().build();
    }

    
    // 5. cancelInvoice
    @Override
    @Transactional
    public EinvInvoiceDto cancelInvoice(String invoiceId, String reason) {
        log.info("[cancelInvoice] invoiceId={}, reason={}", invoiceId, reason);

        EinvInvoiceEntity invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new BusinessException("einv.error.invoice_not_found"));

        if (invoice.getStatusId() == null || invoice.getStatusId() != INV_STATUS_PUBLISHED) {
            throw new BusinessException("einv.error.only_published_can_cancel");
        }

        invoice.setStatusId(INV_STATUS_CANCELLED);
        invoice.setNotes(reason);
        invoiceRepository.saveAndFlush(invoice);

        // Đẩy vào SyncQueue để gửi lệnh hủy đến NCC
        EinvSyncQueueEntity cancelQueue = buildSyncQueueEntry(invoice, "CANCEL", invoice.getTenantId());
        syncQueueRepository.save(cancelQueue);

        createAuditLog("CANCEL_INVOICE", "einv_invoices", invoiceId,
                       toJson(invoice), AUDIT_SUCCESS, null);

        log.info("[cancelInvoice] Invoice {} cancelled successfully", invoiceId);
        return invoiceMapper.toDto(invoice);
    }

    
    // 6. createAuditLog – Ghi nhật ký kiểm toán (Fire & Forget)
    @Override
    public void createAuditLog(String action,
                               String entityName,
                               String entityId,
                               String payload,
                               String result,
                               String errorMsg) {
        try {
            EinvAuditLogEntity log_entry = new EinvAuditLogEntity();
            log_entry.setAction(action);
            log_entry.setEntityName(entityName);
            log_entry.setEntityId(entityId);
            log_entry.setPayload(payload);
            log_entry.setResult(result);
            log_entry.setErrorMsg(errorMsg);
            auditLogRepository.save(log_entry);
            log.debug("[audit] Logged action={} entity={}/{} result={}",
                     action, entityName, entityId, result);
        } catch (Exception ex) {
            // KHÔNG throw – audit failure không được rollback transaction nghiệp vụ
            log.warn("[audit] FAILED to write audit log action={} entity={}/{}: {}",
                     action, entityName, entityId, ex.getMessage());
        }
    }

    
    // Private: Validate Submit Request
    private void validateSubmitRequest(SubmitInvoiceRequest request) {
        // [1] Chống trùng partnerInvoiceId trong tenant
        if (invoiceRepository.existsByPartnerInvoiceIdAndTenantId(
                request.getPartnerInvoiceId(), request.getTenantId())) {
            throw new BusinessException(
                "einv.error.duplicate_partner_invoice_id: " + request.getPartnerInvoiceId());
        }

        // [2] Store phải đã tích hợp NCC
        boolean isIntegrated = storeProviderRepository
                .findByStoreId(request.getStoreId())
                .map(e -> e.getStatus() != null && e.getStatus() == 1)
                .orElse(false);
        if (!isIntegrated) {
            throw new BusinessException("einv.error.provider_not_integrated");
        }

        // [3] Phải có dải ký hiệu đang hoạt động (status=1) cho loại hóa đơn này
        boolean hasActiveSerial = serialRepository
                .findByStoreIdAndProviderId(request.getStoreId(), request.getProviderId())
                .stream()
                .anyMatch(s -> s.getStatus() != null && s.getStatus() == 1
                               && s.getInvoiceTypeId() != null
                               && s.getInvoiceTypeId().equals(request.getInvoiceTypeId()));
        if (!hasActiveSerial) {
            throw new BusinessException("einv.error.no_active_serial_for_type");
        }

        // [4] Điều chỉnh / Thay thế: bắt buộc có orgInvoiceId và lý do
        if (request.getReferenceTypeId() != null && request.getReferenceTypeId() != 0) {
            if (request.getOrgInvoiceId() == null || request.getOrgInvoiceId().isBlank()) {
                throw new BusinessException("einv.error.org_invoice_id_required");
            }
            if (request.getOrgInvoiceReason() == null || request.getOrgInvoiceReason().isBlank()) {
                throw new BusinessException("einv.error.org_invoice_reason_required");
            }
        }

        log.debug("[validateSubmitRequest] PASSED for partnerInvoiceId={}",
                  request.getPartnerInvoiceId());
    }

    
    // Private: Entity Builders
    private EinvInvoiceEntity buildInvoiceEntity(SubmitInvoiceRequest request) {
        EinvInvoiceEntity entity = invoiceMapper.toEntity(request);
        entity.setStatusId(INV_STATUS_NEW);
        entity.setIsDraft(false);
        entity.setIsDeleted(false);
        entity.setIsLocked(false);
        // Lấy serial đang active để gán invoiceForm + invoiceSeries
        serialRepository.findByStoreIdAndProviderId(request.getStoreId(), request.getProviderId())
                .stream()
                .filter(s -> s.getStatus() != null && s.getStatus() == 1)
                .findFirst()
                .ifPresent(serial -> {
                    if (entity.getInvoiceForm() == null)   entity.setInvoiceForm(serial.getInvoiceForm());
                    if (entity.getInvoiceSeries() == null) entity.setInvoiceSeries(serial.getInvoiceSerial());
                });
        return entity;
    }

    private void saveRawPayload(String invoiceId, SubmitInvoiceRequest request) {
        try {
            EinvInvoicePayloadEntity payload = new EinvInvoicePayloadEntity();
            payload.setInvoiceId(invoiceId);
            payload.setRequestJson(toJson(request));
            payloadRepository.save(payload);
        } catch (Exception ex) {
            log.warn("[saveRawPayload] Failed for invoiceId={}: {}", invoiceId, ex.getMessage());
        }
    }

    private EinvSyncQueueEntity buildSyncQueueEntry(EinvInvoiceEntity invoice,
                                                    String syncType,
                                                    String tenantId) {
        EinvSyncQueueEntity queue = new EinvSyncQueueEntity();
        queue.setTenantId(tenantId);
        queue.setProviderId(invoice.getProviderId());
        queue.setInvoiceId(invoice.getId());
        queue.setSyncType(syncType);
        queue.setStatus(QUEUE_PENDING);
        queue.setAttemptCount((byte) 0);
        queue.setMaxAttempts((byte) 3);
        queue.setNextRetryAt(LocalDateTime.now());
        return queue;
    }

    
    // Private: Utility
    private String toJson(Object obj) {
        if (obj == null) return null;
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException ex) {
            log.warn("[toJson] Serialization failed: {}", ex.getMessage());
            return "{}";
        }
    }
}
