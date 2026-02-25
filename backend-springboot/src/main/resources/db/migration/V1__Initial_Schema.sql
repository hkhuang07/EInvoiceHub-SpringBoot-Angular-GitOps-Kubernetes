-- EInvoiceHub — SCHEMA v1
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- NHÓM 1: DANH MỤC HỆ THỐNG (System Catalogs)
-- 1.1. Nhà cung cấp HĐĐT (BKAV, VNPT, MISA, VIETTEL, MOBI ...)
CREATE TABLE `einv_provider`
(
    `id`               VARCHAR(36)  NOT NULL COMMENT 'UUID của NCC',
    `provider_code`    VARCHAR(20)  NOT NULL COMMENT 'Mã ngắn: BKAV, VNPT, MOBI, MISA, VIETTEL',
    `provider_name`    VARCHAR(100) NOT NULL COMMENT 'Tên đầy đủ nhà cung cấp',
    `integration_url`  VARCHAR(500) COMMENT 'Endpoint tích hợp chính',
    `url_lookup`       VARCHAR(500) COMMENT 'Base URL tra cứu hóa đơn (ghép với invoice_lookup_code)',
    `integration_type` INT                   DEFAULT 1 COMMENT '1: Ký 1 bước (HSM - Gửi bản tin là xong), 2: Ký 2 bước (Token/SmartCA - Get Hash -> Sign -> Publish)',
    `is_inactive`      BOOLEAN      NOT NULL DEFAULT FALSE COMMENT 'true = tạm ngưng sử dụng',
    `created_at`       TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_provider_code` (`provider_code`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
    COMMENT = 'Danh mục Nhà cung cấp HĐĐT';

-- 1.2. Loại hình hóa đơn
CREATE TABLE `einv_invoice_type`
(
    `id`                INT          NOT NULL COMMENT 'ID loại hóa đơn',
    `invoice_type_name` VARCHAR(100) NOT NULL COMMENT 'Tên loại hóa đơn',
    `sort_order`        INT          NOT NULL DEFAULT 0,
    `note`              VARCHAR(500) COMMENT 'Ghi chú nghiệp vụ',
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


-- 1.4 Tạo bảng danh mục Trạng thái Cơ quan Thuế (Mục 5.10)
CREATE TABLE `einv_tax_status`
(
    `id`   INT PRIMARY KEY COMMENT 'Mã trạng thái CQT',
    `name` VARCHAR(100) NOT NULL COMMENT 'Tên trạng thái'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='Danh mục trạng thái phản hồi từ CQT (Mục 5.10)';

-- 1.5. Phương thức thanh toán
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
    `id`                  VARCHAR(36) NOT NULL COMMENT 'UUID',

    `tenant_id`           BIGINT      NOT NULL COMMENT 'FK → merchants.id (denorm để truy vấn nhanh)',
    `store_id`            VARCHAR(36) NOT NULL COMMENT 'FK → einv_stores.id',

    `provider_id`         VARCHAR(36) NOT NULL COMMENT 'FK → einv_provider.id',
    `sign_type`           INT                  DEFAULT 0 COMMENT '0:Token, 1:HSM, 2:SmartCA (Theo Excel So sánh CN)',
    `is_two_step_signing` BOOLEAN              DEFAULT FALSE COMMENT 'Ghi đè cấu hình ký 2 bước cho từng chi nhánh',
    `partner_id`          VARCHAR(255) COMMENT 'BKAV: PartnerGUID | VNPT: Account',
    `app_id`              VARCHAR(100) COMMENT 'Dành riêng cho MISA',
    `fkey_prefix`         VARCHAR(50) COMMENT 'Tiền tố Fkey cho VNPT',
    `partner_token`       TEXT COMMENT 'Chứa JWT Token dài của MISA/Viettel',
    `partner_pwd`         TEXT COMMENT 'Password có thể mã hóa dài',
    `partner_usr`         VARCHAR(255) COMMENT 'MISA: app_id | VNPT: username | VIETTEL: username',
    `username_service`    VARCHAR(100) COMMENT 'User API ',
    `password_service`    VARCHAR(255) COMMENT 'Pass API ',

    `integration_url`     VARCHAR(500) COMMENT 'Override URL riêng cho VNPT (mỗi đối tác có link riêng)',
    `tax_code`            VARCHAR(20) COMMENT 'MST nhà bán hàng (mặc định từ tenant, cho phép override)',

    `status`              INT         NOT NULL DEFAULT 0 COMMENT '0:Mới, 1:Đã xác minh (Mã 904),2: Lỗi xác thực, 8:Inactive',
    `integrated_date`     DATETIME(6) COMMENT 'Ngày xác thực tích hợp thành công',

    `created_by`          VARCHAR(100) COMMENT 'Người tạo',
    `created_date`        TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Ngày tạo',
    `updated_by`          VARCHAR(100) COMMENT 'Người cập nhật cuối',
    `updated_date`        TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Ngày cập nhật',
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


-- NHÓM 4: HỆ THỐNG MAPPING DỮ LIỆU
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


-- 4.2. Mapping Loại hóa đơn
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


-- 4.3. Mapping Phương thức thanh toán
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


-- 4.4. Mapping Loại hàng hóa
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

-- 4.5 Mapping Trạng thái
CREATE TABLE `einv_mapping_status`
(
    `id`                   BIGINT      NOT NULL AUTO_INCREMENT,
    `provider_id`          VARCHAR(36) NOT NULL,
    `provider_status_code` VARCHAR(50) NOT NULL COMMENT 'Trạng thái NCC trả về (VD: 1, Success, Sent)',
    `hub_status_id`        INT         NOT NULL COMMENT 'Mã chuẩn của HUB (FK -> einv_invoice_status.id)',
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_map_status_provider` FOREIGN KEY (`provider_id`) REFERENCES `einv_provider` (`id`),
    CONSTRAINT `fk_map_status_hub` FOREIGN KEY (`hub_status_id`) REFERENCES `einv_invoice_status` (`id`)
) ENGINE = InnoDB COMMENT ='Mapping trạng thái hóa đơn NCC ↔ HUB (Tài liệu 4.1)';
/* -- 4.5. Mapping Loại tham chiếu (ReferenceType) — điều chỉnh / thay thế
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
    COMMENT = 'Mapping: Loại tham chiếu HĐ điều chỉnh/thay thế (HUB) ↔ (NCC)';*/

