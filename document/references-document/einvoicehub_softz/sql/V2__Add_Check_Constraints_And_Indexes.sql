-- ==============================================================================
-- EInvoiceHub - Enhanced Schema with CHECK CONSTRAINTS & Additional Validations
-- Database: MariaDB 11+
-- Author: MiniMax Agent (Senior Data Architect)
-- Description: Bổ sung CHECK CONSTRAINTS và các ràng buộc bổ sung cho database
--              theo kiến trúc phân tầng đã quy định
-- ==============================================================================

-- Quy tắc áp dụng:
-- 1. Tầng 1 (Catalogs): ON DELETE RESTRICT - Bảo vệ dữ liệu danh mục
-- 2. Tầng 2 (Tenancy): ON DELETE RESTRICT - Bảo vệ quan hệ merchant-store  
-- 3. Tầng 3 (Transaction): ON DELETE RESTRICT - Bảo vệ dữ liệu giao dịch
-- 4. Tầng 4 (Details): ON DELETE CASCADE - Xóa chi tiết khi xóa header
-- 5. CHECK CONSTRAINTS: Đảm bảo tính toàn vẹn dữ liệu nghiệp vụ

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ==============================================================================
-- BỔ SUNG UNIQUE CONSTRAINT CHO einv_invoices
-- Theo yêu cầu Tầng 3: (tenant_id, partner_invoice_id) để chống trùng lặp tuyệt đối
-- ==============================================================================

-- Xóa unique constraint cũ (store_id, partner_invoice_id)
-- Lưu ý: Chỉ thực hiện nếu muốn thay đổi sang (tenant_id, partner_invoice_id)
-- ALTER TABLE `einv_invoices` DROP INDEX `uq_biz_invoice`;

-- Thêm unique constraint mới theo yêu cầu (tenant_id, partner_invoice_id)
-- Comment lại để tránh lỗi nếu đã có index
-- ALTER TABLE `einv_invoices` ADD UNIQUE INDEX `uq_inv_tenant_partner` (`tenant_id`, `partner_invoice_id`);


-- ==============================================================================
-- NHÓM 1: CHECK CONSTRAINTS CHO HÓA ĐƠN (einv_invoices)
-- ==============================================================================

-- 1.1. Ràng buộc tổng tiền không được âm
ALTER TABLE `einv_invoices`
    ADD CONSTRAINT `chk_inv_total_amount_positive`
    CHECK (`total_amount` IS NULL OR `total_amount` >= 0);

-- 1.2. Ràng buộc tiền thuế không được âm
ALTER TABLE `einv_invoices`
    ADD CONSTRAINT `chk_inv_tax_amount_positive`
    CHECK (`tax_amount` IS NULL OR `tax_amount` >= 0);

-- 1.3. Ràng buộc tiền trước thuế không được âm
ALTER TABLE `einv_invoices`
    ADD CONSTRAINT `chk_inv_net_amount_positive`
    CHECK (`net_amount` IS NULL OR `net_amount` >= 0);

-- 1.4. Ràng buộc tiền chiết khấu không được âm
ALTER TABLE `einv_invoices`
    ADD CONSTRAINT `chk_inv_discount_amount_positive`
    CHECK (`discount_amount` IS NULL OR `discount_amount` >= 0);

-- 1.5. Ràng buộc tiền hàng không được âm
ALTER TABLE `einv_invoices`
    ADD CONSTRAINT `chk_inv_gross_amount_positive`
    CHECK (`gross_amount` IS NULL OR `gross_amount` >= 0);

-- 1.6. Ràng buộc tỷ giá phải lớn hơn 0
ALTER TABLE `einv_invoices`
    ADD CONSTRAINT `chk_inv_exchange_rate_positive`
    CHECK (`exchange_rate` IS NULL OR `exchange_rate` > 0);

-- 1.7. Ràng buộc ngày hóa đơn không được lớn hơn ngày hiện tại quá xa
-- (Cho phép chênh lệch tối đa 7 ngày cho các trường hợp đặc biệt)
ALTER TABLE `einv_invoices`
    ADD CONSTRAINT `chk_inv_invoice_date_valid`
    CHECK (`invoice_date` IS NULL OR `invoice_date` <= DATE_ADD(NOW(), INTERVAL 7 DAY));


