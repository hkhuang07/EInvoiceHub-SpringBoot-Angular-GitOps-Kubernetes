package vn.softz.app.einvoicehub.service.catalog;

import vn.softz.app.einvoicehub.domain.entity.CategoryTaxType;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface CategoryTaxTypeService extends CatalogService<CategoryTaxType, String> {

    /**@param taxName tên loại thuế (VD: "Thuế suất 10%")
     * @return Optional chứa entity nếu tồn tại*/
    Optional<CategoryTaxType> findByTaxName(String taxName);

    /**@param vat tỷ lệ VAT (VD: {@code new BigDecimal("10.00")})
     * @return Optional chứa entity đầu tiên khớp*/
    Optional<CategoryTaxType> findByVat(BigDecimal vat);

    /**
     * Lấy tất cả loại thuế có VAT không null.
     * Loại bỏ "Không chịu thuế" và "Không kê khai" (vat = null).
     * Dùng cho dropdown chọn thuế suất trên form hóa đơn.
     *
     * @return danh sách loại thuế có tỷ lệ VAT cụ thể
     */
    List<CategoryTaxType> findAllWithVat();

    /**
     * Kiểm tra tên đã tồn tại chưa (dùng trong validate trước create/update).
     *
     * @param taxName tên cần kiểm tra
     * @return {@code true} nếu đã tồn tại
     */
    boolean existsByTaxName(String taxName);
}
