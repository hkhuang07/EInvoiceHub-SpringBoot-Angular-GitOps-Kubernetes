
-- EInvoiceHub - Foreign Key Constraints
-- Database: MariaDB 11+

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;


-- NHÓM 1: DANH MỤC HỆ THỐNG (System Catalogs)
-- Không có foreign key - đây là các bảng gốc (root tables)



-- NHÓM 2: QUẢN LÝ ĐƠN VỊ KINH DOANH
-- Tier 1: merchants (bảng gốc - không có FK)
-- Tier 2: einv_stores (tham chiếu merchants)


-- 2.1. Bảng einv_stores tham chiếu merchants
-- Ràng buộc: Một store phải thuộc về một merchant hợp lệ
ALTER TABLE `einv_stores`
    ADD CONSTRAINT `fk_store_merchant`
    FOREIGN KEY (`tenant_id`) REFERENCES `merchants` (`tenant_id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE;

-- Tạo index cho foreign key (nếu chưa có)
CREATE INDEX `idx_store_tenant_fk` ON `einv_stores` (`tenant_id`);



-- NHÓM 3: CẤU HÌNH TÍCH HỢP (Integration Config)
-- Tier 1: merchants, einv_stores, einv_provider, einv_invoice_type (tham chiếu)
-- Tier 2: einv_store_provider, einv_store_provider_history, einv_store_serial


-- 3.1. Bảng einv_store_provider
-- 3.1.1. FK: tenant_id -> merchants
ALTER TABLE `einv_store_provider`
    ADD CONSTRAINT `fk_sp_merchant`
    FOREIGN KEY (`tenant_id`) REFERENCES `merchants` (`tenant_id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE;

-- 3.1.2. FK: store_id -> einv_stores
ALTER TABLE `einv_store_provider`
    ADD CONSTRAINT `fk_sp_store`
    FOREIGN KEY (`store_id`) REFERENCES `einv_stores` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE;

-- 3.1.3. FK: provider_id -> einv_provider
ALTER TABLE `einv_store_provider`
    ADD CONSTRAINT `fk_sp_provider`
    FOREIGN KEY (`provider_id`) REFERENCES `einv_provider` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE;

-- 3.2. Bảng einv_store_provider_history
-- 3.2.1. FK: tenant_id -> merchants
ALTER TABLE `einv_store_provider_history`
    ADD CONSTRAINT `fk_sph_merchant`
    FOREIGN KEY (`tenant_id`) REFERENCES `merchants` (`tenant_id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE;

-- 3.2.2. FK: store_id -> einv_stores
ALTER TABLE `einv_store_provider_history`
    ADD CONSTRAINT `fk_sph_store`
    FOREIGN KEY (`store_id`) REFERENCES `einv_stores` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE;

-- 3.2.3. FK: provider_id -> einv_provider
ALTER TABLE `einv_store_provider_history`
    ADD CONSTRAINT `fk_sph_provider`
    FOREIGN KEY (`provider_id`) REFERENCES `einv_provider` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE;

-- 3.3. Bảng einv_store_serial
-- 3.3.1. FK: tenant_id -> merchants
ALTER TABLE `einv_store_serial`
    ADD CONSTRAINT `fk_ss_merchant`
    FOREIGN KEY (`tenant_id`) REFERENCES `merchants` (`tenant_id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE;

-- 3.3.2. FK: store_id -> einv_stores
ALTER TABLE `einv_store_serial`
    ADD CONSTRAINT `fk_ss_store`
    FOREIGN KEY (`store_id`) REFERENCES `einv_stores` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE;

-- 3.3.3. FK: provider_id -> einv_provider
ALTER TABLE `einv_store_serial`
    ADD CONSTRAINT `fk_ss_provider`
    FOREIGN KEY (`provider_id`) REFERENCES `einv_provider` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE;

-- 3.3.4. FK: invoice_type_id -> einv_invoice_type
ALTER TABLE `einv_store_serial`
    ADD CONSTRAINT `fk_ss_invoice_type`
    FOREIGN KEY (`invoice_type_id`) REFERENCES `einv_invoice_type` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE;



-- NHÓM 4: HỆ THỐNG MAPPING DỮ LIỆU
-- Tier 1: einv_provider, các bảng danh mục (tham chiếu)
-- Tier 2: einv_mapping_* (bảng mapping)


-- 4.1. Bảng einv_mapping_invoice_status
-- 4.1.1. FK: provider_id -> einv_provider
ALTER TABLE `einv_mapping_invoice_status`
    ADD CONSTRAINT `fk_mis_provider`
    FOREIGN KEY (`provider_id`) REFERENCES `einv_provider` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE;

-- 4.1.2. FK: invoice_status_id -> einv_invoice_status
ALTER TABLE `einv_mapping_invoice_status`
    ADD CONSTRAINT `fk_mis_invoice_status`
    FOREIGN KEY (`invoice_status_id`) REFERENCES `einv_invoice_status` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE;

-- 4.2. Bảng einv_mapping_invoice_type
-- 4.2.1. FK: provider_id -> einv_provider
ALTER TABLE `einv_mapping_invoice_type`
    ADD CONSTRAINT `fk_mit_provider`
    FOREIGN KEY (`provider_id`) REFERENCES `einv_provider` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE;

-- 4.2.2. FK: invoice_type_id -> einv_invoice_type
ALTER TABLE `einv_mapping_invoice_type`
    ADD CONSTRAINT `fk_mit_invoice_type`
    FOREIGN KEY (`invoice_type_id`) REFERENCES `einv_invoice_type` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE;

-- 4.3. Bảng einv_mapping_payment_method
-- 4.3.1. FK: provider_id -> einv_provider
ALTER TABLE `einv_mapping_payment_method`
    ADD CONSTRAINT `fk_mpm_provider`
    FOREIGN KEY (`provider_id`) REFERENCES `einv_provider` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE;

-- 4.3.2. FK: payment_method_id -> einv_payment_method
ALTER TABLE `einv_mapping_payment_method`
    ADD CONSTRAINT `fk_mpm_payment_method`
    FOREIGN KEY (`payment_method_id`) REFERENCES `einv_payment_method` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE;

-- 4.4. Bảng einv_mapping_item_type
-- 4.4.1. FK: provider_id -> einv_provider
ALTER TABLE `einv_mapping_item_type`
    ADD CONSTRAINT `fk_mit_item_provider`
    FOREIGN KEY (`provider_id`) REFERENCES `einv_provider` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE;

-- 4.4.2. FK: item_type_id -> einv_item_type
ALTER TABLE `einv_mapping_item_type`
    ADD CONSTRAINT `fk_mit_item_type`
    FOREIGN KEY (`item_type_id`) REFERENCES `einv_item_type` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE;

-- 4.5. Bảng einv_mapping_tax_type
-- 4.5.1. FK: provider_id -> einv_provider
ALTER TABLE `einv_mapping_tax_type`
    ADD CONSTRAINT `fk_mtt_provider`
    FOREIGN KEY (`provider_id`) REFERENCES `einv_provider` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE;

-- 4.5.2. FK: tax_type_id -> category_tax_type
ALTER TABLE `einv_mapping_tax_type`
    ADD CONSTRAINT `fk_mtt_tax_type`
    FOREIGN KEY (`tax_type_id`) REFERENCES `category_tax_type` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE;

-- 4.6. Bảng einv_mapping_reference_type
-- 4.6.1. FK: provider_id -> einv_provider
ALTER TABLE `einv_mapping_reference_type`
    ADD CONSTRAINT `fk_mrt_provider`
    FOREIGN KEY (`provider_id`) REFERENCES `einv_provider` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE;

-- 4.6.2. FK: reference_type_id -> einv_reference_type
ALTER TABLE `einv_mapping_reference_type`
    ADD CONSTRAINT `fk_mrt_reference_type`
    FOREIGN KEY (`reference_type_id`) REFERENCES `einv_reference_type` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE;

-- 4.7. Bảng einv_mapping_unit
-- 4.7.1. FK: provider_id -> einv_provider
ALTER TABLE `einv_mapping_unit`
    ADD CONSTRAINT `fk_mu_provider`
    FOREIGN KEY (`provider_id`) REFERENCES `einv_provider` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE;

-- 4.7.2. FK: unit_code -> einv_unit
ALTER TABLE `einv_mapping_unit`
    ADD CONSTRAINT `fk_mu_unit`
    FOREIGN KEY (`unit_code`) REFERENCES `einv_unit` (`code`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE;

-- 4.8. Bảng einv_mapping_action
-- 4.8.1. FK: provider_id -> einv_provider
ALTER TABLE `einv_mapping_action`
    ADD CONSTRAINT `fk_ma_provider`
    FOREIGN KEY (`provider_id`) REFERENCES `einv_provider` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE;



-- NHÓM 5: NGHIỆP VỤ HÓA ĐƠN
-- Tier 1: merchants, einv_stores, einv_provider, các bảng danh mục (tham chiếu)
-- Tier 2: einv_invoices (bảng header - xử lý đặc biệt)
-- Tier 3: einv_invoices_detail (bảng detail - CASCADE khi xóa header)


-- 5.1. Bảng einv_invoices
-- 5.1.1. FK: tenant_id -> merchants
ALTER TABLE `einv_invoices`
    ADD CONSTRAINT `fk_inv_merchant`
    FOREIGN KEY (`tenant_id`) REFERENCES `merchants` (`tenant_id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE;

-- 5.1.2. FK: store_id -> einv_stores
ALTER TABLE `einv_invoices`
    ADD CONSTRAINT `fk_inv_store`
    FOREIGN KEY (`store_id`) REFERENCES `einv_stores` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE;

-- 5.1.3. FK: provider_id -> einv_provider
ALTER TABLE `einv_invoices`
    ADD CONSTRAINT `fk_inv_provider`
    FOREIGN KEY (`provider_id`) REFERENCES `einv_provider` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE;

-- 5.1.4. FK: invoice_type_id -> einv_invoice_type
ALTER TABLE `einv_invoices`
    ADD CONSTRAINT `fk_inv_invoice_type`
    FOREIGN KEY (`invoice_type_id`) REFERENCES `einv_invoice_type` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE;

-- 5.1.5. FK: reference_type_id -> einv_reference_type
ALTER TABLE `einv_invoices`
    ADD CONSTRAINT `fk_inv_reference_type`
    FOREIGN KEY (`reference_type_id`) REFERENCES `einv_reference_type` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE;

-- 5.1.6. FK: status_id -> einv_invoice_status
ALTER TABLE `einv_invoices`
    ADD CONSTRAINT `fk_inv_status`
    FOREIGN KEY (`status_id`) REFERENCES `einv_invoice_status` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE;

-- 5.1.7. FK: payment_method_id -> einv_payment_method
ALTER TABLE `einv_invoices`
    ADD CONSTRAINT `fk_inv_payment_method`
    FOREIGN KEY (`payment_method_id`) REFERENCES `einv_payment_method` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE;

-- 5.1.8. FK: receive_type_id -> einv_receive_type
ALTER TABLE `einv_invoices`
    ADD CONSTRAINT `fk_inv_receive_type`
    FOREIGN KEY (`receive_type_id`) REFERENCES `einv_receive_type` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE;

-- 5.1.9. FK: tax_status_id -> einv_tax_status
ALTER TABLE `einv_invoices`
    ADD CONSTRAINT `fk_inv_tax_status`
    FOREIGN KEY (`tax_status_id`) REFERENCES `einv_tax_status` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE;

-- 5.1.10. FK: submit_invoice_type -> einv_submit_invoice_type
ALTER TABLE `einv_invoices`
    ADD CONSTRAINT `fk_inv_submit_type`
    FOREIGN KEY (`submit_invoice_type`) REFERENCES `einv_submit_invoice_type` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE;

-- 5.1.11. FK: org_invoice_id -> einv_invoices (Self-reference cho hóa đơn điều chỉnh/thay thế)
-- Lưu ý: FK này cần ON DELETE RESTRICT vì không được phép xóa hóa đơn gốc khi có hóa đơn điều chỉnh
ALTER TABLE `einv_invoices`
    ADD CONSTRAINT `fk_inv_org_invoice`
    FOREIGN KEY (`org_invoice_id`) REFERENCES `einv_invoices` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE;

-- 5.2. Bảng einv_invoices_detail
-- Lưu ý: Đã có FK doc_id trong schema gốc, kiểm tra và bổ sung nếu cần

-- 5.2.1. FK: tenant_id -> merchants
ALTER TABLE `einv_invoices_detail`
    ADD CONSTRAINT `fk_inv_det_merchant`
    FOREIGN KEY (`tenant_id`) REFERENCES `merchants` (`tenant_id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE;

-- 5.2.2. FK: store_id -> einv_stores
ALTER TABLE `einv_invoices_detail`
    ADD CONSTRAINT `fk_inv_det_store`
    FOREIGN KEY (`store_id`) REFERENCES `einv_stores` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE;

-- 5.2.3. FK: doc_id -> einv_invoices (Đã có trong schema, kiểm tra lại)
-- FK này phải CASCADE vì detail phụ thuộc vào header
-- Schema gốc đã có: CONSTRAINT `einv_invoices_detail_ibfk_1` FOREIGN KEY (`doc_id`) REFERENCES `einv_invoices` (`id`)
-- Cần sửa thành ON DELETE CASCADE
ALTER TABLE `einv_invoices_detail`
    DROP FOREIGN KEY `einv_invoices_detail_ibfk_1`;

ALTER TABLE `einv_invoices_detail`
    ADD CONSTRAINT `fk_inv_det_invoice`
    FOREIGN KEY (`doc_id`) REFERENCES `einv_invoices` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE;

-- 5.2.4. FK: item_type_id -> einv_item_type
ALTER TABLE `einv_invoices_detail`
    ADD CONSTRAINT `fk_inv_det_item_type`
    FOREIGN KEY (`item_type_id`) REFERENCES `einv_item_type` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE;

-- 5.2.5. FK: tax_type_id -> category_tax_type
ALTER TABLE `einv_invoices_detail`
    ADD CONSTRAINT `fk_inv_det_tax_type`
    FOREIGN KEY (`tax_type_id`) REFERENCES `category_tax_type` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE;



-- NHÓM 6: LƯU TRỮ PAYLOAD & VẬN HÀNH
-- Tier 1: einv_invoices, merchants, einv_provider (tham chiếu)
-- Tier 2: einv_invoice_payloads, einv_sync_queue


-- 6.1. Bảng einv_invoice_payloads
-- Đã có FK trong schema gốc: fk_payload_invoice (ON DELETE CASCADE)
-- Kiểm tra và đảm bảo có index

-- 6.2. Bảng einv_sync_queue
-- 6.2.1. FK: tenant_id -> merchants
ALTER TABLE `einv_sync_queue`
    ADD CONSTRAINT `fk_queue_merchant`
    FOREIGN KEY (`tenant_id`) REFERENCES `merchants` (`tenant_id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE;

-- 6.2.2. FK: provider_id -> einv_provider
ALTER TABLE `einv_sync_queue`
    ADD CONSTRAINT `fk_queue_provider`
    FOREIGN KEY (`provider_id`) REFERENCES `einv_provider` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE;

-- 6.2.3. FK: invoice_id -> einv_invoices
-- Cần CASCADE vì khi xóa hóa đơn, queue liên quan cần được xóa
ALTER TABLE `einv_sync_queue`
    DROP FOREIGN KEY `fk_queue_invoice`;

ALTER TABLE `einv_sync_queue`
    ADD CONSTRAINT `fk_queue_invoice`
    FOREIGN KEY (`invoice_id`) REFERENCES `einv_invoices` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE;

-- 6.3. Bảng einv_audit_logs
-- Bảng audit không có FK để đảm bảo ghi log được cả khi các bảng khác bị xóa



-- TẠO INDEX CHO CÁC CỘT FK (Bổ sung nếu chưa có)


-- Index cho bảng einv_invoices (bổ sung các index còn thiếu)
CREATE INDEX `idx_inv_provider_fk` ON `einv_invoices` (`provider_id`);
CREATE INDEX `idx_inv_invoice_type_fk` ON `einv_invoices` (`invoice_type_id`);
CREATE INDEX `idx_inv_reference_type_fk` ON `einv_invoices` (`reference_type_id`);
CREATE INDEX `idx_inv_payment_method_fk` ON `einv_invoices` (`payment_method_id`);
CREATE INDEX `idx_inv_receive_type_fk` ON `einv_invoices` (`receive_type_id`);
CREATE INDEX `idx_inv_tax_status_fk` ON `einv_invoices` (`tax_status_id`);
CREATE INDEX `idx_inv_submit_type_fk` ON `einv_invoices` (`submit_invoice_type`);
CREATE INDEX `idx_inv_org_invoice_fk` ON `einv_invoices` (`org_invoice_id`);

-- Index cho bảng einv_invoices_detail
CREATE INDEX `idx_inv_det_provider_fk` ON `einv_invoices_detail` (`provider_id`);
CREATE INDEX `idx_inv_det_item_type_fk` ON `einv_invoices_detail` (`item_type_id`);
CREATE INDEX `idx_inv_det_tax_type_fk` ON `einv_invoices_detail` (`tax_type_id`);

-- Index cho bảng einv_sync_queue
CREATE INDEX `idx_queue_tenant_fk` ON `einv_sync_queue` (`tenant_id`);
CREATE INDEX `idx_queue_provider_fk` ON `einv_sync_queue` (`provider_id`);



-- BẬT LẠI KIỂM TRA FOREIGN KEY
SET FOREIGN_KEY_CHECKS = 1;


