package vn.softz.app.einvoicehub.service;

import vn.softz.app.einvoicehub.dto.EinvMappingInvoiceStatusDto;

import java.util.List;
import java.util.Optional;

/**
 * Service quản lý bảng ánh xạ trạng thái hóa đơn HUB ↔ NCC.
 *
 * <p>Mỗi NCC (BKAV, MobiFone, VNPT…) sử dụng bộ mã trạng thái riêng.
 * Bảng này là "từ điển" chuyển đổi hai chiều:
 * <ul>
 *   <li><b>Hub → Provider</b>: khi gửi lệnh lên NCC, cần biết mã trạng thái NCC tương ứng.</li>
 *   <li><b>Provider → Hub</b>: khi nhận callback/response từ NCC, cần chuẩn hóa về mã Hub.</li>
 * </ul>
 *
 * <h3>Ví dụ</h3>
 * <pre>
 *   BKAV trạng thái "5" ↔ Hub trạng thái 2 ("Đã phát hành")
 *   MobiFone trạng thái "SIGNED" ↔ Hub trạng thái 2
 * </pre>
 */
public interface EinvMappingInvoiceStatusService {

    // ── CRUD ──────────────────────────────────────────────────────────────────

    /**
     * Tạo mới một bản ghi mapping.
     *
     * <p>Validate: không được tồn tại mapping trùng
     * {@code (providerId, invoiceStatusId)}.
     *
     * @param dto dữ liệu mapping cần tạo
     * @return DTO sau khi lưu (bao gồm ID được sinh)
     * @throws vn.softz.core.exception.BusinessException nếu trùng
     */
    EinvMappingInvoiceStatusDto create(EinvMappingInvoiceStatusDto dto);

    /**
     * Cập nhật bản ghi mapping theo ID.
     *
     * @param id  ID của bản ghi cần cập nhật
     * @param dto dữ liệu mới (null field → giữ nguyên)
     * @return DTO sau khi cập nhật
     * @throws vn.softz.core.exception.BusinessException nếu không tìm thấy
     */
    EinvMappingInvoiceStatusDto update(String id, EinvMappingInvoiceStatusDto dto);

    /**
     * Soft-delete: đặt {@code inactive = true}.
     *
     * @param id ID của bản ghi cần xóa
     */
    void delete(String id);

    /**
     * Tìm mapping theo ID.
     *
     * @param id ID của bản ghi
     * @return {@link Optional} chứa DTO nếu tồn tại
     */
    Optional<EinvMappingInvoiceStatusDto> findById(String id);

    /**
     * Lấy toàn bộ mapping theo NCC (bao gồm cả inactive).
     *
     * @param providerId ID của NCC
     * @return danh sách DTO
     */
    List<EinvMappingInvoiceStatusDto> findAllByProvider(String providerId);

    /**
     * Lấy danh sách mapping active theo NCC.
     *
     * @param providerId ID của NCC
     * @return danh sách DTO có {@code inactive = false}
     */
    List<EinvMappingInvoiceStatusDto> findActiveByProvider(String providerId);

    // ── Lookup (core mapping logic) ───────────────────────────────────────────

    /**
     * Tra cứu mã trạng thái NCC từ mã HUB (Hub → Provider).
     *
     * <p>Dùng khi cần gửi lệnh đến NCC và cần điền mã trạng thái theo quy ước của họ.
     *
     * @param providerId      ID của NCC (VD: UUID của BKAV)
     * @param hubStatusId     mã trạng thái Hub (VD: 2 = "Đã phát hành")
     * @return mã trạng thái bên NCC, hoặc {@link Optional#empty()} nếu chưa cấu hình
     */
    Optional<String> findProviderStatusId(String providerId, Byte hubStatusId);

    /**
     * Tra cứu mã trạng thái HUB từ mã NCC (Provider → Hub).
     *
     * <p>Dùng khi nhận callback/response từ NCC để chuẩn hóa về mã Hub
     * trước khi lưu vào {@code einv_invoices.status_id}.
     *
     * @param providerId        ID của NCC
     * @param providerStatusId  mã trạng thái bên NCC (VD: "5", "SIGNED")
     * @return mã trạng thái Hub dạng Byte, hoặc {@link Optional#empty()} nếu chưa map
     */
    Optional<Byte> findHubStatusId(String providerId, String providerStatusId);

    /**
     * Tra cứu mã trạng thái NCC từ mã HUB – trả về fallback nếu không tìm thấy.
     *
     * @param providerId  ID của NCC
     * @param hubStatusId mã trạng thái Hub
     * @param fallback    giá trị trả về nếu không tìm thấy mapping
     * @return mã NCC hoặc {@code fallback}
     */
    String findProviderStatusIdOrDefault(String providerId, Byte hubStatusId, String fallback);

    /**
     * Tra cứu mã Hub từ mã NCC – trả về fallback nếu không tìm thấy.
     *
     * @param providerId       ID của NCC
     * @param providerStatusId mã trạng thái NCC
     * @param fallback         giá trị trả về nếu không tìm thấy mapping
     * @return mã Hub hoặc {@code fallback}
     */
    Byte findHubStatusIdOrDefault(String providerId, String providerStatusId, Byte fallback);
}