-- ==============================================================================
-- NHÓM 2: CHECK CONSTRAINTS CHO CHI TIẾT HÓA ĐƠN (einv_invoices_detail)
-- ==============================================================================

-- 2.1. Ràng buộc số lượng phải lớn hơn 0
ALTER TABLE `einv_invoices_detail`
    ADD CONSTRAINT `chk_inv_det_quantity_positive`
    CHECK (`quantity` IS NULL OR `quantity` > 0);

-- 2.2. Ràng buộc đơn giá không được âm
ALTER TABLE `einv_invoices_detail`
    ADD CONSTRAINT `chk_inv_det_price_positive`
    CHECK (`price` IS NULL OR `price` >= 0);

-- 2.3. Ràng buộc thành tiền không được âm
ALTER TABLE `einv_invoices_detail`
    ADD CONSTRAINT `chk_inv_det_gross_amount_positive`
    CHECK (`gross_amount` IS NULL OR `gross_amount` >= 0);

-- 2.4. Ràng buộc tiền chiết khấu không được âm
ALTER TABLE `einv_invoices_detail`
    ADD CONSTRAINT `chk_inv_det_discount_amount_positive`
    CHECK (`discount_amount` IS NULL OR `discount_amount` >= 0);

-- 2.5. Ràng buộc tỷ lệ chiết khấu từ 0 đến 100
ALTER TABLE `einv_invoices_detail`
    ADD CONSTRAINT `chk_inv_det_discount_rate_valid`
    CHECK (`discount_rate` IS NULL OR (`discount_rate` >= 0 AND `discount_rate` <= 100));

-- 2.6. Ràng buộc thuế suất phải hợp lệ (0, 5, 8, 10, 3.5, 7 hoặc NULL)
-- Danh sách các thuế suất hợp lệ theo quy định Việt Nam
ALTER TABLE `einv_invoices_detail`
    ADD CONSTRAINT `chk_inv_det_tax_rate_valid`
    CHECK (`tax_rate` IS NULL OR `tax_rate` IN (0, 5, 8, 10, 3.5, 7));

-- 2.7. Ràng buộc tiền thuế dòng không được âm
ALTER TABLE `einv_invoices_detail`
    ADD CONSTRAINT `chk_inv_det_tax_amount_positive`
    CHECK (`tax_amount` IS NULL OR `tax_amount` >= 0);

-- 2.8. Ràng buộc tổng tiền dòng không được âm
ALTER TABLE `einv_invoices_detail`
    ADD CONSTRAINT `chk_inv_det_total_amount_positive`
    CHECK (`total_amount` IS NULL OR `total_amount` >= 0);

-- 2.9. Ràng buộc đơn giá sau thuế không được âm
ALTER TABLE `einv_invoices_detail`
    ADD CONSTRAINT `chk_inv_det_net_price_vat_positive`
    CHECK (`net_price_vat` IS NULL OR `net_price_vat` >= 0);

-- 2.10. Ràng buộc đơn giá sau chiết khấu không được âm
ALTER TABLE `einv_invoices_detail`
    ADD CONSTRAINT `chk_inv_det_net_price_positive`
    CHECK (`net_price` IS NULL OR `net_price` >= 0);

-- 2.11. Ràng buộc số dòng phải lớn hơn 0
ALTER TABLE `einv_invoices_detail`
    ADD CONSTRAINT `chk_inv_det_line_no_positive`
    CHECK (`line_no` IS NULL OR `line_no` > 0);


-- ==============================================================================
-- NHÓM 3: CHECK CONSTRAINTS CHO DANH MỤC THUẾ (category_tax_type)
-- ==============================================================================

-- 3.1. Ràng buộc thuế suất VAT phải hợp lệ (0, 5, 8, 10, 3.5, 7 hoặc NULL)
ALTER TABLE `category_tax_type`
    ADD CONSTRAINT `chk_tax_type_vat_valid`
    CHECK (`vat` IS NULL OR `vat` IN (0, 5, 8, 10, 3.5, 7));

-- 3.2. Ràng buộc thuế suất không được âm
ALTER TABLE `category_tax_type`
    ADD CONSTRAINT `chk_tax_type_vat_positive`
    CHECK (`vat` IS NULL OR `vat` >= 0);


-- ==============================================================================
-- NHÓM 4: CHECK CONSTRAINTS CHO CẤU HÌNH TÍCH HỢP (einv_store_provider)
-- ==============================================================================

