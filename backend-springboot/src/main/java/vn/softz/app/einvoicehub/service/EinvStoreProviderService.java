package vn.softz.app.einvoicehub.service;

import vn.softz.app.einvoicehub.dto.EinvStoreProviderDto;
import vn.softz.app.einvoicehub.dto.EinvValidationResult;
import vn.softz.app.einvoicehub.dto.request.EinvStoreProviderRequest;

import java.util.List;
import java.util.Optional;

/**
 * Service quản lý cấu hình tích hợp Store ↔ NCC HĐĐT.
 *
 * <p>Mỗi Store chỉ được cấu hình <b>một</b> NCC tại một thời điểm.
 * Luồng nghiệp vụ: saveConfig → validateConfig → deactivate.
 *
 * <p>Bảo mật: {@code password_service} và {@code partner_pwd} luôn được
 * mã hóa bằng {@link org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder}
 * trước khi lưu vào DB. Giá trị gốc KHÔNG bao giờ được persist.
 */
public interface EinvStoreProviderService {

    /**
     * Lấy cấu hình hiện tại của Store đang đăng nhập (tenant-aware).
     *
     * @return {@link Optional} chứa DTO nếu tồn tại
     */
    Optional<EinvStoreProviderDto> getConfig();

    /**
     * Lấy cấu hình theo storeId (dùng nội bộ / admin).
     */
    Optional<EinvStoreProviderDto> getConfigByStoreId(String storeId);

    /**
     * Lấy danh sách cấu hình theo tenantId (admin view).
     */
    List<EinvStoreProviderDto> getConfigsByTenant(String tenantId);

    /**
     * Tạo mới hoặc cập nhật cấu hình NCC cho Store.
     *
     * <p>Logic:
     * <ol>
     *   <li>Upsert entity theo {@code storeId}.
     *   <li>Mã hóa {@code passwordService} và {@code partnerPwd} bằng BCrypt.
     *   <li>Đặt {@code status = 0} (Chưa tích hợp).
     *   <li>Ghi {@code EinvStoreProviderHistory} với {@code actionType = "CREATE"/"UPDATE"}.
     * </ol>
     *
     * @param request dữ liệu cấu hình từ client
     * @return DTO sau khi lưu (mật khẩu đã bị che)
     */
    EinvStoreProviderDto saveConfig(EinvStoreProviderRequest request);

    /**
     * Xác thực kết nối với NCC (gọi API test).
     *
     * <p>Nếu thành công: {@code status = 1}, {@code integratedDate = now()}.
     * Nếu thất bại: trả về {@link EinvValidationResult} chứa thông báo lỗi.
     *
     * @param request chứa thông tin NCC cần kiểm tra
     * @return kết quả validation
     */
    EinvValidationResult validateConfig(EinvStoreProviderRequest request);

    /**
     * Hủy tích hợp (deactivate) cấu hình NCC của Store.
     *
     * <p>Điều kiện: Không được hủy nếu còn dải ký hiệu có {@code status = 1}.
     * Nếu hủy thành công: {@code status = 8}, xóa {@code integratedDate}.
     *
     * @return kết quả nghiệp vụ
     */
    EinvValidationResult deactivate();

    /**
     * Kiểm tra Store có cấu hình NCC đang hoạt động (status = 1) hay không.
     *
     * @param storeId ID của Store
     * @return {@code true} nếu đã tích hợp
     */
    boolean isIntegrated(String storeId);
}
