-- EInvoiceHub
-- Database: MariaDB 11+ 

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;


-- NHÓM 1: DANH MỤC HỆ THỐNG (System Catalogs)
-- 1.1. Danh mục Loại thuế (Theo bản công ty: category_tax_type)
CREATE TABLE `category_tax_type`
(
    `id`           VARCHAR(36) COLLATE latin1_general_ci NOT NULL COMMENT 'UUID - Mã loại thuế',
    `tax_name`     VARCHAR(100)                          NULL COMMENT 'Tên loại thuế (Tiếng Việt)',
    `tax_name_en`  VARCHAR(100)                          NULL COMMENT 'Tên loại thuế (Tiếng Anh)',
    `description`  VARCHAR(100)                          NULL COMMENT 'Mô tả',
    `vat`          DECIMAL(15, 2)                        NULL COMMENT 'Tỷ lệ thuế (%)',

    `created_by`   VARCHAR(36) COLLATE latin1_general_ci NULL,
    `updated_by`   VARCHAR(36) COLLATE latin1_general_ci NULL,
    `created_date` DATETIME                              NULL,
    `updated_date` DATETIME                              NULL,

    PRIMARY KEY (`id`),
    CONSTRAINT `chk_tax_type_vat_valid`
        CHECK (`vat` IS NULL OR `vat` IN (0, 5, 8, 10, 3.5, 7)),
    CONSTRAINT `chk_tax_type_vat_positive`
        CHECK (`vat` IS NULL OR `vat` >= 0)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
    COMMENT = 'Danh mục Loại thuế ( Theo bản công ty)';

-- 1.2. Nhà cung cấp HĐĐT (BKAV, VNPT, MISA, VIETTEL, MOBI ...)
CREATE TABLE `einv_provider`
(
    `id`              VARCHAR(36) COLLATE latin1_general_ci NOT NULL COMMENT 'UUID của NCC',
    `provider_code`   VARCHAR(36)                           NOT NULL COMMENT 'Mã ngắn: BKAV, VNPT, MOBI, MISA, VIETTEL',
    `provider_name`   VARCHAR(200)                          NULL COMMENT 'Tên đầy đủ nhà cung cấp',
    `integration_url` VARCHAR(200)                          NULL COMMENT 'Endpoint tích hợp chính',
    `lookup_url`      VARCHAR(200)                          NULL COMMENT 'Base URL tra cứu hóa đơn',
    `inactive`        TINYINT(1) DEFAULT 0 COMMENT '0: Hoạt động, 1: Tạm ngưng',

    `created_by`      VARCHAR(36) COLLATE latin1_general_ci NULL,
    `updated_by`      VARCHAR(36) COLLATE latin1_general_ci NULL,
    `created_date`    DATETIME                              NULL,
    `updated_date`    DATETIME                              NULL,

    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_provider_code` (`provider_code`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
    COMMENT = 'Danh mục Nhà cung cấp HĐĐT';

-- 1.3. Loại hình hóa đơn
CREATE TABLE `einv_invoice_type`
(
    `id`           INT                                   NOT NULL COMMENT 'ID loại hóa đơn',
    `name`         VARCHAR(255)                          NOT NULL COMMENT 'Tên loại hóa đơn',
    `sort_order`   INT DEFAULT 0,
    `note`         VARCHAR(255)                          NULL COMMENT 'Ghi chú nghiệp vụ',

    `created_by`   VARCHAR(36) COLLATE latin1_general_ci NULL,
    `updated_by`   VARCHAR(36) COLLATE latin1_general_ci NULL,
    `created_date` DATETIME                              NULL,
    `updated_date` DATETIME                              NULL,

    PRIMARY KEY (`id`),
    CONSTRAINT `chk_inv_type_sort_order_positive`
        CHECK (`sort_order` >= 0),
    UNIQUE KEY `uq_invoice_type_name` (`name`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
    COMMENT = 'Danh mục Loại hình hóa đơn điện tử';

-- 1.4. Trạng thái hóa đơn
CREATE TABLE `einv_invoice_status`
(
    `id`           INT                                   NOT NULL COMMENT 'Mã trạng thái hệ thống HUB',
    `name`         VARCHAR(255)                          NOT NULL COMMENT 'Tên trạng thái',
    `description`  VARCHAR(255)                          NOT NULL COMMENT 'Mô tả chi tiết',
    `note`         VARCHAR(255)                          NULL COMMENT 'Ghi chú',

    `created_by`   VARCHAR(36) COLLATE latin1_general_ci NULL,
    `updated_by`   VARCHAR(36) COLLATE latin1_general_ci NULL,
    `created_date` DATETIME                              NULL,
    `updated_date` DATETIME                              NULL,

    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_inv_status_name` (`name`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
    COMMENT = 'Danh mục Trạng thái hóa đơn (chuẩn hóa, độc lập NCC)';

-- 1.5. Trạng thái Cơ quan Thuế
CREATE TABLE `einv_tax_status`
(
    `id`           INT COMMENT 'Mã trạng thái CQT',
    `name`         VARCHAR(100)                          NOT NULL COMMENT 'Tên trạng thái',

    `created_by`   VARCHAR(36) COLLATE latin1_general_ci NULL,
    `updated_by`   VARCHAR(36) COLLATE latin1_general_ci NULL,
    `created_date` DATETIME                              NULL,
    `updated_date` DATETIME                              NULL,
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
    COMMENT = 'Danh mục trạng thái phản hồi từ CQT';

-- 1.6. Phương thức thanh toán
CREATE TABLE `einv_payment_method`
(
    `id`           INT                                   NOT NULL COMMENT 'Mã phương thức thanh toán HUB',
    `name`         VARCHAR(255)                          NOT NULL COMMENT 'Tên phương thức',
    `note`         VARCHAR(255)                          NULL COMMENT 'Ghi chú',

    `created_by`   VARCHAR(36) COLLATE latin1_general_ci NULL,
    `updated_by`   VARCHAR(36) COLLATE latin1_general_ci NULL,
    `created_date` DATETIME                              NULL,
    `updated_date` DATETIME                              NULL,

    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_payment_method_name` (`name`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
    COMMENT = 'Danh mục Phương thức thanh toán';

-- 1.7. Đơn vị tính
CREATE TABLE `einv_unit`
(
    `code`         VARCHAR(50)                           NOT NULL COMMENT 'Mã đơn vị tính (DVT01, DVT02...)',
    `name`         VARCHAR(100)                          NOT NULL,

    `created_by`   VARCHAR(36) COLLATE latin1_general_ci NULL,
    `updated_by`   VARCHAR(36) COLLATE latin1_general_ci NULL,
    `created_date` DATETIME                              NULL,
    `updated_date` DATETIME                              NULL,

    PRIMARY KEY (`code`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
    COMMENT = 'Danh mục Đơn vị tính chuẩn HUB';

-- 1.8. Loại hàng hóa
CREATE TABLE `einv_item_type`
(
    `id`           INT                                   NOT NULL COMMENT 'ID loại hàng hóa',
    `name`         VARCHAR(255)                          NOT NULL COMMENT 'Tên loại hàng hóa',
    `note`         VARCHAR(255)                          NULL COMMENT 'Ghi chú',

    `created_by`   VARCHAR(36) COLLATE latin1_general_ci NULL,
    `updated_by`   VARCHAR(36) COLLATE latin1_general_ci NULL,
    `created_date` DATETIME                              NULL,
    `updated_date` DATETIME                              NULL,

    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_item_type_name` (`name`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
    COMMENT = 'Danh mục Loại hàng hóa trên dòng HĐ';

-- 1.9. Danh mục Hình thức nhận hóa đơn
CREATE TABLE `einv_receive_type`
(
    `id`           INT COMMENT 'ID hình thức nhận',
    `name`         VARCHAR(255)                          NOT NULL COMMENT 'Tên hình thức nhận',

    `created_by`   VARCHAR(36) COLLATE latin1_general_ci NULL,
    `updated_by`   VARCHAR(36) COLLATE latin1_general_ci NULL,
    `created_date` DATETIME                              NULL,
    `updated_date` DATETIME                              NULL,

    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
    COMMENT = 'Danh mục Hình thức nhận hóa đơn';

-- 1.10. Danh mục Loại tham chiếu (ReferenceType)
CREATE TABLE `einv_reference_type`
(
    `id`           INT                                   NOT NULL COMMENT 'ID loại tham chiếu',
    `name`         VARCHAR(255)                          NOT NULL COMMENT 'Tên loại tham chiếu',
    `note`         VARCHAR(255)                          NULL COMMENT 'Ghi chú',

    `created_by`   VARCHAR(36) COLLATE latin1_general_ci NULL,
    `updated_by`   VARCHAR(36) COLLATE latin1_general_ci NULL,
    `created_date` DATETIME                              NULL,
    `updated_date` DATETIME                              NULL,

    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_reference_type_name` (`name`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
    COMMENT = 'Danh mục Loại tham chiếu HĐ (Gốc/ĐC/TT)';

-- 1.11. Loại nghiệp vụ Submit
CREATE TABLE `einv_submit_invoice_type`
(
    `id`           VARCHAR(3)                            NOT NULL COMMENT 'Mã SubmitInvoiceType (100, 120, 121...)',
    `name`         VARCHAR(255)                          NOT NULL COMMENT 'Tên nghiệp vụ',
    `description`  VARCHAR(255)                          NULL,

    `created_by`   VARCHAR(36) COLLATE latin1_general_ci NULL,
    `updated_by`   VARCHAR(36) COLLATE latin1_general_ci NULL,
    `created_date` DATETIME                              NULL,
    `updated_date` DATETIME                              NULL,

    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
    COMMENT = 'Danh mục Loại nghiệp vụ Submit HĐ';



-- NHÓM 2: QUẢN LÝ ĐƠN VỊ KINH DOANH


-- 2.1. Tenant / Merchant (Theo bản công ty: tenant_id là VARCHAR(36))
CREATE TABLE `merchants`
(
    `id`           BIGINT                                NOT NULL AUTO_INCREMENT COMMENT 'Surrogate PK',
    `tenant_id`    VARCHAR(36) COLLATE latin1_general_ci NOT NULL COMMENT 'Mã định danh Tenant (UUID)',
    `company_name` VARCHAR(255)                          NOT NULL COMMENT 'Tên doanh nghiệp',
    `tax_code`     VARCHAR(20)                           NOT NULL COMMENT 'Mã số thuế chính',
    `is_active`    TINYINT(1)                            NOT NULL DEFAULT 1 COMMENT '1: Hoạt động, 0: Không hoạt động',

    `created_by`   VARCHAR(36) COLLATE latin1_general_ci NULL,
    `updated_by`   VARCHAR(36) COLLATE latin1_general_ci NULL,
    `created_date` DATETIME                              NULL,
    `updated_date` DATETIME                              NULL,

    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_tenant_id` (`tenant_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
    COMMENT = 'Quản lý Merchant/Tenant (đơn vị sử dụng hệ thống)';

-- 2.2. Store / Chi nhánh
CREATE TABLE `einv_stores`
(
    `id`           VARCHAR(36) COLLATE latin1_general_ci NOT NULL COMMENT 'UUID của Store',
    `tenant_id`    VARCHAR(36) COLLATE latin1_general_ci NOT NULL COMMENT 'FK → merchants.tenant_id',
    `store_name`   VARCHAR(255)                          NOT NULL COMMENT 'Tên chi nhánh',
    `tax_code`     VARCHAR(20) COMMENT 'MST chi nhánh (nếu khác tenant)',
    `is_active`    TINYINT(1)                            NOT NULL DEFAULT 1 COMMENT '1: Hoạt động, 0: Không hoạt động',

    `created_by`   VARCHAR(36) COLLATE latin1_general_ci NULL,
    `updated_by`   VARCHAR(36) COLLATE latin1_general_ci NULL,
    `created_date` DATETIME                              NULL,
    `updated_date` DATETIME                              NULL,

    PRIMARY KEY (`id`),
    CONSTRAINT `fk_store_merchant`
        FOREIGN KEY (`tenant_id`) REFERENCES `merchants` (`tenant_id`)
            ON DELETE RESTRICT
            ON UPDATE CASCADE,
    INDEX `idx_store_tenant_fk` (`tenant_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
    COMMENT = 'Quản lý Chi nhánh (Store) thuộc Merchant';



-- NHÓM 3: CẤU HÌNH TÍCH HỢP (Integration Config)


-- 3.1. Liên kết Store ↔ Provider (Theo bản công ty)
CREATE TABLE `einv_store_provider`
(
    `id`                  VARCHAR(36) COLLATE latin1_general_ci NOT NULL COMMENT 'UUID',
    `tenant_id`           VARCHAR(36) COLLATE latin1_general_ci NULL COMMENT 'FK → merchants.tenant_id',
    `store_id`            VARCHAR(36) COLLATE latin1_general_ci NULL COMMENT 'FK → einv_stores.id',
    `provider_id`         VARCHAR(36) COLLATE latin1_general_ci NULL COMMENT 'FK → einv_provider.id',

    `partner_id`          VARCHAR(200) COMMENT 'PartnerGUID, vnpt_Account',
    `partner_token`       VARCHAR(200) COMMENT 'PartnerToken; vnpt_ACPass',
    `partner_usr`         VARCHAR(200) COMMENT 'MISA: app_id, vnpt_username',
    `partner_pwd`         VARCHAR(200) COMMENT 'vnpt_password',

    `status`              TINYINT(1) DEFAULT 0 COMMENT '0: Chưa tích hợp; 1: Tích hợp thành công; 8: đã hủy tích hợp',
    `integrated_date`     DATETIME COMMENT 'Ngày tích hợp thành công',
    `integration_url`     VARCHAR(200) COMMENT 'Override URL riêng cho VNPT',
    `tax_code`            VARCHAR(200) COMMENT 'Mã số thuế',

    -- Các trường bổ sung từ project (không có trong bản công ty)
    `sign_type`           INT        DEFAULT 0 COMMENT '0:Token, 1:HSM, 2:SmartCA',
    `is_two_step_signing` TINYINT(1) DEFAULT 0 COMMENT 'Ký 2 bước',
    `app_id`              VARCHAR(100) COMMENT 'Dành riêng cho MISA',
    `fkey_prefix`         VARCHAR(50) COMMENT 'Tiền tố Fkey cho VNPT',
    `username_service`    VARCHAR(100) COMMENT 'User API',
    `password_service`    VARCHAR(255) COMMENT 'Pass API',

    `created_by`          VARCHAR(36) COLLATE latin1_general_ci NULL,
    `updated_by`          VARCHAR(36) COLLATE latin1_general_ci NULL,
    `created_date`        DATETIME   DEFAULT CURRENT_TIMESTAMP,
    `updated_date`        DATETIME   DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (`id`),
    CONSTRAINT `fk_sp_merchant`
        FOREIGN KEY (`tenant_id`) REFERENCES `merchants` (`tenant_id`)
            ON DELETE RESTRICT
            ON UPDATE CASCADE,
    CONSTRAINT `fk_sp_store`
        FOREIGN KEY (`store_id`) REFERENCES `einv_stores` (`id`)
            ON DELETE RESTRICT
            ON UPDATE CASCADE,
    CONSTRAINT `fk_sp_provider`
        FOREIGN KEY (`provider_id`) REFERENCES `einv_provider` (`id`)
            ON DELETE RESTRICT
            ON UPDATE CASCADE,
    CONSTRAINT `chk_sp_sign_type_valid`
        CHECK (`sign_type` IS NULL OR `sign_type` IN (0, 1, 2)),
    CONSTRAINT `chk_sp_status_valid`
        CHECK (`status` IS NULL OR `status` IN (0, 1, 8)),
    INDEX `idx_sp_tenant` (`tenant_id`),
    INDEX `idx_sp_store` (`store_id`),
    INDEX `idx_sp_provider` (`provider_id`),
    INDEX `idx_sp_status` (`status`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
    COMMENT = 'Cấu hình tích hợp Store ↔ NCC HĐĐT';

-- 3.2. Lịch sử thay đổi Store Provider (BẢNG MỚI - Theo bản công ty)
CREATE TABLE `einv_store_provider_history`
(
    `id`           VARCHAR(36) COLLATE latin1_general_ci NOT NULL COMMENT 'UUID',
    `tenant_id`    VARCHAR(36) COLLATE latin1_general_ci NULL COMMENT 'FK → merchants.tenant_id',
    `store_id`     VARCHAR(36) COLLATE latin1_general_ci NULL COMMENT 'FK → einv_stores.id',
    `provider_id`  VARCHAR(36) COLLATE latin1_general_ci NULL COMMENT 'FK → einv_provider.id',
    `action_type`  VARCHAR(50)                           NULL COMMENT 'Loại hành động: CREATE, UPDATE, DELETE',
    `status`       TINYINT                               NULL COMMENT 'Trạng thái sau thay đổi',
    `notes`        VARCHAR(100)                          NULL COMMENT 'Ghi chú',

    `created_by`   VARCHAR(36) COLLATE latin1_general_ci NULL,
    `updated_by`   VARCHAR(36) COLLATE latin1_general_ci NULL,
    `created_date` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_date` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (`id`),
    CONSTRAINT `fk_sph_merchant`
        FOREIGN KEY (`tenant_id`) REFERENCES `merchants` (`tenant_id`)
            ON DELETE RESTRICT
            ON UPDATE CASCADE,
    CONSTRAINT `fk_sph_store`
        FOREIGN KEY (`store_id`) REFERENCES `einv_stores` (`id`)
            ON DELETE RESTRICT
            ON UPDATE CASCADE,
    CONSTRAINT `fk_sph_provider`
        FOREIGN KEY (`provider_id`) REFERENCES `einv_provider` (`id`)
            ON DELETE RESTRICT
            ON UPDATE CASCADE,
    INDEX `idx_sph_store_provider` (`store_id`, `provider_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
    COMMENT = 'Lịch sử thay đổi cấu hình Store Provider';

-- 3.3. Dải ký hiệu / Mẫu số hóa đơn theo Store
CREATE TABLE `einv_store_serial`
(
    `id`                 VARCHAR(36) COLLATE latin1_general_ci NOT NULL COMMENT 'UUID',
    `tenant_id`          VARCHAR(36) COLLATE latin1_general_ci NULL COMMENT 'FK → merchants.tenant_id',
    `store_id`           VARCHAR(36) COLLATE latin1_general_ci NULL COMMENT 'FK → einv_stores.id ( Theo bản công ty)',
    `provider_id`        VARCHAR(36) COLLATE latin1_general_ci NULL COMMENT 'FK → einv_provider.id ( Theo bản công ty)',
    `invoice_type_id`    INT                                   NULL COMMENT 'FK → einv_invoice_type.id',

    `provider_serial_id` VARCHAR(50) COMMENT 'ID dải ký hiệu bên NCC cấp',
    `invoice_form`       VARCHAR(20) COMMENT 'Mẫu số hóa đơn (VD: 1/001)',
    `invoice_serial`     VARCHAR(20) COMMENT 'Ký hiệu hóa đơn (VD: C25TAA)',

    `start_date`         DATETIME COMMENT 'Ngày bắt đầu hiệu lực',
    `status`             INT DEFAULT 1 COMMENT '0: Chờ duyệt; 1: Đã duyệt; 8: Ngưng sử dụng',

    `created_by`         VARCHAR(36) COLLATE latin1_general_ci NULL,
    `updated_by`         VARCHAR(36) COLLATE latin1_general_ci NULL,
    `created_date`       DATETIME                              NULL,
    `updated_date`       DATETIME                              NULL,

    PRIMARY KEY (`id`),
    CONSTRAINT `fk_ss_merchant`
        FOREIGN KEY (`tenant_id`) REFERENCES `merchants` (`tenant_id`)
            ON DELETE RESTRICT
            ON UPDATE CASCADE,
    CONSTRAINT `fk_ss_store`
        FOREIGN KEY (`store_id`) REFERENCES `einv_stores` (`id`)
            ON DELETE RESTRICT
            ON UPDATE CASCADE,
    CONSTRAINT `fk_ss_provider`
        FOREIGN KEY (`provider_id`) REFERENCES `einv_provider` (`id`)
            ON DELETE RESTRICT
            ON UPDATE CASCADE,
    CONSTRAINT `fk_ss_invoice_type`
        FOREIGN KEY (`invoice_type_id`) REFERENCES `einv_invoice_type` (`id`)
            ON DELETE RESTRICT
            ON UPDATE CASCADE,
    CONSTRAINT `chk_ss_status_valid`
        CHECK (`status` IS NULL OR `status` IN (0, 1, 8)),
    INDEX `idx_ss_tenant` (`tenant_id`),
    INDEX `idx_ss_store` (`store_id`),
    INDEX `idx_ss_status` (`status`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
    COMMENT = 'Quản lý Dải ký hiệu / Mẫu số hóa đơn theo Store';



-- NHÓM 4: HỆ THỐNG MAPPING DỮ LIỆU
-- 4.1. Mapping Trạng thái hóa đơn
CREATE TABLE `einv_mapping_invoice_status`
(
    `id`                         VARCHAR(36) COLLATE latin1_general_ci NOT NULL COMMENT 'UUID',
    `provider_id`                VARCHAR(36) COLLATE latin1_general_ci NULL COMMENT 'FK → einv_provider.id',
    `invoice_status_id`          INT                                   NULL COMMENT 'FK → einv_invoice_status.id',
    `provider_invoice_status_id` VARCHAR(36)                           NULL COMMENT 'ID trạng thái bên NCC',
    `inactive`                   TINYINT(1) DEFAULT 0 COMMENT '0: Mặc định; 1: Disable',
    `note`                       VARCHAR(200)                          NULL COMMENT 'Ghi chú',

    `created_by`                 VARCHAR(36) COLLATE latin1_general_ci NULL,
    `updated_by`                 VARCHAR(36) COLLATE latin1_general_ci NULL,
    `created_date`               DATETIME                              NULL,
    `updated_date`               DATETIME                              NULL,

    PRIMARY KEY (`id`),
    CONSTRAINT `fk_mis_provider`
        FOREIGN KEY (`provider_id`) REFERENCES `einv_provider` (`id`)
            ON DELETE RESTRICT
            ON UPDATE CASCADE,
    CONSTRAINT `fk_mis_invoice_status`
        FOREIGN KEY (`invoice_status_id`) REFERENCES `einv_invoice_status` (`id`)
            ON DELETE RESTRICT
            ON UPDATE CASCADE,
    INDEX `idx_mis_provider` (`provider_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
    COMMENT = 'Mapping trạng thái hóa đơn HUB ↔ NCC';

-- 4.2. Mapping Loại hóa đơn
CREATE TABLE `einv_mapping_invoice_type`
(
    `id`                       VARCHAR(36) COLLATE latin1_general_ci NOT NULL COMMENT 'UUID',
    `provider_id`              VARCHAR(36) COLLATE latin1_general_ci NULL COMMENT 'FK → einv_provider.id',
    `invoice_type_id`          INT                                   NULL COMMENT 'FK → einv_invoice_type.id',
    `provider_invoice_type_id` VARCHAR(36)                           NULL COMMENT 'ID loại hóa đơn bên NCC',
    `inactive`                 TINYINT(1) DEFAULT 0 COMMENT '0: Mặc định; 1: Disable',
    `note`                     VARCHAR(200)                          NULL COMMENT 'Ghi chú',

    `created_by`               VARCHAR(36) COLLATE latin1_general_ci NULL,
    `updated_by`               VARCHAR(36) COLLATE latin1_general_ci NULL,
    `created_date`             DATETIME                              NULL,
    `updated_date`             DATETIME                              NULL,

    PRIMARY KEY (`id`),
    CONSTRAINT `fk_mit_provider`
        FOREIGN KEY (`provider_id`) REFERENCES `einv_provider` (`id`)
            ON DELETE RESTRICT
            ON UPDATE CASCADE,
    CONSTRAINT `fk_mit_invoice_type`
        FOREIGN KEY (`invoice_type_id`) REFERENCES `einv_invoice_type` (`id`)
            ON DELETE RESTRICT
            ON UPDATE CASCADE,
    INDEX `idx_mit_provider` (`provider_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
    COMMENT = 'Mapping loại hóa đơn HUB ↔ NCC';

-- 4.3. Mapping Phương thức thanh toán
CREATE TABLE `einv_mapping_payment_method`
(
    `id`                         VARCHAR(36) COLLATE latin1_general_ci NOT NULL COMMENT 'UUID',
    `provider_id`                VARCHAR(36) COLLATE latin1_general_ci NULL COMMENT 'FK → einv_provider.id',
    `payment_method_id`          INT                                   NULL COMMENT 'FK → einv_payment_method.id',
    `provider_payment_method_id` VARCHAR(36)                           NULL COMMENT 'ID phương thức thanh toán bên NCC',
    `inactive`                   TINYINT(1) DEFAULT 0 COMMENT '0: Mặc định; 1: Disable',
    `note`                       VARCHAR(200)                          NULL COMMENT 'Ghi chú',

    `created_by`                 VARCHAR(100),
    `created_date`               TIMESTAMP  DEFAULT CURRENT_TIMESTAMP,
    `updated_by`                 VARCHAR(100),
    `updated_date`               TIMESTAMP  DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (`id`),
    CONSTRAINT `fk_mpm_provider`
        FOREIGN KEY (`provider_id`) REFERENCES `einv_provider` (`id`)
            ON DELETE RESTRICT
            ON UPDATE CASCADE,
    CONSTRAINT `fk_mpm_payment_method`
        FOREIGN KEY (`payment_method_id`) REFERENCES `einv_payment_method` (`id`)
            ON DELETE RESTRICT
            ON UPDATE CASCADE,
    INDEX `idx_mpm_provider` (`provider_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
    COMMENT = 'Mapping phương thức thanh toán HUB ↔ NCC';

-- 4.4. Mapping Loại hàng hóa
CREATE TABLE `einv_mapping_item_type`
(
    `id`                    VARCHAR(36) COLLATE latin1_general_ci NOT NULL COMMENT 'UUID',
    `provider_id`           VARCHAR(36) COLLATE latin1_general_ci NULL COMMENT 'FK → einv_provider.id',
    `item_type_id`          INT                                   NULL COMMENT 'FK → einv_item_type.id',
    `provider_item_type_id` VARCHAR(36)                           NULL COMMENT 'ID loại hàng hóa bên NCC',
    `inactive`              TINYINT(1) DEFAULT 0 COMMENT '0: Mặc định; 1: Disable',
    `note`                  VARCHAR(200)                          NULL COMMENT 'Ghi chú',

    `created_by`            VARCHAR(100),
    `created_date`          TIMESTAMP  DEFAULT CURRENT_TIMESTAMP,
    `updated_by`            VARCHAR(100),
    `updated_date`          TIMESTAMP  DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (`id`),
    CONSTRAINT `fk_mit_item_provider`
        FOREIGN KEY (`provider_id`) REFERENCES `einv_provider` (`id`)
            ON DELETE RESTRICT
            ON UPDATE CASCADE,
    CONSTRAINT `fk_mit_item_type`
        FOREIGN KEY (`item_type_id`) REFERENCES `einv_item_type` (`id`)
            ON DELETE RESTRICT
            ON UPDATE CASCADE,
    INDEX `idx_mit_item_provider` (`provider_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
    COMMENT = 'Mapping loại hàng hóa HUB ↔ NCC';

-- 4.5. Mapping Loại thuế (THEO BẢN CÔNG TY: category_tax_type)
CREATE TABLE `einv_mapping_tax_type`
(
    `id`                   VARCHAR(36) COLLATE latin1_general_ci NOT NULL COMMENT 'UUID',
    `provider_id`          VARCHAR(36) COLLATE latin1_general_ci NULL COMMENT 'FK → einv_provider.id',
    `tax_type_id`          VARCHAR(36) COLLATE latin1_general_ci NULL COMMENT 'FK → category_tax_type.id',
    `provider_tax_type_id` VARCHAR(36)                           NULL COMMENT 'ID loại thuế bên NCC',
    `provider_tax_rate`    VARCHAR(36)                           NULL COMMENT 'Tỷ lệ thuế bên NCC',
    `inactive`             TINYINT(1) DEFAULT 0 COMMENT '0: Mặc định; 1: Disable',
    `note`                 VARCHAR(200)                          NULL COMMENT 'Ghi chú',

    `created_by`           VARCHAR(100),
    `created_date`         TIMESTAMP  DEFAULT CURRENT_TIMESTAMP,
    `updated_by`           VARCHAR(100),
    `updated_date`         TIMESTAMP  DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (`id`),
    CONSTRAINT `fk_mtt_provider`
        FOREIGN KEY (`provider_id`) REFERENCES `einv_provider` (`id`)
            ON DELETE RESTRICT
            ON UPDATE CASCADE,
    CONSTRAINT `fk_mtt_tax_type`
        FOREIGN KEY (`tax_type_id`) REFERENCES `category_tax_type` (`id`)
            ON DELETE RESTRICT
            ON UPDATE CASCADE,
    INDEX `idx_mtt_provider` (`provider_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
    COMMENT = 'Mapping loại thuế HUB ↔ NCC';

-- 4.6. Mapping Loại tham chiếu (BẢNG MỚI - Theo bản công ty)
CREATE TABLE `einv_mapping_reference_type`
(
    `id`                         VARCHAR(36) COLLATE latin1_general_ci NOT NULL COMMENT 'UUID',
    `provider_id`                VARCHAR(36) COLLATE latin1_general_ci NULL COMMENT 'FK → einv_provider.id',
    `reference_type_id`          INT                                   NULL COMMENT 'FK → einv_reference_type.id',
    `provider_reference_type_id` VARCHAR(36)                           NULL COMMENT 'ID loại tham chiếu bên NCC',
    `inactive`                   TINYINT(1) DEFAULT 0 COMMENT '0: Mặc định; 1: Disable',
    `note`                       VARCHAR(200)                          NULL COMMENT 'Ghi chú',

    `created_by`                 VARCHAR(100),
    `created_date`               TIMESTAMP  DEFAULT CURRENT_TIMESTAMP,
    `updated_by`                 VARCHAR(100),
    `updated_date`               TIMESTAMP  DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (`id`),
    CONSTRAINT `fk_mrt_provider`
        FOREIGN KEY (`provider_id`) REFERENCES `einv_provider` (`id`)
            ON DELETE RESTRICT
            ON UPDATE CASCADE,
    CONSTRAINT `fk_mrt_reference_type`
        FOREIGN KEY (`reference_type_id`) REFERENCES `einv_reference_type` (`id`)
            ON DELETE RESTRICT
            ON UPDATE CASCADE,
    INDEX `idx_mrt_provider` (`provider_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
    COMMENT = 'Mapping loại tham chiếu HĐ điều chỉnh/thay thế HUB ↔ NCC';

-- 4.7. Mapping Đơn vị tính (BỔ SUNG)
CREATE TABLE `einv_mapping_unit`
(
    `id`                 VARCHAR(36) COLLATE latin1_general_ci NOT NULL COMMENT 'UUID',
    `provider_id`        VARCHAR(36) COLLATE latin1_general_ci NULL COMMENT 'FK → einv_provider.id',
    `unit_code`          VARCHAR(50)                           NULL COMMENT 'FK → einv_unit.code',
    `provider_unit_code` VARCHAR(100)                          NULL COMMENT 'Mã đơn vị tính bên NCC',
    `inactive`           TINYINT(1) DEFAULT 0 COMMENT '0: Mặc định; 1: Disable',
    `note`               VARCHAR(200)                          NULL COMMENT 'Ghi chú',

    `created_by`         VARCHAR(100),
    `created_date`       TIMESTAMP  DEFAULT CURRENT_TIMESTAMP,
    `updated_by`         VARCHAR(100),
    `updated_date`       TIMESTAMP  DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (`id`),
    CONSTRAINT `fk_mu_provider`
        FOREIGN KEY (`provider_id`) REFERENCES `einv_provider` (`id`)
            ON DELETE RESTRICT
            ON UPDATE CASCADE,
    CONSTRAINT `fk_mu_unit`
        FOREIGN KEY (`unit_code`) REFERENCES `einv_unit` (`code`)
            ON DELETE RESTRICT
            ON UPDATE CASCADE,
    INDEX `idx_mu_provider` (`provider_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
    COMMENT = 'Mapping đơn vị tính HUB ↔ NCC';

-- 4.8. Mapping Lệnh Nghiệp vụ (GIỮ LẠI từ project vì không có trong bản công ty)
CREATE TABLE `einv_mapping_action`
(
    `id`           BIGINT                                NOT NULL AUTO_INCREMENT COMMENT 'ID mapping',
    `provider_id`  VARCHAR(36) COLLATE latin1_general_ci NOT NULL COMMENT 'FK → einv_provider.id',
    `hub_action`   VARCHAR(50)                           NOT NULL COMMENT 'SUBMIT, SIGN, CANCEL, REPLACE, ADJUST',
    `provider_cmd` VARCHAR(100)                          NOT NULL COMMENT 'BKAV: 100 | VNPT: ImportAndPublishInv',
    `description`  VARCHAR(255) COMMENT 'Mô tả',

    `created_by`   VARCHAR(36) COLLATE latin1_general_ci NULL,
    `updated_by`   VARCHAR(36) COLLATE latin1_general_ci NULL,
    `created_date` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_date` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (`id`),
    CONSTRAINT `fk_ma_provider`
        FOREIGN KEY (`provider_id`) REFERENCES `einv_provider` (`id`)
            ON DELETE RESTRICT
            ON UPDATE CASCADE,
    INDEX `idx_ma_provider` (`provider_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
    COMMENT = 'Mapping lệnh nghiệp vụ HUB ↔ NCC';



-- NHÓM 5: NGHIỆP VỤ HÓA ĐƠN


-- 5.1. Hóa đơn (THEO BẢN CÔNG TY: dùng VARCHAR(36) UUID, tenant_id VARCHAR(36))
CREATE TABLE `einv_invoices`
(
    `id`                   VARCHAR(36) COLLATE latin1_general_ci NOT NULL COMMENT 'UUID Primary Key',
    `tenant_id`            VARCHAR(36) COLLATE latin1_general_ci NULL COMMENT 'FK → merchants.tenant_id',
    `store_id`             VARCHAR(36) COLLATE latin1_general_ci NULL COMMENT 'FK → einv_stores.id',
    `provider_id`          VARCHAR(36) COLLATE latin1_general_ci NULL COMMENT 'FK → einv_provider.id',

    `partner_invoice_id`   VARCHAR(50) COLLATE latin1_general_ci NULL COMMENT 'ID hóa đơn gốc từ POS',
    `provider_invoice_id`  VARCHAR(50)                           NULL COMMENT 'ID hóa đơn do NCC cấp',

    `invoice_type_id`      INT                                   NULL COMMENT 'FK → einv_invoice_type.id',
    `reference_type_id`    INT                                   NULL COMMENT '0: Gốc, 2: Điều chỉnh, 3: Thay thế',
    `status_id`            INT                                   NULL COMMENT 'FK → einv_invoice_status.id',

    `invoice_form`         VARCHAR(50)                           NULL COMMENT 'Mẫu số (VD: 1/001)',
    `invoice_series`       VARCHAR(50)                           NULL COMMENT 'Ký hiệu (VD: C25TAA)',
    `invoice_no`           VARCHAR(50)                           NULL COMMENT 'Số hóa đơn',
    `invoice_date`         DATETIME                              NULL COMMENT 'Ngày hóa đơn',
    `signed_date`          DATETIME                              NULL COMMENT 'Ngày ký số',

    `payment_method_id`    INT                                   NULL COMMENT 'FK → einv_payment_method.id',
    `buyer_tax_code`       VARCHAR(50)                           NULL COMMENT 'Mã số thuế người mua',
    `buyer_company`        VARCHAR(300)                          NULL COMMENT 'Tên đơn vị người mua',
    `buyer_id_no`          VARCHAR(20) COLLATE latin1_general_ci NULL COMMENT 'CCCD/Hộ chiếu',
    `buyer_full_name`      VARCHAR(200)                          NULL COMMENT 'Tên người mua',
    `buyer_address`        VARCHAR(300)                          NULL COMMENT 'Địa chỉ người mua',
    `buyer_mobile`         VARCHAR(50)                           NULL COMMENT 'Số điện thoại người mua',
    `buyer_bank_account`   VARCHAR(50)                           NULL COMMENT 'Số tài khoản người mua',
    `buyer_bank_name`      VARCHAR(200)                          NULL COMMENT 'Tên ngân hàng người mua',
    `buyer_budget_code`    VARCHAR(20)                           NULL COMMENT 'Mã số đơn vị quan hệ ngân sách',

    `receive_type_id`      INT                                   NULL COMMENT 'FK → einv_receive_type.id',
    `receiver_email`       VARCHAR(300)                          NULL COMMENT 'Email nhận hóa đơn',

    `currency_code`        VARCHAR(20)                           NULL DEFAULT 'VND' COMMENT 'Mã tiền tệ',
    `exchange_rate`        DECIMAL(10, 2)                        NULL DEFAULT 1.0 COMMENT 'Tỷ giá',

    `tax_authority_code`   VARCHAR(50)                           NULL COMMENT 'Mã CQT cấp',
    `invoice_lookup_code`  VARCHAR(50)                           NULL COMMENT 'Mã tra cứu HĐ',

    -- Thông tin hóa đơn gốc (điều chỉnh/thay thế)
    `org_invoice_id`       VARCHAR(36) COLLATE latin1_general_ci NULL COMMENT 'ID HĐ gốc',
    `org_invoice_form`     VARCHAR(50)                           NULL COMMENT 'Mẫu số HĐ gốc',
    `org_invoice_series`   VARCHAR(50)                           NULL COMMENT 'Ký hiệu HĐ gốc',
    `org_invoice_no`       VARCHAR(50)                           NULL COMMENT 'Số HĐ gốc',
    `org_invoice_date`     DATETIME                              NULL COMMENT 'Ngày HĐ gốc',
    `org_invoice_reason`   VARCHAR(500)                          NULL COMMENT 'Lý do điều chỉnh/thay thế',

    -- Các trường tiền tệ
    `gross_amount`         DECIMAL(15, 2)                        NULL COMMENT 'Thành tiền hàng hóa',
    `discount_amount`      DECIMAL(15, 2)                        NULL COMMENT 'Số tiền chiết khấu',
    `net_amount`           DECIMAL(15, 2)                        NULL COMMENT 'Thành tiền trước thuế',
    `tax_amount`           DECIMAL(15, 2)                        NULL COMMENT 'Số tiền thuế',
    `total_amount`         DECIMAL(15, 2)                        NULL COMMENT 'Trị giá thanh toán',

    -- Các trường bổ sung từ project (không có trong bản công ty)
    `tax_status_id`        INT                                        DEFAULT 0 COMMENT 'FK → einv_tax_status.id',
    `cqt_response_code`    VARCHAR(10) COMMENT 'Mã phản hồi từ CQT',
    `provider_response_id` VARCHAR(100) COMMENT 'ID bản tin của NCC',
    `is_draft`             TINYINT(1)                                 DEFAULT 0 COMMENT 'Hóa đơn nháp',
    `is_mtt`               TINYINT(1)                                 DEFAULT 0 COMMENT 'Hóa đơn Máy tính tiền',
    `is_petrol`            TINYINT(1)                                 DEFAULT 0 COMMENT 'Hóa đơn Xăng dầu',
    `is_locked`            TINYINT(1)                                 DEFAULT 0 COMMENT 'Hóa đơn bị khóa',
    `is_deleted`           TINYINT(1)                                 DEFAULT 0 COMMENT 'Hóa đơn bị xóa',
    `sign_type`            INT COMMENT '0: Token, 1: HSM',
    `submit_invoice_type`  VARCHAR(3) COMMENT 'FK → einv_submit_invoice_type.id',
    `response_message`     VARCHAR(500) COMMENT 'Thông báo từ NCC',
    `error_code`           VARCHAR(50) COMMENT 'Mã lỗi từ NCC',
    `secret_code`          VARCHAR(50) COMMENT 'Mã bí mật (Viettel)',
    `buyer_plate_no`       VARCHAR(50) COMMENT 'Biển số xe (Xăng dầu)',
    `extra_metadata`       JSON COMMENT 'Trường động (MISA/VNPT)',
    `delivery_info`        JSON COMMENT 'Thông tin vận chuyển (PXK)',
    `total_amount_text`    VARCHAR(500) COMMENT 'Giá trị bằng chữ',
    `tax_summary_json`     JSON COMMENT 'Tổng hợp thuế suất cho XML CQT',
    `notes`                VARCHAR(300) COMMENT 'Ghi chú hóa đơn',

    `created_by`           VARCHAR(36) COLLATE latin1_general_ci NULL,
    `updated_by`           VARCHAR(36) COLLATE latin1_general_ci NULL,
    `created_date`         DATETIME                                   DEFAULT CURRENT_TIMESTAMP,
    `updated_date`         DATETIME                                   DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (`id`),
    CONSTRAINT `fk_inv_merchant`
        FOREIGN KEY (`tenant_id`) REFERENCES `merchants` (`tenant_id`)
            ON DELETE RESTRICT
            ON UPDATE CASCADE,
    CONSTRAINT `fk_inv_store`
        FOREIGN KEY (`store_id`) REFERENCES `einv_stores` (`id`)
            ON DELETE RESTRICT
            ON UPDATE CASCADE,
    CONSTRAINT `fk_inv_provider`
        FOREIGN KEY (`provider_id`) REFERENCES `einv_provider` (`id`)
            ON DELETE RESTRICT
            ON UPDATE CASCADE,
    CONSTRAINT `fk_inv_invoice_type`
        FOREIGN KEY (`invoice_type_id`) REFERENCES `einv_invoice_type` (`id`)
            ON DELETE RESTRICT
            ON UPDATE CASCADE,
    CONSTRAINT `fk_inv_reference_type`
        FOREIGN KEY (`reference_type_id`) REFERENCES `einv_reference_type` (`id`)
            ON DELETE RESTRICT
            ON UPDATE CASCADE,
    CONSTRAINT `fk_inv_status`
        FOREIGN KEY (`status_id`) REFERENCES `einv_invoice_status` (`id`)
            ON DELETE RESTRICT
            ON UPDATE CASCADE,
    CONSTRAINT `fk_inv_payment_method`
        FOREIGN KEY (`payment_method_id`) REFERENCES `einv_payment_method` (`id`)
            ON DELETE RESTRICT
            ON UPDATE CASCADE,
    CONSTRAINT `fk_inv_receive_type`
        FOREIGN KEY (`receive_type_id`) REFERENCES `einv_receive_type` (`id`)
            ON DELETE RESTRICT
            ON UPDATE CASCADE,
    CONSTRAINT `fk_inv_tax_status`
        FOREIGN KEY (`tax_status_id`) REFERENCES `einv_tax_status` (`id`)
            ON DELETE RESTRICT
            ON UPDATE CASCADE,
    CONSTRAINT `fk_inv_submit_type`
        FOREIGN KEY (`submit_invoice_type`) REFERENCES `einv_submit_invoice_type` (`id`)
            ON DELETE RESTRICT
            ON UPDATE CASCADE,
    CONSTRAINT `fk_inv_org_invoice`
        FOREIGN KEY (`org_invoice_id`) REFERENCES `einv_invoices` (`id`)
            ON DELETE RESTRICT
            ON UPDATE CASCADE,
    CONSTRAINT `chk_inv_total_amount_positive`
        CHECK (`total_amount` IS NULL OR `total_amount` >= 0),
    CONSTRAINT `chk_inv_tax_amount_positive`
        CHECK (`tax_amount` IS NULL OR `tax_amount` >= 0),
    CONSTRAINT `chk_inv_net_amount_positive`
        CHECK (`net_amount` IS NULL OR `net_amount` >= 0),
    CONSTRAINT `chk_inv_discount_amount_positive`
        CHECK (`discount_amount` IS NULL OR `discount_amount` >= 0),
    CONSTRAINT `chk_inv_gross_amount_positive`
        CHECK (`gross_amount` IS NULL OR `gross_amount` >= 0),
    CONSTRAINT `chk_inv_exchange_rate_positive`
        CHECK (`exchange_rate` IS NULL OR `exchange_rate` > 0),
    UNIQUE INDEX `uq_inv_tenant_partner` (`tenant_id`, `partner_invoice_id`),
    INDEX `idx_inv_lookup_code` (`invoice_lookup_code`),
    INDEX `idx_inv_buyer_tax` (`buyer_tax_code`),
    INDEX `idx_inv_date` (`invoice_date`),
    INDEX `idx_inv_provider_date` (`provider_id`, `invoice_date`),
    INDEX `idx_inv_tenant_status` (`tenant_id`, `status_id`),
    INDEX `idx_inv_store_status` (`store_id`, `status_id`),
    INDEX `idx_inv_provider_status` (`provider_id`, `status_id`),
    INDEX `idx_inv_tenant` (`tenant_id`),
    INDEX `idx_inv_status` (`status_id`),
    INDEX `idx_inv_invoice_type_fk` (`invoice_type_id`),
    INDEX `idx_inv_reference_type_fk` (`reference_type_id`),
    INDEX `idx_inv_payment_method_fk` (`payment_method_id`),
    INDEX `idx_inv_receive_type_fk` (`receive_type_id`),
    INDEX `idx_inv_tax_status_fk` (`tax_status_id`),
    INDEX `idx_inv_submit_type_fk` (`submit_invoice_type`),
    INDEX `idx_inv_org_invoice_fk` (`org_invoice_id`),
    INDEX `idx_inv_created_date` (`created_date`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
    COMMENT = 'Hóa đơn điện tử (Header) - HUB Central';

-- 5.2. Dòng hàng hóa trên hóa đơn (THEO BẢN CÔNG TY)
CREATE TABLE `einv_invoices_detail`
(
    `id`              VARCHAR(36) COLLATE latin1_general_ci NOT NULL COMMENT 'UUID',
    `tenant_id`       VARCHAR(36) COLLATE latin1_general_ci NULL COMMENT 'FK → merchants.tenant_id',
    `store_id`        VARCHAR(36) COLLATE latin1_general_ci NULL COMMENT 'FK → einv_stores.id',
    `doc_id`          VARCHAR(36) COLLATE latin1_general_ci NULL COMMENT 'FK → einv_invoices.id',
    `line_no`         INT                                   NULL COMMENT 'Số thứ tự dòng',
    `is_free`         TINYINT(1)                            NULL COMMENT '1: Hàng tặng, đơn giá = 0',

    `item_type_id`    INT                                   NULL COMMENT 'FK → einv_item_type.id',
    `item_id`         VARCHAR(36) COLLATE latin1_general_ci NULL COMMENT 'Mã hàng hóa',
    `item_name`       VARCHAR(500)                          NULL COMMENT 'Tên hàng hóa, dịch vụ',
    `unit`            VARCHAR(50)                           NULL COMMENT 'Đơn vị tính',

    `quantity`        DECIMAL(15, 2)                        NULL COMMENT 'Số lượng',
    `price`           DECIMAL(15, 2)                        NULL COMMENT 'Đơn giá',
    `gross_amount`    DECIMAL(15, 2)                        NULL COMMENT 'Thành tiền = quantity*price',
    `discount_rate`   DECIMAL(15, 2)                        NULL COMMENT 'Tỷ lệ chiết khấu (%)',
    `discount_amount` DECIMAL(15, 2)                        NULL COMMENT 'Số tiền chiết khấu',

    `net_price_vat`   DECIMAL(15, 2)                        NULL COMMENT 'Đơn giá có VAT = Thanh toán / Số lượng',
    `net_price`       DECIMAL(15, 2)                        NULL COMMENT 'Đơn giá sau CK',
    `net_amount`      DECIMAL(15, 2)                        NULL COMMENT 'Thành tiền sau CK',

    `tax_type_id`     VARCHAR(36) COLLATE latin1_general_ci NULL COMMENT 'FK → category_tax_type.id',
    `tax_rate`        DECIMAL(15, 2)                        NULL COMMENT 'Thuế suất (%)',
    `tax_amount`      DECIMAL(15, 2)                        NULL COMMENT 'Số tiền thuế',
    `total_amount`    DECIMAL(15, 2)                        NULL COMMENT 'Tổng thanh toán dòng',

    `notes`           VARCHAR(500)                          NULL COMMENT 'Ghi chú',

    -- Trường bổ sung từ project (không có trong bản công ty)
    `adjustment_type` INT      DEFAULT 0 COMMENT 'Dành cho HĐ Điều chỉnh: 1: Thông tin, 2: Tăng, 3: Giảm',

    `created_by`      VARCHAR(36) COLLATE latin1_general_ci NULL,
    `updated_by`      VARCHAR(36) COLLATE latin1_general_ci NULL,
    `created_date`    DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_date`    DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (`id`),
    CONSTRAINT `fk_inv_det_invoice`
        FOREIGN KEY (`doc_id`) REFERENCES `einv_invoices` (`id`)
            ON DELETE CASCADE
            ON UPDATE CASCADE,
    CONSTRAINT `fk_inv_det_merchant`
        FOREIGN KEY (`tenant_id`) REFERENCES `merchants` (`tenant_id`)
            ON DELETE RESTRICT
            ON UPDATE CASCADE,
    CONSTRAINT `fk_inv_det_store`
        FOREIGN KEY (`store_id`) REFERENCES `einv_stores` (`id`)
            ON DELETE RESTRICT
            ON UPDATE CASCADE,
    CONSTRAINT `fk_inv_det_item_type`
        FOREIGN KEY (`item_type_id`) REFERENCES `einv_item_type` (`id`)
            ON DELETE RESTRICT
            ON UPDATE CASCADE,
    CONSTRAINT `fk_inv_det_tax_type`
        FOREIGN KEY (`tax_type_id`) REFERENCES `category_tax_type` (`id`)
            ON DELETE RESTRICT
            ON UPDATE CASCADE,
    CONSTRAINT `chk_inv_det_adjustment_type_valid`
        CHECK (`adjustment_type` IS NULL OR `adjustment_type` IN (0, 1, 2, 3)),
    CONSTRAINT `chk_inv_det_quantity_positive`
        CHECK (`quantity` IS NULL OR `quantity` > 0),
    CONSTRAINT `chk_inv_det_price_positive`
        CHECK (`price` IS NULL OR `price` >= 0),
    CONSTRAINT `chk_inv_det_gross_amount_positive`
        CHECK (`gross_amount` IS NULL OR `gross_amount` >= 0),
    CONSTRAINT `chk_inv_det_discount_amount_positive`
        CHECK (`discount_amount` IS NULL OR `discount_amount` >= 0),
    CONSTRAINT `chk_inv_det_discount_rate_valid`
        CHECK (`discount_rate` IS NULL OR (`discount_rate` >= 0 AND `discount_rate` <= 100)),
    CONSTRAINT `chk_inv_det_tax_rate_valid`
        CHECK (`tax_rate` IS NULL OR `tax_rate` IN (0, 5, 8, 10, 3.5, 7)),
    CONSTRAINT `chk_inv_det_tax_amount_positive`
        CHECK (`tax_amount` IS NULL OR `tax_amount` >= 0),
    CONSTRAINT `chk_inv_det_total_amount_positive`
        CHECK (`total_amount` IS NULL OR `total_amount` >= 0),
    CONSTRAINT `chk_inv_det_net_price_vat_positive`
        CHECK (`net_price_vat` IS NULL OR `net_price_vat` >= 0),
    CONSTRAINT `chk_inv_det_net_price_positive`
        CHECK (`net_price` IS NULL OR `net_price` >= 0),
    CONSTRAINT `chk_inv_det_line_no_positive`
        CHECK (`line_no` IS NULL OR `line_no` > 0),
    INDEX `idx_inv_det_doc_line` (`doc_id`, `line_no`),
    INDEX `idx_inv_det_tenant` (`tenant_id`),
    INDEX `biz_retail_detail_biz_retail_FK` (`doc_id`),
    INDEX `biz_retail_detail_tenant_id_IDX` (`tenant_id`),
    INDEX `idx_inv_det_item_type_fk` (`item_type_id`),
    INDEX `idx_inv_det_tax_type_fk` (`tax_type_id`)

) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
    COMMENT = 'Dòng hàng hóa trên Hóa đơn (Detail)';



-- NHÓM 6: LƯU TRỮ PAYLOAD & VẬN HÀNH
-- 6.1. Lưu trữ Payload gốc
CREATE TABLE `einv_invoice_payloads`
(
    `invoice_id`    VARCHAR(36) COLLATE latin1_general_ci NOT NULL COMMENT 'FK → einv_invoices.id',
    `request_json`  LONGTEXT COMMENT 'Bản tin JSON gửi lên NCC',
    `request_xml`   LONGTEXT COMMENT 'Bản tin XML gửi lên NCC (VNPT)',
    `response_json` LONGTEXT COMMENT 'Phản hồi thô từ NCC',
    `signed_xml`    LONGTEXT COMMENT 'XML đã ký số (nếu có)',
    `pdf_data`      LONGTEXT COMMENT 'Dữ liệu PDF base64 (nếu NCC trả về)',
    `response_raw`  LONGTEXT COMMENT 'Lưu phản hồi thô JSON từ NCC',

    `created_by`    VARCHAR(36) COLLATE latin1_general_ci NULL,
    `updated_by`    VARCHAR(36) COLLATE latin1_general_ci NULL,
    `created_date`  DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_date`  DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (`invoice_id`),
    CONSTRAINT `fk_payload_invoice` FOREIGN KEY (`invoice_id`) REFERENCES `einv_invoices` (`id`) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
    COMMENT = 'Lưu payload thô (request/response) để debug và audit';

-- 6.2. Hàng chờ đồng bộ
CREATE TABLE `einv_sync_queue`
(
    `id`             VARCHAR(36) COLLATE latin1_general_ci NOT NULL COMMENT 'UUID',
    `tenant_id`      VARCHAR(36) COLLATE latin1_general_ci NULL COMMENT 'FK → merchants.tenant_id',
    `provider_id`    VARCHAR(36) COLLATE latin1_general_ci NULL COMMENT 'FK → einv_provider.id',
    `invoice_id`     VARCHAR(36) COLLATE latin1_general_ci NULL COMMENT 'FK → einv_invoices.id',
    `cqt_message_id` VARCHAR(100) COMMENT 'ID thông điệp truyền nhận với Cơ quan Thuế',
    `sync_type`      VARCHAR(50)                           NOT NULL COMMENT 'SUBMIT | SIGN | GET_STATUS | GET_INVOICE',
    `status`         VARCHAR(20)                           NOT NULL DEFAULT 'PENDING' COMMENT 'PENDING | PROCESSING | SUCCESS | FAILED',
    `attempt_count`  INT                                   NOT NULL DEFAULT 0 COMMENT 'Số lần đã retry',
    `max_attempts`   INT                                   NOT NULL DEFAULT 3 COMMENT 'Giới hạn retry',
    `last_error`     TEXT COMMENT 'Thông báo lỗi lần cuối',
    `error_code`     VARCHAR(50) COMMENT 'Mã lỗi từ NCC',
    `next_retry_at`  DATETIME COMMENT 'Thời điểm retry tiếp theo',

    `created_by`     VARCHAR(36) COLLATE latin1_general_ci NULL,
    `updated_by`     VARCHAR(36) COLLATE latin1_general_ci NULL,
    `created_date`   DATETIME                                       DEFAULT CURRENT_TIMESTAMP,
    `updated_date`   DATETIME                                       DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (`id`),
    CONSTRAINT `fk_queue_invoice`
        FOREIGN KEY (`invoice_id`) REFERENCES `einv_invoices` (`id`)
            ON DELETE CASCADE
            ON UPDATE CASCADE,
    CONSTRAINT `fk_queue_merchant`
        FOREIGN KEY (`tenant_id`) REFERENCES `merchants` (`tenant_id`)
            ON DELETE RESTRICT
            ON UPDATE CASCADE,
    CONSTRAINT `fk_queue_provider`
        FOREIGN KEY (`provider_id`) REFERENCES `einv_provider` (`id`)
            ON DELETE RESTRICT
            ON UPDATE CASCADE,
    CONSTRAINT `chk_queue_attempt_count_positive`
        CHECK (`attempt_count` >= 0),
    CONSTRAINT `chk_queue_max_attempts_positive`
        CHECK (`max_attempts` > 0),
    CONSTRAINT `chk_queue_attempt_max_valid`
        CHECK (`attempt_count` <= `max_attempts`),
    CONSTRAINT `chk_queue_status_valid`
        CHECK (`status` IN ('PENDING', 'PROCESSING', 'SUCCESS', 'FAILED')),
    INDEX `idx_queue_tenant_status` (`tenant_id`, `status`),
    INDEX `idx_queue_provider_status` (`provider_id`, `status`),
    INDEX `idx_queue_status_retry` (`status`, `next_retry_at`),
    INDEX `idx_queue_invoice` (`invoice_id`),
    INDEX `idx_queue_tenant_fk` (`tenant_id`),
    INDEX `idx_queue_provider_fk` (`provider_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
    COMMENT = 'Hàng chờ xử lý bất đồng bộ';

-- 6.3. Nhật ký kiểm toán
CREATE TABLE `einv_audit_logs`
(
    `id`           BIGINT AUTO_INCREMENT                 NOT NULL COMMENT ' log',
    `action`       VARCHAR(100)                          NOT NULL COMMENT 'VD: SUBMIT_INVOICE, SIGN_INVOICE, GET_STATUS',
    `entity_name`  VARCHAR(100) COMMENT 'Tên bảng liên quan',
    `entity_id`    VARCHAR(100) COMMENT 'ID bản ghi bị tác động',
    `payload`      JSON COMMENT 'Dữ liệu tại thời điểm action',
    `result`       VARCHAR(20) COMMENT 'SUCCESS | FAILURE',
    `error_msg`    TEXT COMMENT 'Chi tiết lỗi nếu thất bại',

    `created_by`   VARCHAR(36) COLLATE latin1_general_ci NULL,
    `updated_by`   VARCHAR(36) COLLATE latin1_general_ci NULL,
    `created_date` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_date` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (`id`),
    INDEX `idx_audit_created_by` (`created_by`),
    INDEX `idx_audit_entity` (`entity_name`, `entity_id`),
    INDEX `idx_audit_action` (`action`),
    INDEX `idx_audit_created` (`created_date`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
    COMMENT = 'Nhật ký kiểm toán toàn bộ hành động trên hệ thống';

-- NHÓM 7: DỮ LIỆU MẪU (MASTER DATA)
-- 7.1. Danh sách Nhà cung cấp (Provider)
-- Lưu ý: Giữ integration_url và lookup_url từ file V2 vì tài liệu không có các trường này
INSERT INTO `einv_provider` (`id`, `provider_code`, `provider_name`, `integration_url`, `lookup_url`, `inactive`)
VALUES ('550e8400-e29b-41d4-a716-446655440001', 'BKAV', 'Công ty cổ phần Bkav', 'https://einvoicebkav.com.vn/api',
        'https://einvoicebkav.com.vn/invoice/api', 0),
       ('550e8400-e29b-41d4-a716-446655440002', 'VNPT', 'VNPT Invoice', 'https://einvoice.vnpt.com.vn/api',
        'https://einvoice.vnpt.com.vn/invoiceWS', 0),
       ('550e8400-e29b-41d4-a716-446655440003', 'MISA', 'MISA Invoice', 'https://invoice.misa.com.vn/api',
        'https://invoice.misa.com.vn', 0),
       ('550e8400-e29b-41d4-a716-446655440004', 'MOBI', 'Mobifone Invoice', 'https://m-invoice.mobifone.com.vn/api',
        'https://m-invoice.mobifone.com.vn', 0),
       ('550e8400-e29b-41d4-a716-446655440005', 'VIETTEL', 'Viettel Invoice',
        'https://einvoice.viettelgroup.com.vn/api', 'https://einvoice.viettelgroup.com.vn', 0)
ON DUPLICATE KEY UPDATE `provider_name` = VALUES(`provider_name`);

-- 7.2. Danh mục Loại thuế (THEO BẢN CÔNG TY: category_tax_type)
INSERT INTO `category_tax_type` (`id`, `tax_name`, `tax_name_en`, `description`, `vat`)
VALUES ('550e8400-e29b-41d4-a716-446655440010', 'Thuế suất 0%', 'VAT 0%',
        'Hàng hóa, dịch vụ không chịu thuế GTGT (áp dụng tỷ lệ 0%)', 0.00),
       ('550e8400-e29b-41d4-a716-446655440011', 'Thuế suất 5%', 'VAT 5%',
        'Hàng hóa, dịch vụ chịu thuế GTGT với tỷ lệ 5%', 5.00),
       ('550e8400-e29b-41d4-a716-446655440012', 'Thuế suất 8%', 'VAT 8%',
        'Hàng hóa, dịch vụ chịu thuế GTGT với tỷ lệ 8%', 8.00),
       ('550e8400-e29b-41d4-a716-446655440013', 'Thuế suất 10%', 'VAT 10%',
        'Hàng hóa, dịch vụ chịu thuế GTGT với tỷ lệ 10%', 10.00),
       ('550e8400-e29b-41d4-a716-446655440014', 'Không chịu thuế GTGT', 'Exempt',
        'Hàng hóa, dịch vụ không chịu thuế GTGT', NULL),
       ('550e8400-e29b-41d4-a716-446655440015', 'Không kê khai thuế', 'Not Declaration',
        'Hàng hóa, dịch vụ không kê khai thuế GTGT', NULL),
       ('550e8400-e29b-41d4-a716-446655440016', 'Thuế suất 5%x70%', 'VAT 5%x70%',
        'Hàng hóa, dịch vụ chịu thuế GTGT với tỷ lệ 3.5%', 3.50),
       ('550e8400-e29b-41d4-a716-446655440017', 'Thuế suất 10%x70%', 'VAT 10%x70%',
        'Hàng hóa, dịch vụ chịu thuế GTGT với tỷ lệ 7%', 7.00)
ON DUPLICATE KEY UPDATE `tax_name` = VALUES(`tax_name`),
                        `vat`      = VALUES(`vat`);

-- 7.3. Danh mục Loại hóa đơn
INSERT INTO `einv_invoice_type` (`id`, `name`, `note`)
VALUES (1, 'Hóa đơn Giá trị gia tăng', 'Hóa đơn GTGT (VAT)'),
       (2, 'Hóa đơn bán hàng', 'Hóa đơn thông thường'),
       (4, 'Hóa đơn bán hàng (dành cho Tổ chức, Cá nhân trong khu PTQ)', 'Hóa đơn đặc biệt'),
       (5, 'Phiếu xuất kho & vận chuyển nội bộ', 'PXK&VCNB'),
       (6, 'Phiếu xuất kho gửi bán hàng đại lý', 'PXKHGBDL'),
       (7, 'Biên lai thu phí không điền sẵn mệnh giá', 'Biên lai thu phí'),
       (8, 'Biên lai thu phí điền sẵn mệnh giá', 'Biên lai thu phí'),
       (9, 'Hóa đơn bán tài sản công', 'Hóa đơn đặc biệt'),
       (10, 'Tem, vé, thẻ điện tử là Hóa đơn GTGT', 'Tem, vé điện tử'),
       (11, 'Tem, vé, thẻ điện tử là Hóa đơn BH', 'Tem, vé điện tử')
ON DUPLICATE KEY UPDATE `name` = VALUES(`name`);

-- 7.4. Danh mục Trạng thái hóa đơn
INSERT INTO `einv_invoice_status` (`id`, `name`, `description`, `note`)
VALUES (1, 'Mới tạo', 'Hóa đơn mới tạo chưa có số hóa đơn', 'Trạng thái ban đầu'),
       (2, 'Đã phát hành', 'Hóa đơn đã ký phát hành', 'Phát hành thành công'),
       (3, 'Đã hủy', 'Hoá đơn bị huỷ từ trạng thái Đã phát hành', 'Vẫn báo cáo thuế'),
       (4, 'Đã xóa', 'Hóa đơn đã xóa khỏi hệ thống', 'Không dùng'),
       (5, 'Chờ thay thế', 'Hóa đơn thay thế chưa ký', 'Chờ ký số'),
       (6, 'Thay thế', 'Hóa đơn Thay thế đã ký', 'Hoàn tất thay thế'),
       (7, 'Chờ điều chỉnh', 'Hóa đơn điều chỉnh chưa ký', 'Chờ ký số điều chỉnh'),
       (8, 'Điều chỉnh', 'Hóa đơn điều chỉnh đã ký', 'Hoàn tất điều chỉnh'),
       (9, 'Bị thay thế', 'Hóa đơn bị thay thế', 'Đã bị thay thế'),
       (10, 'Bị điều chỉnh', 'Hóa đơn bị điều chỉnh', 'Đã bị điều chỉnh'),
       (11, 'Trống (Đã cấp số, chờ ký)', 'Hóa đơn đã được cấp số và chưa ký', 'Đã cấp số'),
       (12, 'Không sử dụng', 'Hoá đơn bị xoá từ trạng thái mới tạo', 'Không dùng nữa'),
       (13, 'Chờ hủy', 'Hóa đơn đã được chuyển trạng thái chờ hủy và cần ký để hủy', 'Chờ ký hủy'),
       (14, 'Chờ điều chỉnh chiết khấu', 'Hóa đơn điều chỉnh chiết khấu chưa ký', 'Chờ ký số'),
       (15, 'Điều chỉnh chiết khấu', 'Hóa đơn điều chỉnh chiết khấu đã ký', 'Hoàn tất điều chỉnh')
ON DUPLICATE KEY UPDATE `name`        = VALUES(`name`),
                        `description` = VALUES(`description`);

-- 7.5. Danh mục Trạng thái Cơ quan Thuế
INSERT INTO `einv_tax_status` (`id`, `name`)
VALUES (0, 'Chưa có trạng thái của CQT'),
       (32, 'Chờ Thuế xử lý'),
       (33, 'Thuế đã duyệt'),
       (34, 'Cần rà soát'),
       (35, 'Cấp mã bị lỗi'),
       (36, 'Chờ cấp mã'),
       (37, 'Có sai sót'),
       (38, 'Lỗi được CQT trả về'),
       (39, 'Lỗi khi đẩy vào Queue của Thuế')
ON DUPLICATE KEY UPDATE `name` = VALUES(`name`);

-- 7.6. Danh mục Phương thức thanh toán
INSERT INTO `einv_payment_method` (`id`, `name`, `note`)
VALUES (1, 'TM', 'Tiền mặt'),
       (2, 'CK', 'Chuyển khoản'),
       (3, 'TM/CK', 'Tiền mặt/Chuyển khoản'),
       (4, 'Xuất hàng cho chi nhánh', 'Xuất nội bộ'),
       (5, 'Hàng biếu tặng', 'Tặng'),
       (6, 'Cấn trừ công nợ', 'Cấn trừ'),
       (7, 'Trả hàng', 'Trả lại'),
       (8, 'Khuyến mại không thu tiền', 'Khuyến mại'),
       (9, 'Xuất sử dụng', 'Xuất sử dụng'),
       (10, 'Không thu tiền', 'Không thu tiền'),
       (11, 'D/A', 'Nhờ thu chấp nhận chứng từ'),
       (12, 'D/P', 'Nhờ thu đổi chứng từ'),
       (13, 'TT', 'Trả trước'),
       (14, 'L/C', 'Thư tín dụng'),
       (15, 'Công nợ', 'Công nợ'),
       (16, 'Nhờ thu', 'Nhờ thu'),
       (17, 'TM/CK/B', 'TM/CK/B'),
       (18, 'Thẻ tín dụng', 'Thẻ tín dụng'),
       (19, 'CK/Cấn trừ công nợ', 'CK/Cấn trừ'),
       (20, 'Hàng hóa', 'Hàng hóa'),
       (21, 'Hàng mẫu', 'Hàng mẫu'),
       (22, 'Thẻ', 'Thẻ'),
       (23, 'Bù trừ công nợ', 'Bù trừ'),
       (24, 'Qua LAZADA', 'Qua LAZADA'),
       (25, 'Qua TIKI', 'Qua TIKI'),
       (26, 'Xuất hóa đơn nội bộ', 'Xuất nội bộ'),
       (27, 'T/T', 'Chuyển tiền bằng điện'),
       (28, 'TTR', 'Chuyển tiền bồi hoàn bằng điện'),
       (29, 'TM/CK/Qua LAZADA', 'TM/CK/Qua LAZADA'),
       (30, 'TM/CK/Qua TIKI', 'TM/CK/Qua TIKI'),
       (31, 'TM/The', 'TM/Thẻ'),
       (32, 'CK/The', 'CK/Thẻ'),
       (33, 'TM/CK/The', 'TM/CK/Thẻ'),
       (34, 'CK/LC', 'Chuyển khoản/Thư tín dụng'),
       (35, 'L/C at sight', 'Thư tín dụng trả ngay'),
       (36, 'Xuất hàng hoá, dịch vụ trả thay lương', 'Xuất hàng trả thay lương'),
       (38, 'CAD', 'Giao chứng từ trả tiền'),
       (39, 'Qua SHOPEE', 'Qua SHOPEE'),
       (40, 'TM/CK/Qua SHOPEE', 'TM/CK/Qua SHOPEE'),
       (41, 'Cho vay/Cho mượn', 'Cho vay/Cho mượn'),
       (42, 'Ví điện tử', 'Ví điện tử'),
       (43, 'Điểm', 'Điểm'),
       (44, 'Voucher', 'Voucher')
ON DUPLICATE KEY UPDATE `name` = VALUES(`name`),
                        `note` = VALUES(`note`);

-- 7.7. Danh mục Đơn vị tính
INSERT INTO `einv_unit` (`code`, `name`)
VALUES ('DVT01', 'Cái'),
       ('DVT02', 'Chiếc'),
       ('DVT03', 'Cuộn'),
       ('DVT04', 'Mét'),
       ('DVT05', 'Kg'),
       ('DVT06', 'Tấn'),
       ('DVT07', 'Lít'),
       ('DVT08', 'Thùng'),
       ('DVT09', 'Hộp'),
       ('DVT10', 'Bộ')
ON DUPLICATE KEY UPDATE `name` = VALUES(`name`);

-- 7.8. Danh mục Loại hàng hóa
INSERT INTO `einv_item_type` (`id`, `name`, `note`)
VALUES (0, 'Hàng hoá dịch vụ (mặc định)', 'Mặc định'),
       (1, 'Thuế khác', 'Thuế khác'),
       (2, 'Phí khác', 'Phí khác'),
       (3, 'Phí phục vụ', 'Phí phục vụ'),
       (4, 'Ghi chú', 'Ghi chú/Diễn giải'),
       (5, 'Phụ thu', 'Phụ thu'),
       (6, 'Phí Hoàn', 'Phí Hoàn'),
       (7, 'Lệ Phí', 'Lệ Phí'),
       (8, 'Phí An ninh', 'Phí An ninh'),
       (9, 'Số tiền bằng chữ', 'Số tiền bằng chữ'),
       (10, 'Tiền đất giảm trừ khi tính thuế', 'Tiền đất giảm trừ'),
       (11, 'Điều chỉnh Hoá đơn ngoài hệ thống', 'Điều chỉnh HĐ ngoài hệ thống'),
       (12, 'Diễn giải Hàng hóa đi kèm có đánh STT', 'Hàng hóa đi kèm có STT'),
       (13, 'Diễn giải Hàng hóa đi kèm không đánh STT', 'Hàng hóa đi kèm không STT'),
       (14, 'Phí dịch vụ cho hóa đơn hoàn vé', 'Phí dịch vụ hoàn vé'),
       (15, 'Hàng hoá khuyến mãi', 'Hàng khuyến mãi'),
       (16, 'Giảm 20% mức tỷ lệ % trên doanh thu', 'Giảm 20%'),
       (17, 'Thuế tiêu thụ đặc biệt', 'Thuế tiêu thụ đặc biệt')
ON DUPLICATE KEY UPDATE `name` = VALUES(`name`);

-- 7.9. Danh mục Hình thức nhận hóa đơn
INSERT INTO `einv_receive_type` (`id`, `name`)
VALUES (1, 'Email'),
       (2, 'SMS'),
       (3, 'Email & SMS'),
       (4, 'Chuyển phát nhanh')
ON DUPLICATE KEY UPDATE `name` = VALUES(`name`);

-- 7.10. Danh mục Loại tham chiếu
INSERT INTO `einv_reference_type` (`id`, `name`, `note`)
VALUES (0, 'Hóa đơn gốc', 'Hóa đơn ban đầu'),
       (2, 'Hóa đơn điều chỉnh', 'Hóa đơn điều chỉnh thông tin'),
       (3, 'Hóa đơn thay thế', 'Hóa đơn thay thế hóa đơn sai sót')
ON DUPLICATE KEY UPDATE `name` = VALUES(`name`);

-- 7.11. Danh mục Loại nghiệp vụ Submit
INSERT INTO `einv_submit_invoice_type` (`id`, `name`, `description`)
VALUES ('100', 'Tạo hóa đơn mới', 'Tạo hóa đơn mới, chưa cấp số hóa đơn'),
       ('101', 'Tạo hóa đơn mới (Cấp số)', 'Tạo hóa đơn mới, cấp sẵn số hóa đơn, chưa ký'),
       ('102', 'Tạo và ký phát hành', 'Tạo mới hóa đơn và ký phát hành luôn'),
       ('110', 'Tạo hóa đơn (PMKT cấp mẫu)', 'Mẫu số, ký hiệu do PMKT cấp, Số HĐ = 0'),
       ('111', 'Tạo hóa đơn (PMKT cấp đủ)', 'Mẫu số, ký hiệu, số hóa đơn do PMKT cấp'),
       ('112', 'Tạo hóa đơn (PMKT cấp, Bkav số)', 'Mẫu số, ký hiệu do PMKT cấp, số HĐ do Bkav cấp'),
       ('120', 'Thay thế (Số HĐ = 0)', 'Tạo Hóa đơn thay thế (Số HĐ = 0)'),
       ('121', 'Điều chỉnh (Số HĐ = 0)', 'Tạo Hóa đơn điều chỉnh (Số HĐ = 0)'),
       ('122', 'Điều chỉnh chiết khấu', 'Tạo Hóa đơn điều chỉnh chiết khấu'),
       ('123', 'Thay thế (Bkav cấp số)', 'Tạo Hóa đơn thay thế, số HĐ do Bkav cấp'),
       ('124', 'Điều chỉnh (Bkav cấp số)', 'Tạo Hóa đơn điều chỉnh, số HĐ do Bkav cấp'),
       ('125', 'Điều chỉnh HĐ ngoài hệ thống', 'Tạo Hóa đơn điều chỉnh cho Hoá đơn ngoài hệ thống'),
       ('126', 'Điều chỉnh chiết khấu (Bkav cấp số)', 'Tạo Hóa đơn điều chỉnh chiết khấu, số HĐ do Bkav cấp'),
       ('127', 'Điều chỉnh HĐ ngoài hệ thống (Bkav cấp số)',
        'Tạo Hóa đơn điều chỉnh cho Hoá đơn ngoài hệ thống, số HĐ do Bkav cấp'),
       ('128', 'Thay thế HĐ ngoài hệ thống (Bkav cấp số)',
        'Tạo Hóa đơn Thay thế cho Hoá đơn ngoài hệ thống, số HĐ do Bkav cấp');

-- 7.12. Mapping Lệnh Nghiệp vụ (Ví dụ với BKAV)
INSERT INTO `einv_mapping_action` (`provider_id`, `hub_action`, `provider_cmd`, `description`)
VALUES ('550e8400-e29b-41d4-a716-446655440001', 'SUBMIT_NEW', '100', 'Tạo hóa đơn mới, chưa cấp số'),
       ('550e8400-e29b-41d4-a716-446655440001', 'SUBMIT_ASSIGNED', '101', 'Tạo hóa đơn mới, cấp sẵn số, chưa ký'),
       ('550e8400-e29b-41d4-a716-446655440001', 'SUBMIT_PUBLISH', '102', 'Tạo mới hóa đơn và ký phát hành luôn'),
       ('550e8400-e29b-41d4-a716-446655440001', 'ADJUST_ASSIGNED', '111', 'Tạo HĐ điều chỉnh, cấp sẵn số, chưa ký'),
       ('550e8400-e29b-41d4-a716-446655440001', 'ADJUST_PUBLISH', '112', 'Tạo HĐ điều chỉnh và ký phát hành luôn'),
       ('550e8400-e29b-41d4-a716-446655440001', 'REPLACE_NEW', '120', 'Tạo HĐ thay thế, số HĐ = 0'),
       ('550e8400-e29b-41d4-a716-446655440001', 'REPLACE_ASSIGNED', '123', 'Tạo HĐ thay thế, số HĐ do Bkav cấp'),
       ('550e8400-e29b-41d4-a716-446655440001', 'ADJUST_NEW', '121', 'Tạo HĐ điều chỉnh, số HĐ = 0'),
       ('550e8400-e29b-41d4-a716-446655440001', 'ADJUST_ASSIGNED2', '124', 'Tạo HĐ điều chỉnh, số HĐ do Bkav cấp'),
       ('550e8400-e29b-41d4-a716-446655440001', 'CANCEL', '201', 'Hủy hóa đơn đã phát hành (theo InvoiceGUID)'),
       ('550e8400-e29b-41d4-a716-446655440001', 'CANCEL_BY_ID', '202', 'Hủy hóa đơn đã phát hành (theo PartnerID)'),
       ('550e8400-e29b-41d4-a716-446655440001', 'DELETE', '301', 'Xóa hóa đơn mới tạo (theo PartnerID)'),
       ('550e8400-e29b-41d4-a716-446655440001', 'DELETE_BY_GUID', '303', 'Xóa hóa đơn mới tạo (theo InvoiceGUID)'),
       ('550e8400-e29b-41d4-a716-446655440001', 'GET_INVOICE', '800', 'Lấy thông tin hóa đơn'),
       ('550e8400-e29b-41d4-a716-446655440001', 'GET_STATUS', '801', 'Lấy trạng thái của tờ Hóa đơn'),
       ('550e8400-e29b-41d4-a716-446655440001', 'GET_HISTORY', '802', 'Lấy lịch sử thay đổi của tờ hóa đơn'),
       ('550e8400-e29b-41d4-a716-446655440001', 'SIGN_HSM', '205', 'Ký hóa đơn bằng HSM'),
       ('550e8400-e29b-41d4-a716-446655440001', 'SIGN_MULTI_HSM', '206', 'Ký nhiều hóa đơn bằng HSM');

SET FOREIGN_KEY_CHECKS = 1;