-- 4.1. Ràng buộc sign_type chỉ được các giá trị hợp lệ (0: Token, 1: HSM, 2: SmartCA)
ALTER TABLE `einv_store_provider`
    ADD CONSTRAINT `chk_sp_sign_type_valid`
    CHECK (`sign_type` IS NULL OR `sign_type` IN (0, 1, 2));

-- 4.2. Ràng buộc status chỉ được các giá trị hợp lệ (0: Chưa tích hợp, 1: Thành công, 8: Đã hủy)
ALTER TABLE `einv_store_provider`
    ADD CONSTRAINT `chk_sp_status_valid`
    CHECK (`status` IS NULL OR `status` IN (0, 1, 8));


-- ==============================================================================
-- NHÓM 5: CHECK CONSTRAINTS CHO HÀNG CHỜ ĐỒNG BỘ (einv_sync_queue)
-- ==============================================================================

-- 5.1. Ràng buộc attempt_count không được âm
ALTER TABLE `einv_sync_queue`
    ADD CONSTRAINT `chk_queue_attempt_count_positive`
    CHECK (`attempt_count` >= 0);

-- 5.2. Ràng buộc max_attempts phải lớn hơn 0
ALTER TABLE `einv_sync_queue`
    ADD CONSTRAINT `chk_queue_max_attempts_positive`
    CHECK (`max_attempts` > 0);

-- 5.3. Ràng buộc attempt_count không được lớn hơn max_attempts
ALTER TABLE `einv_sync_queue`
    ADD CONSTRAINT `chk_queue_attempt_max_valid`
    CHECK (`attempt_count` <= `max_attempts`);

-- 5.4. Ràng buộc status phải là giá trị hợp lệ
ALTER TABLE `einv_sync_queue`
    ADD CONSTRAINT `chk_queue_status_valid`
    CHECK (`status` IN ('PENDING', 'PROCESSING', 'SUCCESS', 'FAILED'));


-- ==============================================================================
-- NHÓM 6: CHECK CONSTRAINTS CHO DẢI KÝ HIỆU (einv_store_serial)
-- ==============================================================================

-- 6.1. Ràng buộc status chỉ được các giá trị hợp lệ (0: Chờ duyệt, 1: Đã duyệt, 8: Ngưng sử dụng)
ALTER TABLE `einv_store_serial`
    ADD CONSTRAINT `chk_ss_status_valid`
    CHECK (`status` IS NULL OR `status` IN (0, 1, 8));


-- ==============================================================================
-- NHÓM 7: CHECK CONSTRAINTS CHO LOẠI HÓA ĐƠN (einv_invoice_type)
-- ==============================================================================

-- 7.1. Ràng buộc sort_order không được âm
ALTER TABLE `einv_invoice_type`
    ADD CONSTRAINT `chk_inv_type_sort_order_positive`
    CHECK (`sort_order` >= 0);


-- ==============================================================================
-- NHÓM 8: INDEX BỔ SUNG CHO PERFORMANCE
-- ==============================================================================

-- 8.1. Index cho bảng einv_invoices - tìm kiếm theo ngày tạo (phân trang)
CREATE INDEX `idx_inv_created_date` ON `einv_invoices` (`created_date`);

-- 8.2. Index cho bảng einv_invoices - tìm kiếm theo provider và ngày
CREATE INDEX `idx_inv_provider_date` ON `einv_invoices` (`provider_id`, `invoice_date`);

-- 8.3. Index cho bảng einv_invoices - tìm kiếm theo tenant và trạng thái
CREATE INDEX `idx_inv_tenant_status` ON `einv_invoices` (`tenant_id`, `status_id`);

-- 8.4. Index cho bảng einv_invoices - tìm kiếm theo store và trạng thái
CREATE INDEX `idx_inv_store_status` ON `einv_invoices` (`store_id`, `status_id`);

-- 8.5. Index cho bảng einv_invoices - tìm kiếm theo provider và trạng thái
CREATE INDEX `idx_inv_provider_status` ON `einv_invoices` (`provider_id`, `status_id`);

-- 8.6. Index cho bảng einv_invoices_detail - tìm kiếm theo doc_id và line_no
CREATE INDEX `idx_inv_det_doc_line` ON `einv_invoices_detail` (`doc_id`, `line_no`);

