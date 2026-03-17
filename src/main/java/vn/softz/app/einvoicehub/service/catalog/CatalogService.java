package vn.softz.app.einvoicehub.service.catalog;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CatalogService<D, ID> {

    /**@param dto dữ liệu cần tạo
     * @return DTO/Entity sau khi lưu (bao gồm ID được sinh)
     * @throws vn.softz.app.einvoicehub.exception.BusinessException nếu trùng mã/tên*/
    D create(D dto);

    /**@param id  ID của bản ghi cần cập nhật
     * @param dto dữ liệu mới
     * @return DTO/Entity sau khi cập nhật
     * @throws vn.softz.app.einvoicehub.exception.BusinessException nếu không tìm thấy*/
    D update(ID id, D dto);

    /**@param id ID của bản ghi cần xóa
     * @throws vn.softz.app.einvoicehub.exception.BusinessException nếu có FK reference (hard-delete)*/
    void delete(ID id);

    /**@param id ID cần tìm
     * @return Optional chứa DTO/Entity nếu tồn tại*/
    Optional<D> findById(ID id);

    List<D> findAll();

    /**@param pageable thông tin phân trang và sắp xếp
     * @return trang kết quả*/
    Page<D> findAll(Pageable pageable);
}