-- 4.6. Mapping Lệnh
CREATE TABLE `einv_mapping_action`
(
    `id`           BIGINT       NOT NULL AUTO_INCREMENT,
    `provider_id`  VARCHAR(36)  NOT NULL,
    `hub_action`   VARCHAR(50)  NOT NULL COMMENT 'SUBMIT, SIGN, CANCEL, REPLACE, ADJUST',
    `provider_cmd` VARCHAR(100) NOT NULL COMMENT 'BKAV: 100 | VNPT: ImportAndPublishInv',
    `description`  VARCHAR(255),
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_map_action_provider` FOREIGN KEY (`provider_id`) REFERENCES `einv_provider` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT = 'Mapping lệnh nghiệp vụ HUB ↔ NCC (Dựa trên Excel Chức năng)';

-- 4. Tạo bảng Mapping cho Đơn vị tính (Unit Mapping)
CREATE TABLE `einv_mapping_unit`
(
    `id`            BIGINT       NOT NULL AUTO_INCREMENT,
    `provider_id`   VARCHAR(36)  NOT NULL,
    `system_code`   VARCHAR(50)  NOT NULL COMMENT 'Mã đơn vị tính HUB',
    `provider_code` VARCHAR(100) NOT NULL COMMENT 'Mã tương ứng bên NCC',
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_map_unit_provider` FOREIGN KEY (`provider_id`) REFERENCES `einv_provider` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT = 'Mapping Đơn vị tính HUB & NCC';


-- NHÓM 5: NGHIỆP VỤ HÓA ĐƠN
-- 5.1. Hóa đơn
CREATE TABLE `einv_invoices`
(
    `id`                   BIGINT      NOT NULL AUTO_INCREMENT COMMENT 'Surrogate PK',
    `tenant_id`            BIGINT      NOT NULL COMMENT 'FK → merchants.id',
    `store_id`             VARCHAR(36) NOT NULL COMMENT 'FK → einv_stores.id',
    `provider_id`          VARCHAR(36) COMMENT 'FK → einv_provider.id',

    `partner_invoice_id`   VARCHAR(50) NOT NULL COMMENT 'ID hóa đơn gốc từ POS (PartnerInvoiceID)',
    `provider_invoice_id`  VARCHAR(50) COMMENT 'ID hóa đơn do NCC cấp',
    `provider_response_id` VARCHAR(100) COMMENT 'ID bản tin của NCC (Phục vụ đối soát Step 6)',

    `status_id`            INT         NOT NULL DEFAULT 0 COMMENT 'FK → einv_invoice_status.id',
    `tax_status_id`        INT                  DEFAULT 0 COMMENT 'FK → einv_tax_status.id',
    `cqt_response_code`    VARCHAR(10) COMMENT 'Mã phản hồi từ CQT (Ví dụ: 100, 101)',
    `is_draft`             BOOLEAN              DEFAULT FALSE,
    `is_mtt`               BOOLEAN              DEFAULT FALSE COMMENT 'Hóa đơn Máy tính tiền',
    `is_petrol`            BOOLEAN              DEFAULT FALSE COMMENT 'Hóa đơn Xăng dầu',
    `is_locked`            BOOLEAN              DEFAULT FALSE COMMENT 'Khóa hóa đơn khi đang xử lý (Tránh trùng lặp Step 1)',

    `invoice_type_id`      INT COMMENT 'FK → einv_invoice_type.id',
    `reference_type_id`    INT         NOT NULL DEFAULT 0 COMMENT '0: Gốc, 2: Điều chỉnh, 3: Thay thế',
    `sign_type`            INT COMMENT '0: Token, 1: HSM',
    `payment_method_id`    INT COMMENT 'FK → einv_payment_method.id',

    `invoice_form`         VARCHAR(50) COMMENT 'Mẫu số (VD: 1/001)',
    `invoice_series`       VARCHAR(20) COMMENT 'Ký hiệu (VD: C25TAA)',
    `invoice_no`           VARCHAR(8) COMMENT 'Số hóa đơn (8 chữ số theo TT78)',
    `invoice_date`         DATETIME(6),
    `signed_date`          DATETIME(6),
    `hash_value`           TEXT COMMENT 'Giá trị Hash để ký số',
    `tax_authority_code`   VARCHAR(100) COMMENT 'Mã CQT cấp (MCCQT)',
    `invoice_lookup_code`  VARCHAR(50) COMMENT 'Mã tra cứu HĐ',
    `response_message`     VARCHAR(500) COMMENT 'Thông báo lỗi/thành công từ NCC',
    `secret_code`          VARCHAR(50) COMMENT 'Mã bí mật (Viettel)',

    `buyer_tax_code`       VARCHAR(50),
    `buyer_code`           VARCHAR(50) COMMENT 'Mã khách hàng',
    `buyer_company`        VARCHAR(300),
    `buyer_full_name`      VARCHAR(200),
    `buyer_address`        VARCHAR(300),
    `buyer_mobile`         VARCHAR(50),
    `buyer_plate_no`       VARCHAR(50) COMMENT 'Biển số xe (Xăng dầu)',
    `extra_metadata`       JSON COMMENT 'Trường động (MISA/VNPT)',
    `delivery_info`        JSON COMMENT 'Thông tin vận chuyển (PXK)',

    `currency_code`        VARCHAR(20)          DEFAULT 'VND',
    `exchange_rate`        DECIMAL(18, 6)       DEFAULT 1.0,
    `gross_amount`         DECIMAL(20, 2),
    `discount_amount`      DECIMAL(20, 2),
    `net_amount`           DECIMAL(20, 2),
    `tax_amount`           DECIMAL(20, 2),
    `total_amount`         DECIMAL(20, 2),
    `total_amount_text`    VARCHAR(500),
    `tax_summary_json`     JSON COMMENT 'Tổng hợp thuế suất cho XML CQT',

    `org_invoice_id`       BIGINT COMMENT 'ID bản ghi HĐ gốc (Self-reference)',
    `org_invoice_form`     VARCHAR(50),
    `org_invoice_series`   VARCHAR(20),
    `org_invoice_no`       VARCHAR(50),
    `org_invoice_date`     DATETIME(6),
    `org_invoice_reason`   VARCHAR(500),

    `created_by`           VARCHAR(100),
    `created_at`           TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_by`           VARCHAR(100),
    `updated_at`           TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_biz_invoice` (`store_id`, `partner_invoice_id`),
    INDEX `idx_inv_lookup_code` (`invoice_lookup_code`),
    CONSTRAINT `fk_inv_store` FOREIGN KEY (`store_id`) REFERENCES `einv_stores` (`id`),
    CONSTRAINT `fk_inv_tax_status` FOREIGN KEY (`tax_status_id`) REFERENCES `einv_tax_status` (`id`),
    CONSTRAINT `fk_inv_org_ref` FOREIGN KEY (`org_invoice_id`) REFERENCES `einv_invoices` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT = 'Hóa đơn điện tử (Header) - HUB Central';

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
    `gross_amount`    DECIMAL(20, 2) COMMENT 'Thành tiền chưa CK (quantity × price)',
    `discount_rate`   DECIMAL(20, 2) COMMENT 'Tỷ lệ chiết khấu (%)',
    `discount_amount` DECIMAL(20, 2) COMMENT 'Tiền chiết khấu',
    `net_amount`      DECIMAL(20, 2) COMMENT 'Thành tiền sau CK (gross - discount)',
    `net_price`       DECIMAL(20, 6) COMMENT 'Đơn giá sau CK (= net_amount / quantity)',
    -- Thuế
    `tax_type_id`     VARCHAR(36) COMMENT 'Mã loại thuế HUB (mapping sang NCC)',
    `tax_rate`        DECIMAL(15, 2) COMMENT 'Thuế suất (%)',
    `tax_amount`      DECIMAL(20, 2) COMMENT 'Tiền thuế',

    `net_price_vat`   DECIMAL(15, 6) COMMENT 'Đơn giá sau CK + thuế (= total_amount / quantity)',
    `total_amount`    DECIMAL(20, 2) COMMENT 'Tổng thanh toán dòng (net_amount + tax_amount)',

    `adjustment_type` INT DEFAULT 0 COMMENT 'Dành cho HĐ Điều chỉnh: 1: Thông tin, 2: Tăng, 3: Giảm (Mục IV.28 Excel)',
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
    `response_raw`  LONGTEXT COMMENT 'Lưu phản hồi thô JSON từ NCC',
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
    `id`             BIGINT      NOT NULL AUTO_INCREMENT,
    `invoice_id`     BIGINT      NOT NULL COMMENT 'FK → einv_invoices.id',
    `cqt_message_id` VARCHAR(100) COMMENT 'ID thông điệp truyền nhận với Cơ quan Thuế (Theo quy chuẩn truyền nhận PDF)',
    `sync_type`      VARCHAR(50) NOT NULL COMMENT 'SUBMIT | SIGN | GET_STATUS | GET_INVOICE',
    `status`         VARCHAR(20) NOT NULL DEFAULT 'PENDING' COMMENT 'PENDING | PROCESSING | SUCCESS | FAILED',
    `attempt_count`  INT         NOT NULL DEFAULT 0 COMMENT 'Số lần đã retry',
    `max_attempts`   INT         NOT NULL DEFAULT 3 COMMENT 'Giới hạn retry',
    `last_error`     TEXT COMMENT 'Thông báo lỗi lần cuối',
    `error_code`     VARCHAR(50) COMMENT 'Mã lỗi từ NCC (Dùng để mapping trạng thái ở bảng mapping_status)',
    `next_retry_at`  TIMESTAMP COMMENT 'Thời điểm retry tiếp theo',
    `created_at`     TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`     TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
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


INSERT INTO `einv_invoice_status` (`id`, `name`, `description`)
VALUES (0, 'Nháp', 'Hóa đơn mới khởi tạo hoặc hóa đơn nháp'),
       (1, 'Chờ ký', 'Hóa đơn đã gửi NCC thành công, đang chờ ký số'),
       (2, 'Đã phát hành', 'Hóa đơn đã ký số thành công và có hiệu lực pháp lý'),
       (3, 'Hủy', 'Hóa đơn đã bị hủy bỏ nghiệp vụ'),
       (4, 'Chờ gửi CQT', 'Hóa đơn đang chờ HUB gửi bản tin lên Cơ quan Thuế'),
       (5, 'CQT không chấp nhận', 'Hóa đơn bị Cơ quan Thuế từ chối cấp mã hoặc báo lỗi');

INSERT INTO `einv_payment_method` (`id`, `name`, `note`)
VALUES (1, 'TM', 'Tiền mặt'),
       (2, 'CK', 'Chuyển khoản'),
       (3, 'TM/CK', 'Tiền mặt/Chuyển khoản'),
       (4, 'Xuất hàng cho chi nhánh', 'Điều chuyển nội bộ chi nhánh'),
       (5, 'Hàng biếu tặng', 'Xuất hàng biếu tặng, quảng cáo'),
       (6, 'Cấn trừ công nợ', 'Thanh toán bằng hình thức cấn trừ'),
       (7, 'Trả hàng', 'Xuất hóa đơn cho hàng trả lại');

INSERT INTO `einv_tax_status` (`id`, `name`)
VALUES (0, 'Chưa gửi CQT'),
       (1, 'Đã gửi CQT'),
       (2, 'CQT chấp nhận'),
       (3, 'CQT không chấp nhận');

INSERT INTO `einv_invoice_type` (`id`, `invoice_type_name`, `note`)
VALUES (11, 'Chứng từ điện tử khấu trừ thuế TNCN', 'Theo Nghị định 70'),
       (12, 'Chứng từ điện tử khấu trừ thuế (TMĐT, nền tảng số)', 'Theo Nghị định 70'),
       (13, 'Biên lai phí, lệ phí không in sẵn mệnh giá', 'Theo Nghị định 70'),
       (14, 'Biên lai phí, lệ phí in sẵn mệnh giá', 'Theo Nghị định 70'),
       (15, 'Biên lai thu thuế, phí, lệ phí (CQT sử dụng - CTT50)', 'Mã CTT50'),
       (16, 'Biên lai thu thuế, phí, lệ phí đặt in, tự in', 'Thông tư 303')
ON DUPLICATE KEY UPDATE `invoice_type_name` = VALUES(`invoice_type_name`),
                        `note`              = VALUES(`note`);


SET FOREIGN_KEY_CHECKS = 1;