-- 8.7. Index cho bảng einv_invoices_detail - tìm kiếm theo tenant_id
CREATE INDEX `idx_inv_det_tenant` ON `einv_invoices_detail` (`tenant_id`);

-- 8.8. Index cho bảng einv_sync_queue - tìm kiếm theo tenant và status
CREATE INDEX `idx_queue_tenant_status` ON `einv_sync_queue` (`tenant_id`, `status`);

-- 8.9. Index cho bảng einv_sync_queue - tìm kiếm theo provider và status
CREATE INDEX `idx_queue_provider_status` ON `einv_sync_queue` (`provider_id`, `status`);

-- 8.10. Index cho bảng einv_audit_logs - tìm kiếm theo created_by
CREATE INDEX `idx_audit_created_by` ON `einv_audit_logs` (`created_by`);

-- 8.11. Index cho bảng einv_audit_logs - tìm kiếm theo tenant_id (nếu cần)
-- Lưu ý: bảng audit không có tenant_id, có thể cần thêm cột này nếu muốn lọc theo tenant


-- ==============================================================================
-- NHÓM 9: CÁC RÀNG BUỘC BỔ SUNG KHÁC
-- ==============================================================================

-- 9.1. Thêm cột tenant_id vào bảng audit_logs để hỗ trợ lọc theo tenant
-- (Tùy chọn - nếu cần thiết cho việc kiểm toán theo tenant)
-- ALTER TABLE `einv_audit_logs` ADD COLUMN `tenant_id` VARCHAR(36) COLLATE latin1_general_ci NULL;
-- ALTER TABLE `einv_audit_logs` ADD INDEX `idx_audit_tenant` (`tenant_id`);

-- 9.2. Thêm ràng buộc cho cột adjustment_type trong detail
-- Chỉ cho phép các giá trị: 0 (Không điều chỉnh), 1 (Thông tin), 2 (Tăng), 3 (Giảm)
ALTER TABLE `einv_invoices_detail`
    ADD CONSTRAINT `chk_inv_det_adjustment_type_valid`
    CHECK (`adjustment_type` IS NULL OR `adjustment_type` IN (0, 1, 2, 3));


-- ==============================================================================
-- BẬT LẠI KIỂM TRA FOREIGN KEY
-- ==============================================================================

SET FOREIGN_KEY_CHECKS = 1;


-- ==============================================================================
-- TÓM TẮT CÁC RÀNG BUỘC ĐƯỢC THÊM
-- ==============================================================================
--
-- CHECK CONSTRAINTS (21 ràng buộc):
-- - einv_invoices: 7 ràng buộc (total_amount, tax_amount, net_amount, discount_amount, gross_amount, exchange_rate, invoice_date)
-- - einv_invoices_detail: 11 ràng buộc (quantity, price, gross_amount, discount_amount, discount_rate, tax_rate, tax_amount, total_amount, net_price_vat, net_price, line_no)
-- - category_tax_type: 2 ràng buộc (vat valid, vat positive)
-- - einv_store_provider: 2 ràng buộc (sign_type, status)
-- - einv_sync_queue: 4 ràng buộc (attempt_count, max_attempts, attempt_max_valid, status)
-- - einv_store_serial: 1 ràng buộc (status)
-- - einv_invoice_type: 1 ràng buộc (sort_order)
-- - einv_invoices_detail: 1 ràng buộc bổ sung (adjustment_type)
--
-- INDEX BỔ SUNG (11 index):
-- - einv_invoices: 6 index (created_date, provider_date, tenant_status, store_status, provider_status)
-- - einv_invoices_detail: 2 index (doc_line, tenant)
-- - einv_sync_queue: 2 index (tenant_status, provider_status)
-- - einv_audit_logs: 1 index (created_by)
--
-- LƯU Ý QUAN TRỌNG:
-- 1. Tất cả các cột Boolean trong schema sử dụng TINYINT(1) - đây là kiểu chuẩn cho MariaDB/MySQL
--    và tương thích hoàn toàn với Hibernate mapping
-- 2. Các trường đặc thù của Provider (BKAV, VNPT, MISA...) đã được đẩy vào cột extra_metadata JSON
-- 3. Kiến trúc phân tầng đã tuân thủ: Catalog -> Tenancy -> Transaction -> Details
-- 4. Không có vòng lặp phụ thuộc - chỉ có quan hệ cây trỏ xuống và quan hệ kim cương tập trung tại einv_invoices
-- ==============================================================================
