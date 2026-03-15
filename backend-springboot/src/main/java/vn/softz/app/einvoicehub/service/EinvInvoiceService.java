package vn.softz.app.einvoicehub.service;

import vn.softz.app.einvoicehub.dto.EinvInvoiceDto;
import vn.softz.app.einvoicehub.dto.request.GetInvoicesRequest;
import vn.softz.app.einvoicehub.dto.request.SignInvoicesRequest;
import vn.softz.app.einvoicehub.dto.request.SubmitInvoiceRequest;
import vn.softz.app.einvoicehub.dto.response.GetInvoicesResponse;
import vn.softz.app.einvoicehub.dto.response.ListInvoicesResponse;
import vn.softz.app.einvoicehub.dto.response.SignInvoiceResponse;
import vn.softz.app.einvoicehub.dto.response.SubmitInvoiceResponse;

import java.util.Optional;

/**
 * Service nghiệp vụ cốt lõi của EInvoiceHub.
 *
 * <h3>Luồng nghiệp vụ chính</h3>
 * <pre>
 *   [POS/Client]
 *       │ submitInvoice(request)
 *       ▼
 *   [EInvoiceHub] Validate → Lưu Hub DB → Đẩy vào SyncQueue (status=PENDING)
 *       │
 *       │  [Async Worker / Scheduler]
 *       │  signInvoice(invoiceId)
 *       ▼
 *   [Provider API] Gọi ký số (BKAV, MobiFone, VNPT...)
 *       │
 *       │  [Callback / Polling]
 *       │  handleCallback(invoiceId, callbackPayload)
 *       ▼
 *   [EInvoiceHub] Cập nhật trạng thái + Ghi AuditLog
 * </pre>
 *
 * <h3>Audit</h3>
 * Mọi hành động write đều gọi {@link #createAuditLog} để ghi vào
 * {@code einv_audit_logs}. Log này là immutable append-only.
 */
public interface EinvInvoiceService {

    /**
     * Nhận yêu cầu xuất hóa đơn từ POS/Client.
     *
     * <p>Luồng:
     * <ol>
     *   <li>Validate dữ liệu đầu vào (số tiền, dải ký hiệu, trùng partnerInvoiceId…).
     *   <li>Lưu hóa đơn vào {@code einv_invoices} với {@code status = 1 (Mới tạo)}.
     *   <li>Lưu payload thô vào {@code einv_invoice_payloads}.
     *   <li>Đẩy vào {@code einv_sync_queue} với {@code syncType = "SUBMIT"}.
     *   <li>Ghi AuditLog hành động {@code "SUBMIT_INVOICE"}.
     * </ol>
     *
     * @param request dữ liệu hóa đơn từ client
     * @return kết quả submit (invoiceId HUB, trạng thái ban đầu)
     */
    SubmitInvoiceResponse submitInvoice(SubmitInvoiceRequest request);

    /**
     * Ký số hóa đơn bằng NCC (gọi từ Async Worker hoặc trực tiếp).
     *
     * <p>Luồng:
     * <ol>
     *   <li>Load hóa đơn từ DB, kiểm tra trạng thái hợp lệ để ký (status = 1).
     *   <li>Load cấu hình NCC ({@code einv_store_provider}) cho Store.
     *   <li>Gọi Provider API (BKAV SOAP / MobiFone HTTP / VNPT WS).
     *   <li>Cập nhật {@code provider_invoice_id}, {@code invoice_no}, {@code signed_date}.
     *   <li>Cập nhật trạng thái hóa đơn → {@code 2 (Đã phát hành)} hoặc giữ queue.
     *   <li>Ghi AuditLog {@code "SIGN_INVOICE"}.
     * </ol>
     *
     * @param invoiceIds  danh sách ID hóa đơn cần ký
     * @param request     tham số ký (loại chữ ký, HSM config…)
     * @return kết quả ký cho từng hóa đơn
     */
    SignInvoiceResponse signInvoice(SignInvoicesRequest request);

    /**
     * Tiếp nhận callback/phản hồi từ NCC sau khi xử lý.
     *
     * <p>Luồng:
     * <ol>
     *   <li>Xác thực chữ ký / token của callback (tránh giả mạo).
     *   <li>Mapping mã trạng thái NCC → mã Hub qua {@link EinvMappingInvoiceStatusService}.
     *   <li>Cập nhật {@code einv_invoices}: status, taxAuthorityCode, invoiceLookupCode.
     *   <li>Cập nhật {@code einv_sync_queue}: status = SUCCESS / FAILED.
     *   <li>Ghi AuditLog {@code "HANDLE_CALLBACK"}.
     * </ol>
     *
     * @param invoiceId       ID hóa đơn Hub
     * @param callbackPayload JSON thô từ NCC / CQT
     * @param providerId      ID của NCC gửi callback
     */
    void handleCallback(String invoiceId, String callbackPayload, String providerId);

    /**
     * Tra cứu hóa đơn theo bộ lọc (phân trang).
     *
     * @param request bộ lọc (tenantId, storeId, ngày, trạng thái…)
     * @return danh sách hóa đơn có phân trang
     */
    ListInvoicesResponse getInvoices(GetInvoicesRequest request);

    /**
     * Lấy chi tiết một hóa đơn (bao gồm dòng hàng hóa).
     *
     * @param invoiceId ID hóa đơn Hub
     * @return DTO đầy đủ hoặc empty nếu không tìm thấy
     */
    Optional<EinvInvoiceDto> getInvoiceById(String invoiceId);

    /**
     * Lấy thông tin hóa đơn từ NCC (on-demand query).
     *
     * <p>Dùng khi cần đồng bộ trạng thái mà không chờ callback,
     * ví dụ: user bấm "Làm mới trạng thái" trên giao diện.
     *
     * @param request chứa invoiceId và providerId
     * @return thông tin hóa đơn từ NCC
     */
    GetInvoicesResponse getInvoiceFromProvider(GetInvoicesRequest request);

    /**
     * Hủy hóa đơn đã phát hành.
     *
     * <p>Điều kiện: hóa đơn phải có {@code status = 2 (Đã phát hành)}.
     * Sau khi hủy: {@code status = 3 (Đã hủy)}, ghi AuditLog {@code "CANCEL_INVOICE"}.
     *
     * @param invoiceId ID hóa đơn cần hủy
     * @param reason    lý do hủy (ghi vào notes)
     * @return DTO hóa đơn sau khi hủy
     */
    EinvInvoiceDto cancelInvoice(String invoiceId, String reason);

    /**
     * Ghi nhật ký kiểm toán vào {@code einv_audit_logs}.
     *
     * <p>Được gọi nội bộ sau mọi hành động write. Không throw exception
     * để không ảnh hưởng transaction chính — lỗi audit chỉ log warn.
     *
     * @param action     tên hành động: SUBMIT_INVOICE, SIGN_INVOICE, CANCEL_INVOICE…
     * @param entityName tên bảng bị tác động: "einv_invoices"
     * @param entityId   ID của bản ghi bị tác động
     * @param payload    snapshot JSON của bản ghi tại thời điểm hành động
     * @param result     "SUCCESS" hoặc "FAILURE"
     * @param errorMsg   chi tiết lỗi (null nếu SUCCESS)
     */
    void createAuditLog(String action,
                        String entityName,
                        String entityId,
                        String payload,
                        String result,
                        String errorMsg);

    // PLACEHOLDER CLASS – xóa file này sau khi đã merge vào các Repository thực.
    final class RepositoryExtensionNotes {
        private RepositoryExtensionNotes() {}
    }
}
