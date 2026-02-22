package com.einvoicehub.core.service;

import com.einvoicehub.core.domain.entity.EinvInvoiceRegistrationEntity;
import com.einvoicehub.core.domain.entity.EinvMerchantEntity;
import com.einvoicehub.core.domain.entity.EinvRegistrationStatusEntity;
import com.einvoicehub.core.domain.repository.EinvInvoiceRegistrationRepository;
import com.einvoicehub.core.domain.repository.EinvInvoiceTemplateRepository;
import com.einvoicehub.core.domain.repository.EinvMerchantRepository;
import com.einvoicehub.core.domain.repository.EinvRegistrationStatusRepository;
import com.einvoicehub.core.dto.EinvInvoiceRegistrationRequest;
import com.einvoicehub.core.dto.EinvInvoiceRegistrationResponse;
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
public class EinvInvoiceRegistrationService {

    private final EinvInvoiceRegistrationRepository repository;
    private final EinvMerchantRepository merchantRepository;
    private final EinvRegistrationStatusRepository statusRepository;
    private final EinvInvoiceTemplateRepository templateRepository;
    private final EinvHubMapper mapper;

    @Transactional(readOnly = true)
    public List<EinvInvoiceRegistrationResponse> getAll() {
        log.info("[Registration] Lấy danh sách toàn bộ các dải số đã đăng ký");
        return repository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EinvInvoiceRegistrationResponse getById(Long id) {
        log.info("[Registration] Truy vấn chi tiết đăng ký ID: {}", id);
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new InvalidDataException(ErrorCode.INVALID_DATA, "Không tìm thấy dải số đăng ký"));
    }

    @Transactional
    public EinvInvoiceRegistrationResponse create(EinvInvoiceRegistrationRequest request) {
        log.info("[Registration] Đang tạo đăng ký mới cho Merchant ID: {}, Số TB: {}",
                request.getMerchantId(), request.getRegistrationNumber());

        // 1. Kiểm tra trạng thái Merchant (Phải còn hoạt động)
        EinvMerchantEntity merchant = merchantRepository.findById(request.getMerchantId())
                .filter(m -> !m.getIsDeleted())
                .orElseThrow(() -> new InvalidDataException(ErrorCode.MERCHANT_NOT_FOUND));

        // 2. Validate dải số: From <= To và Quantity khớp
        validateRange(request.getFromNumber(), request.getToNumber(), request.getQuantity());

        // 3. Kiểm tra trạng thái đăng ký
        EinvRegistrationStatusEntity status = statusRepository.findById(request.getStatusId())
                .orElseThrow(() -> new InvalidDataException(ErrorCode.INVALID_DATA, "Trạng thái đăng ký không hợp lệ"));

        EinvInvoiceRegistrationEntity entity = mapper.toEntity(request);
        entity.setMerchant(merchant);
        entity.setStatus(status);
        entity.setCurrentNumber(0L); // Khởi tạo số đã dùng là 0

        entity = repository.save(entity);
        log.info("[Registration] Đã lưu thành công dải số ID: {}", entity.getId());
        return mapper.toResponse(entity);
    }

    @Transactional
    public EinvInvoiceRegistrationResponse update(Long id, EinvInvoiceRegistrationRequest request) {
        log.info("[Registration] Cập nhật đăng ký ID: {}", id);

        EinvInvoiceRegistrationEntity entity = repository.findById(id)
                .orElseThrow(() -> new InvalidDataException(ErrorCode.INVALID_DATA, "Dữ liệu không tồn tại"));

        if (Boolean.TRUE.equals(entity.getMerchant().getIsDeleted())) {
            throw new InvalidDataException(ErrorCode.INVALID_DATA, "Doanh nghiệp đã ngừng hoạt động, không thể sửa");
        }

        validateRange(request.getFromNumber(), request.getToNumber(), request.getQuantity());

        entity.setRegistrationNumber(request.getRegistrationNumber());
        entity.setFromNumber(request.getFromNumber());
        entity.setToNumber(request.getToNumber());
        entity.setQuantity(request.getQuantity());
        entity.setEffectiveDate(request.getEffectiveDate());
        entity.setExpirationDate(request.getExpirationDate());
        entity.setIssuedBy(request.getIssuedBy());
        entity.setNote(request.getNote());

        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public void delete(Long id) {
        log.warn("[Registration] Yêu cầu xóa dải số ID: {}", id);

        if (!repository.existsById(id)) {
            throw new InvalidDataException(ErrorCode.INVALID_DATA, "Bản ghi không tồn tại");
        }

        // Kiểm tra ràng buộc tham chiếu từ bảng invoice_template
        if (templateRepository.existsByRegistrationId(id)) {
            log.error("[Registration] Xóa thất bại: Dải số ID {} đang được dùng trong Mẫu hóa đơn", id);
            throw new InvalidDataException(ErrorCode.INVALID_DATA, "Dải số này đã được sử dụng để cấu hình mẫu hóa đơn, không thể xóa");
        }

        repository.deleteById(id);
        log.info("[Registration] Đã xóa thành công dải số ID: {}", id);
    }

    private void validateRange(Long from, Long to, Long qty) {
        if (from == null || to == null || from > to) {
            throw new InvalidDataException(ErrorCode.INVALID_DATA, "Dải số không hợp lệ (Số bắt đầu phải nhỏ hơn hoặc bằng số kết thúc)");
        }
        if (qty != null && (to - from + 1) != qty) {
            throw new InvalidDataException(ErrorCode.INVALID_DATA, "Số lượng không khớp với dải số (To - From + 1 != Quantity)");
        }
    }
}