-- EInvoiceHub — SCHEMA v1
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- NHÓM 1: DANH MỤC HỆ THỐNG (System Catalogs)
-- 1.1. Nhà cung cấp HĐĐT (BKAV, VNPT, MISA, VIETTEL, MOBI ...)
CREATE TABLE `einv_provider`
(
    `id`              VARCHAR(36)  NOT NULL COMMENT 'UUID của NCC',
    `provider_code`   VARCHAR(20)  NOT NULL COMMENT 'Mã ngắn: BKAV, VNPT, MOBI, MISA, VIETTEL',
    `provider_name`   VARCHAR(100) NOT NULL COMMENT 'Tên đầy đủ nhà cung cấp',
    `integration_url` VARCHAR(500) COMMENT 'Endpoint tích hợp chính',
    `url_lookup`      VARCHAR(500) COMMENT 'Base URL tra cứu hóa đơn (ghép với invoice_lookup_code)',
    `is_inactive`     BOOLEAN      NOT NULL DEFAULT FALSE COMMENT 'true = tạm ngưng sử dụng',
    `created_at`      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_provider_code` (`provider_code`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
    COMMENT = 'Danh mục Nhà cung cấp HĐĐT';

-- 1.2. Loại hình hóa đơn
CREATE TABLE `einv_invoice_type`
(
    `id`         INT          NOT NULL COMMENT 'ID loại hóa đơn (integer, vận hành tự quy định)',
    `invoice_type_name`       VARCHAR(100) NOT NULL COMMENT 'Tên loại hóa đơn',
    `sort_order` INT          NOT NULL DEFAULT 0,
    `note`       VARCHAR(500) COMMENT 'Ghi chú nghiệp vụ',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
    COMMENT = 'Danh mục Loại hình hóa đơn điện tử';


-- 1.3. Trạng thái hóa đơn
CREATE TABLE `einv_invoice_status`
(
    `id`          INT          NOT NULL COMMENT 'Mã trạng thái hệ thống HUB',
    `name`        VARCHAR(100) NOT NULL COMMENT 'Tên trạng thái',
    `description` VARCHAR(500) COMMENT 'Mô tả chi tiết',
    `note`        VARCHAR(500) COMMENT 'Ghi chú',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
    COMMENT = 'Danh mục Trạng thái hóa đơn (chuẩn hóa, độc lập NCC)';


-- 1.4. Phương thức thanh toán
CREATE TABLE `einv_payment_method`
(
    `id`   INT          NOT NULL COMMENT 'Mã phương thức thanh toán HUB',
    `name` VARCHAR(100) NOT NULL COMMENT 'Tên phương thức',
    `note` VARCHAR(500) COMMENT 'Ghi chú',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
    COMMENT = 'Danh mục Phương thức thanh toán';

-- NHÓM 2: QUẢN LÝ ĐƠN VỊ KINH DOANH
-- 2.1. Tenant / Merchant
CREATE TABLE `merchants`
(
    `id`           BIGINT       NOT NULL AUTO_INCREMENT COMMENT 'Surrogate PK',
    `tenant_id`    VARCHAR(50)  NOT NULL COMMENT 'Mã định danh Tenant từ hệ thống PosORA',
    `company_name` VARCHAR(255) NOT NULL COMMENT 'Tên doanh nghiệp',
    `tax_code`     VARCHAR(20)  NOT NULL COMMENT 'Mã số thuế chính',
    `is_active`    BOOLEAN      NOT NULL DEFAULT TRUE,
    `created_at`   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_tenant_id` (`tenant_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
    COMMENT = 'Quản lý Merchant/Tenant (đơn vị sử dụng hệ thống)';

-- 2.2. Store / Chi nhánh
CREATE TABLE `einv_stores`
(
    `id`         VARCHAR(36)  NOT NULL COMMENT 'UUID của Store',
    `tenant_id`  BIGINT       NOT NULL COMMENT 'FK → merchants.id',
    `store_name` VARCHAR(255) NOT NULL COMMENT 'Tên chi nhánh',
    `tax_code`   VARCHAR(20) COMMENT 'MST chi nhánh (nếu khác tenant)',
    `is_active`  BOOLEAN      NOT NULL DEFAULT TRUE,
    `created_at` TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_store_tenant` FOREIGN KEY (`tenant_id`) REFERENCES `merchants` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
    COMMENT = 'Quản lý Chi nhánh (Store) thuộc Merchant';


-- NHÓM 3: CẤU HÌNH TÍCH HỢP (Integration Config)
-- 3.1. Liên kết Store ↔ Provider
CREATE TABLE `einv_store_provider`
(
    `id`              VARCHAR(36) NOT NULL COMMENT 'UUID',

    `tenant_id`       BIGINT      NOT NULL COMMENT 'FK → merchants.id (denorm để truy vấn nhanh)',
    `store_id`        VARCHAR(36) NOT NULL COMMENT 'FK → einv_stores.id',

    `provider_id`     VARCHAR(36) NOT NULL COMMENT 'FK → einv_provider.id',

    `partner_id`      VARCHAR(255) COMMENT 'BKAV: PartnerGUID | VNPT: Account',
    `partner_token`   VARCHAR(500) COMMENT 'BKAV: PartnerToken | VNPT: ACPass',
    `partner_usr`     VARCHAR(255) COMMENT 'MISA: app_id | VNPT: username | VIETTEL: username',
    `partner_pwd`     VARCHAR(500) COMMENT 'VIETTEL: password | VNPT: password',

    `integration_url` VARCHAR(500) COMMENT 'Override URL riêng cho VNPT (mỗi đối tác có link riêng)',
    `tax_code`        VARCHAR(20) COMMENT 'MST nhà bán hàng (mặc định từ tenant, cho phép override)',

    `status`          INT         NOT NULL DEFAULT 0 COMMENT '0=Chưa tích hợp | 1=Thành công | 8=Inactive',
    `integrated_date` DATETIME(6) COMMENT 'Ngày xác thực tích hợp thành công',

    `created_by`      VARCHAR(100) COMMENT 'Người tạo',
    `created_date`    TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Ngày tạo',
    `updated_by`      VARCHAR(100) COMMENT 'Người cập nhật cuối',
    `updated_date`    TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Ngày cập nhật',
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_sp_store` FOREIGN KEY (`store_id`) REFERENCES `einv_stores` (`id`),
    CONSTRAINT `fk_sp_provider` FOREIGN KEY (`provider_id`) REFERENCES `einv_provider` (`id`),
    INDEX `idx_sp_tenant` (`tenant_id`),
    INDEX `idx_sp_store` (`store_id`),
    INDEX `idx_sp_provider` (`provider_id`),
    INDEX `idx_sp_status` (`status`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
    COMMENT = 'Cấu hình tích hợp Store ↔ NCC HĐĐT (16 trường theo spec 4.2/4.3)';


-- 3.2. Dải ký hiệu / Mẫu số hóa đơn theo Store
CREATE TABLE `einv_store_serial`
(
    `id`                 VARCHAR(36) NOT NULL COMMENT 'UUID',

    `tenant_id`          BIGINT      NOT NULL COMMENT 'FK → merchants.id',
    `store_id`           VARCHAR(36) NOT NULL COMMENT 'FK → einv_stores.id',

    `provider_id`        VARCHAR(36) NOT NULL COMMENT 'FK → einv_provider.id',
    `invoice_type_id`    INT         NOT NULL COMMENT 'FK → einv_invoice_type.id',

    `provider_serial_id` VARCHAR(100) COMMENT 'ID dải ký hiệu bên NCC cấp',
    `invoice_form`       VARCHAR(50) COMMENT 'Mẫu số hóa đơn (VD: 1/001, 2/001)',
    `invoice_serial`     VARCHAR(20) COMMENT 'Ký hiệu hóa đơn (VD: C25TAA, K25TYY)',

    `start_date`         DATETIME(6) COMMENT 'Ngày bắt đầu hiệu lực',
    `status`             INT         NOT NULL DEFAULT 1 COMMENT '1=Active | 0=Inactive',

    `created_by`         VARCHAR(100),
    `created_date`       TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_by`         VARCHAR(100),
    `updated_date`       TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_ss_store` FOREIGN KEY (`store_id`) REFERENCES `einv_stores` (`id`),
    CONSTRAINT `fk_ss_provider` FOREIGN KEY (`provider_id`) REFERENCES `einv_provider` (`id`),
    CONSTRAINT `fk_ss_invoice_type` FOREIGN KEY (`invoice_type_id`) REFERENCES `einv_invoice_type` (`id`),
    INDEX `idx_ss_store_provider` (`store_id`, `provider_id`, `status`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
    COMMENT = 'Quản lý Dải ký hiệu / Mẫu số hóa đơn theo Store (thay đổi hàng năm theo NĐ70)';


-- NHÓM 4: HỆ THỐNG MAPPING DỮ LIỆU (Conversion Layer)
-- 4.1. Mapping Loại thuế / VAT (TaxType)
CREATE TABLE `einv_mapping_tax_type`
(
    `id`            BIGINT       NOT NULL AUTO_INCREMENT,
    `provider_id`   VARCHAR(36)  NOT NULL COMMENT 'FK → einv_provider.id',
    `system_code`   VARCHAR(50)  NOT NULL COMMENT 'Mã thuế nội bộ HUB (VD: VAT0, VAT5, VAT10, EXEMPT)',
    `provider_code` VARCHAR(100) NOT NULL COMMENT 'Mã tương ứng bên NCC',
    `description`   VARCHAR(255) COMMENT 'Mô tả ánh xạ',
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_map_tax_provider` FOREIGN KEY (`provider_id`) REFERENCES `einv_provider` (`id`),
    UNIQUE KEY `uq_map_tax` (`provider_id`, `system_code`),
    INDEX `idx_map_tax_lookup` (`provider_id`, `system_code`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
    COMMENT = 'Mapping: Mã loại thuế (HUB) ↔ (NCC)';


-- 4.2. Mapping Loại hóa đơn (InvoiceType)
CREATE TABLE `einv_mapping_invoice_type`
(
    `id`            BIGINT       NOT NULL AUTO_INCREMENT,
    `provider_id`   VARCHAR(36)  NOT NULL COMMENT 'FK → einv_provider.id',
    `system_code`   VARCHAR(50)  NOT NULL COMMENT 'Mã loại HĐ nội bộ HUB',
    `provider_code` VARCHAR(100) NOT NULL COMMENT 'Mã tương ứng bên NCC',
    `description`   VARCHAR(255),
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_map_inv_type_provider` FOREIGN KEY (`provider_id`) REFERENCES `einv_provider` (`id`),
    UNIQUE KEY `uq_map_inv_type` (`provider_id`, `system_code`),
    INDEX `idx_map_inv_type_lookup` (`provider_id`, `system_code`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
    COMMENT = 'Mapping: Loại hóa đơn (HUB) ↔ (NCC)';


-- 4.3. Mapping Phương thức thanh toán (PaymentMethod)
-- VD: HUB "CASH" → BKAV "TM" | MOBI "1"

CREATE TABLE `einv_mapping_payment_method`
(
    `id`            BIGINT       NOT NULL AUTO_INCREMENT,
    `provider_id`   VARCHAR(36)  NOT NULL COMMENT 'FK → einv_provider.id',
    `system_code`   VARCHAR(50)  NOT NULL COMMENT 'Mã PTTT nội bộ HUB',
    `provider_code` VARCHAR(100) NOT NULL COMMENT 'Mã tương ứng bên NCC',
    `description`   VARCHAR(255),
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_map_pay_provider` FOREIGN KEY (`provider_id`) REFERENCES `einv_provider` (`id`),
    UNIQUE KEY `uq_map_pay` (`provider_id`, `system_code`),
    INDEX `idx_map_pay_lookup` (`provider_id`, `system_code`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
    COMMENT = 'Mapping: Phương thức thanh toán (HUB) ↔ (NCC)';


-- 4.4. Mapping Loại hàng hóa / dòng HĐ (ItemType)
-- VD: HUB "GOODS" → BKAV "1" | MOBI "2"

CREATE TABLE `einv_mapping_item_type`
(
    `id`            BIGINT       NOT NULL AUTO_INCREMENT,
    `provider_id`   VARCHAR(36)  NOT NULL COMMENT 'FK → einv_provider.id',
    `system_code`   VARCHAR(50)  NOT NULL COMMENT 'Mã loại item nội bộ HUB',
    `provider_code` VARCHAR(100) NOT NULL COMMENT 'Mã tương ứng bên NCC',
    `description`   VARCHAR(255),
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_map_item_provider` FOREIGN KEY (`provider_id`) REFERENCES `einv_provider` (`id`),
    UNIQUE KEY `uq_map_item` (`provider_id`, `system_code`),
    INDEX `idx_map_item_lookup` (`provider_id`, `system_code`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
    COMMENT = 'Mapping: Loại hàng hóa trên dòng HĐ (HUB) ↔ (NCC)';


-- 4.5. Mapping Loại tham chiếu (ReferenceType) — điều chỉnh / thay thế
-- VD: HUB "ADJUST" → BKAV "3" | MOBI "3"
-- Ref: Tài liệu 4.3 — Step 4 (SubmitInvoice), field Data.ReferenceTypeID

CREATE TABLE `einv_mapping_reference_type`
(
    `id`            BIGINT       NOT NULL AUTO_INCREMENT,
    `provider_id`   VARCHAR(36)  NOT NULL COMMENT 'FK → einv_provider.id',
    `system_code`   VARCHAR(50)  NOT NULL COMMENT 'Mã reference HUB (VD: ORIGINAL, ADJUST, REPLACE)',
    `provider_code` VARCHAR(100) NOT NULL COMMENT 'Mã tương ứng bên NCC',
    `description`   VARCHAR(255),
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_map_ref_provider` FOREIGN KEY (`provider_id`) REFERENCES `einv_provider` (`id`),
    UNIQUE KEY `uq_map_ref` (`provider_id`, `system_code`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
    COMMENT = 'Mapping: Loại tham chiếu HĐ điều chỉnh/thay thế (HUB) ↔ (NCC)';


-- NHÓM 5: NGHIỆP VỤ HÓA ĐƠN
-- 5.1. Hóa đơn
CREATE TABLE `einv_invoices`
(
    `id`                  BIGINT      NOT NULL AUTO_INCREMENT COMMENT 'Surrogate PK',

    `tenant_id`           BIGINT      NOT NULL COMMENT 'FK → merchants.id',
    `store_id`            VARCHAR(36) NOT NULL COMMENT 'FK → einv_stores.id',
    `provider_id`         VARCHAR(36) COMMENT 'FK → einv_provider.id (NCC được dùng khi phát sinh HĐ)',
    -- Định danh nghiệp vụ
    `partner_invoice_id`  VARCHAR(50) NOT NULL COMMENT 'ID hóa đơn gốc từ POS (PartnerInvoiceID)',
    `provider_invoice_id` VARCHAR(50) COMMENT 'ID hóa đơn do NCC cấp (cập nhật sau Step 5)',
    -- Phân loại
    `invoice_type_id`     INT COMMENT 'FK → einv_invoice_type.id',
    `reference_type_id`   INT         NOT NULL DEFAULT 0 COMMENT '0=Gốc | 1=Điều chỉnh | 2=Thay thế',
    `payment_method_id`   INT COMMENT 'FK → einv_payment_method.id',

    `status_id`           INT         NOT NULL DEFAULT 0 COMMENT 'FK → einv_invoice_status.id',

    -- Thông tin hóa đơn — điền sau khi NCC phản hồi
    `invoice_form`        VARCHAR(50) COMMENT 'Mẫu số hóa đơn (VD: 1/001)',
    `invoice_series`      VARCHAR(20) COMMENT 'Ký hiệu hóa đơn (VD: C25TAA)',
    `invoice_no`          VARCHAR(20) COMMENT 'Số hóa đơn',
    `invoice_date`        DATETIME(6) COMMENT 'Ngày lập hóa đơn',
    `signed_date`         DATETIME(6) COMMENT 'Ngày ký số phát hành',
    `tax_authority_code`  VARCHAR(100) COMMENT 'Mã CQT (Cơ quan Thuế) cấp cho HĐ',
    `invoice_lookup_code` VARCHAR(50) COMMENT 'Mã tra cứu HĐ (kết hợp einv_provider.url_lookup + code này)',
    -- Thông tin người mua
    `buyer_tax_code`      VARCHAR(50) COMMENT 'MST người mua',
    `buyer_company`       VARCHAR(300) COMMENT 'Tên công ty người mua',
    `buyer_id_no`         VARCHAR(20) COMMENT 'CMND/CCCD người mua (cá nhân)',
    `buyer_full_name`     VARCHAR(200) COMMENT 'Họ tên người mua',
    `buyer_address`       VARCHAR(300) COMMENT 'Địa chỉ người mua',
    `buyer_mobile`        VARCHAR(50) COMMENT 'SĐT người mua',
    `buyer_bank_account`  VARCHAR(50) COMMENT 'Số TK ngân hàng người mua',
    `buyer_bank_name`     VARCHAR(200) COMMENT 'Tên ngân hàng người mua',
    `buyer_budget_code`   VARCHAR(20) COMMENT 'Mã chương ngân sách (đơn vị Nhà nước)',
    -- Nhận HĐ qua email
    `receive_type_id`     INT COMMENT '0=Không gửi | 1=Gửi email',
    `receiver_email`      VARCHAR(300) COMMENT 'Email nhận HĐ',
    -- Tiền tệ
    `currency_code`       VARCHAR(20) COMMENT 'Mã tiền tệ (VD: VND, USD)',
    `exchange_rate`       DECIMAL(15, 6) COMMENT 'Tỷ giá quy đổi',
    -- HĐ gốc được điều chỉnh/thay thế
    `org_invoice_id`      VARCHAR(36) COMMENT 'ID HĐ gốc trong hệ thống HUB (self-reference)',
    `org_invoice_form`    VARCHAR(50) COMMENT 'Mẫu số HĐ gốc',
    `org_invoice_series`  VARCHAR(20) COMMENT 'Ký hiệu HĐ gốc',
    `org_invoice_no`      VARCHAR(50) COMMENT 'Số HĐ gốc',
    `org_invoice_date`    DATETIME(6) COMMENT 'Ngày lập HĐ gốc',
    `org_invoice_reason`  VARCHAR(500) COMMENT 'Lý do điều chỉnh / thay thế',
    -- Tổng tiền
    `gross_amount`        DECIMAL(15, 2) COMMENT 'Tổng tiền hàng trước CK',
    `discount_amount`     DECIMAL(15, 2) COMMENT 'Tổng chiết khấu',
    `net_amount`          DECIMAL(15, 2) COMMENT 'Tiền sau CK, trước thuế (gross - discount)',
    `tax_amount`          DECIMAL(15, 2) COMMENT 'Tổng tiền thuế VAT',
    `total_amount`        DECIMAL(15, 2) COMMENT 'Tổng thanh toán (net + tax)',
    -- [Audit]
    `created_by`          VARCHAR(100),
    `created_at`          TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_by`          VARCHAR(100),
    `updated_at`          TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    -- [Index tối ưu truy vấn Multi-tenant]
    UNIQUE KEY `uq_biz_invoice` (`store_id`, `partner_invoice_id`) COMMENT 'Không cho phép 1 Store gửi HĐ trùng POS ID',
    INDEX `idx_inv_tenant` (`tenant_id`),
    INDEX `idx_inv_store` (`store_id`),
    INDEX `idx_inv_provider` (`provider_id`),
    INDEX `idx_inv_status` (`status_id`),
    INDEX `idx_inv_lookup_code` (`invoice_lookup_code`),
    INDEX `idx_inv_partner_inv` (`partner_invoice_id`),
    CONSTRAINT `fk_inv_store` FOREIGN KEY (`store_id`) REFERENCES `einv_stores` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
    COMMENT = 'Hóa đơn điện tử (Header) — trung tâm nghiệp vụ của HUB';


-- 5.2. Dòng hàng hóa trên hóa đơn
CREATE TABLE `einv_invoices_detail`
(
    `id`              BIGINT       NOT NULL AUTO_INCREMENT COMMENT 'Surrogate PK',
    `doc_id`          BIGINT       NOT NULL COMMENT 'FK → einv_invoices.id',

    `line_no`         INT COMMENT 'Số thứ tự dòng trên HĐ',
    -- Thông tin hàng hóa / dịch vụ
    `item_id`         VARCHAR(36) COMMENT 'Mã hàng hóa trong hệ thống POS',
    `item_name`       VARCHAR(500) NOT NULL COMMENT 'Tên hàng hóa / dịch vụ',
    `item_type_id`    INT COMMENT 'Loại dòng HĐ (mapping sang NCC)',
    `is_free`         BOOLEAN COMMENT 'true = hàng khuyến mãi, không tính tiền',
    `unit`            VARCHAR(50) COMMENT 'Đơn vị tính',
    -- Số lượng & giá
    `quantity`        DECIMAL(15, 6) COMMENT 'Số lượng',
    `price`           DECIMAL(15, 6) COMMENT 'Đơn giá chưa CK (= gross_amount / quantity)',
    -- Tiền
    `gross_amount`    DECIMAL(15, 2) COMMENT 'Thành tiền chưa CK (quantity × price)',
    `discount_rate`   DECIMAL(15, 2) COMMENT 'Tỷ lệ chiết khấu (%)',
    `discount_amount` DECIMAL(15, 2) COMMENT 'Tiền chiết khấu',
    `net_amount`      DECIMAL(15, 2) COMMENT 'Thành tiền sau CK (gross - discount)',
    `net_price`       DECIMAL(15, 6) COMMENT 'Đơn giá sau CK (= net_amount / quantity)',
    -- Thuế
    `tax_type_id`     VARCHAR(36) COMMENT 'Mã loại thuế HUB (mapping sang NCC)',
    `tax_rate`        DECIMAL(15, 2) COMMENT 'Thuế suất (%)',
    `tax_amount`      DECIMAL(15, 2) COMMENT 'Tiền thuế',

    `net_price_vat`   DECIMAL(15, 6) COMMENT 'Đơn giá sau CK + thuế (= total_amount / quantity)',
    `total_amount`    DECIMAL(15, 2) COMMENT 'Tổng thanh toán dòng (net_amount + tax_amount)',
    -- [Ghi chú]
    `notes`           VARCHAR(500) COMMENT 'Ghi chú riêng cho dòng hàng',
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_detail_invoice` FOREIGN KEY (`doc_id`) REFERENCES `einv_invoices` (`id`) ON DELETE CASCADE,
    INDEX `idx_detail_doc` (`doc_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
    COMMENT = 'Dòng hàng hóa trên Hóa đơn (Detail) — xóa theo HĐ cha (CASCADE)';


-- NHÓM 6: LƯU TRỮ PAYLOAD & VẬN HÀNH
-- 6.1. Lưu trữ Payload gốc
CREATE TABLE `einv_invoice_payloads`
(
    `invoice_id`    BIGINT    NOT NULL COMMENT 'FK → einv_invoices.id (1-1)',
    `request_json`  LONGTEXT COMMENT 'Bản tin JSON gửi lên NCC',
    `request_xml`   LONGTEXT COMMENT 'Bản tin XML gửi lên NCC (VNPT)',
    `response_json` LONGTEXT COMMENT 'Phản hồi thô từ NCC',
    `signed_xml`    LONGTEXT COMMENT 'XML đã ký số (nếu có)',
    `pdf_data`      LONGTEXT COMMENT 'Dữ liệu PDF base64 (nếu NCC trả về)',
    `created_at`    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`invoice_id`),
    CONSTRAINT `fk_payload_invoice` FOREIGN KEY (`invoice_id`) REFERENCES `einv_invoices` (`id`) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
    COMMENT = 'Lưu payload thô (request/response) để debug và audit — xóa theo HĐ';


-- 6.2. Hàng chờ đồng bộ
CREATE TABLE `einv_sync_queue`
(
    `id`            BIGINT      NOT NULL AUTO_INCREMENT,
    `invoice_id`    BIGINT      NOT NULL COMMENT 'FK → einv_invoices.id',
    `sync_type`     VARCHAR(50) NOT NULL COMMENT 'SUBMIT | SIGN | GET_STATUS | GET_INVOICE',
    `status`        VARCHAR(20) NOT NULL DEFAULT 'PENDING' COMMENT 'PENDING | PROCESSING | SUCCESS | FAILED',
    `attempt_count` INT         NOT NULL DEFAULT 0 COMMENT 'Số lần đã retry',
    `max_attempts`  INT         NOT NULL DEFAULT 3 COMMENT 'Giới hạn retry',
    `last_error`    TEXT COMMENT 'Thông báo lỗi lần cuối',
    `next_retry_at` TIMESTAMP COMMENT 'Thời điểm retry tiếp theo',
    `created_at`    TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`    TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_queue_invoice` FOREIGN KEY (`invoice_id`) REFERENCES `einv_invoices` (`id`),
    INDEX `idx_queue_status_retry` (`status`, `next_retry_at`),
    INDEX `idx_queue_invoice` (`invoice_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
    COMMENT = 'Hàng chờ xử lý bất đồng bộ (retry Submit/Sign/GetStatus tự động)';


-- 6.3. Nhật ký kiểm toán
CREATE TABLE `einv_audit_logs`
(
    `id`          BIGINT       NOT NULL AUTO_INCREMENT,
    `action`      VARCHAR(100) NOT NULL COMMENT 'VD: SUBMIT_INVOICE, SIGN_INVOICE, GET_STATUS',
    `entity_name` VARCHAR(100) COMMENT 'Tên bảng liên quan',
    `entity_id`   VARCHAR(100) COMMENT 'ID bản ghi bị tác động',
    `payload`     JSON COMMENT 'Dữ liệu tại thời điểm action',
    `result`      VARCHAR(20) COMMENT 'SUCCESS | FAILURE',
    `error_msg`   TEXT COMMENT 'Chi tiết lỗi nếu thất bại',
    `created_by`  VARCHAR(100) COMMENT 'User/System thực hiện action',
    `created_at`  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    INDEX `idx_audit_entity` (`entity_name`, `entity_id`),
    INDEX `idx_audit_action` (`action`),
    INDEX `idx_audit_created` (`created_at`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
    COMMENT = 'Nhật ký kiểm toán toàn bộ hành động trên hệ thống';


-- NHÓM 7: VIEW HỖ TRỢ
/* 7.1. View thông tin User (ánh xạ từ hệ thống auth của PosORA)
CREATE OR REPLACE VIEW `vw_sys_user_author` AS
SELECT id,
       name,
       full_name AS fullName
FROM `sys_users`
WHERE is_active = TRUE;*/


INSERT INTO `einv_invoice_status` (`id`, `name`, `description`, `note`)
VALUES (0, 'Chưa gửi NCC', 'HĐ đã lưu trong HUB, chưa gửi lên NCC', NULL),
       (1, 'Đã gửi NCC - chờ ký', 'NCC đã nhận HĐ, chờ ký số phát hành', NULL),
       (2, 'Đã phát hành', 'HĐ đã ký số và có hiệu lực pháp lý', 'Trạng thái cuối hợp lệ'),
       (3, 'HĐ Điều chỉnh đã ký', 'HĐ điều chỉnh đã được ký số', NULL),
       (4, 'HĐ Thay thế đã ký', 'HĐ thay thế đã được ký số', NULL),
       (5, 'Bị điều chỉnh', 'HĐ này đã bị một HĐ điều chỉnh khác tham chiếu', NULL),
       (6, 'Bị thay thế', 'HĐ này đã bị một HĐ thay thế khác tham chiếu', NULL),
       (8, 'Hủy / Xóa', 'HĐ đã bị hủy hoặc xóa bởi NCC', NULL),
       (9, 'Lỗi', 'Xảy ra lỗi trong quá trình gửi/ký', 'Cần kiểm tra sync_queue');

INSERT INTO `einv_payment_method` (`id`, `name`, `note`)
VALUES (1, 'Tiền mặt', 'TM'),
       (2, 'Chuyển khoản', 'CK'),
       (3, 'Tiền mặt/Chuyển khoản', 'TM/CK'),
       (4, 'Thẻ', NULL),
       (5, 'Khác', NULL);


SET FOREIGN_KEY_CHECKS = 1;
