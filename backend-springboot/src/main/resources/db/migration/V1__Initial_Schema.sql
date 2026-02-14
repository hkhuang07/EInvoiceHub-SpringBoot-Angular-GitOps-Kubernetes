-- =============================================================================
-- EInvoiceHub Database Schema - Core + Shell Strategy
-- Database: MariaDB | Character Set: utf8mb4_unicode_ci
-- =============================================================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- SECTION 1: CATALOG TABLES (Reference Data)
-- 1. Invoice Types - Danh mục loại hóa đơn
CREATE TABLE `invoice_types`
(
    `id`            BIGINT AUTO_INCREMENT PRIMARY KEY,
    `type_code`     VARCHAR(10)  NOT NULL UNIQUE COMMENT 'Mã loại hóa đơn (VD: 01GTKT)',
    `type_name`     VARCHAR(100) NOT NULL COMMENT 'Tên loại hóa đơn',
    `description`   TEXT         NULL COMMENT 'Mô tả chi tiết',
    `is_active`     BOOLEAN      NOT NULL DEFAULT TRUE COMMENT 'Còn hiệu lực',
    `display_order` INT          NOT NULL DEFAULT 0 COMMENT 'Thứ tự hiển thị',
    `created_at`    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX `idx_type_code` (`type_code`),
    INDEX `idx_is_active` (`is_active`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='Danh mục loại hóa đơn';
INSERT INTO invoice_types (type_code, type_name, description, display_order)
VALUES ('01GTKT', 'Hóa đơn giá trị gia tăng', 'Hóa đơn GTGT mẫu 01GTKT theo Thông tư 78/2021/TT-BTC', 1),
       ('02GTTT', 'Hóa đơn bán hàng', 'Hóa đơn bán hàng mẫu 02GTTT theo Thông tư 78/2021/TT-BTC', 2),
       ('07KPTQ', 'Hóa đơn điện tử kiểm soát', 'Hóa đơn điện tử kiểm soát theo Nghị định 123/2020/NĐ-CP', 3),
       ('03KPTQ', 'Hóa đơn điện tử không kiểm soát', 'Hóa đơn điện tử không kiểm soát theo Nghị định 123/2020/NĐ-CP',
        4);

-- 2. Invoice Statuses - Danh mục trạng thái hóa đơn
CREATE TABLE `invoice_statuses`
(
    `id`          INT PRIMARY KEY COMMENT 'ID trạng thái',
    `name`        VARCHAR(100) NOT NULL UNIQUE COMMENT 'Tên trạng thái',
    `description` VARCHAR(255) NULL COMMENT 'Mô tả chi tiết trạng thái',
    `note`        VARCHAR(500) NULL COMMENT 'Ghi chú nghiệp vụ',
    `created_at`  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at`  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='Bảng danh mục trạng thái hóa đơn';
INSERT INTO invoice_statuses (id, name, description, note)
VALUES (1, 'DRAFT', 'Hóa đơn nháp', 'Mới tạo, chưa ký'),
       (2, 'PENDING', 'Đang xử lý', 'Đang chờ gửi sang Provider'),
       (3, 'SIGNING', 'Đang ký số', 'Đang thực hiện ký điện tử'),
       (4, 'SENT_TO_PROVIDER', 'Đã gửi Provider', 'Đã gửi nhưng chưa có kết quả từ CQT'),
       (5, 'SUCCESS', 'Thành công', 'Hóa đơn đã hợp lệ và có mã CQT'),
       (6, 'FAILED', 'Thất bại', 'Lỗi từ Provider hoặc CQT'),
       (7, 'CANCELLED', 'Đã hủy', 'Hóa đơn đã bị hủy bỏ'),
       (8, 'REPLACED', 'Đã thay thế', 'Đã có hóa đơn khác thay thế cho hóa đơn này');

-- 3. Payment Methods - Danh mục phương thức thanh toán
CREATE TABLE `payment_methods`
(
    `id`            BIGINT AUTO_INCREMENT PRIMARY KEY,
    `method_code`   VARCHAR(10)  NOT NULL UNIQUE COMMENT 'Mã phương thức: TM, CK...',
    `method_name`   VARCHAR(100) NOT NULL COMMENT 'Tên phương thức',
    `description`   TEXT         NULL COMMENT 'Mô tả chi tiết',
    `is_active`     BOOLEAN      NOT NULL DEFAULT TRUE COMMENT 'Còn hiệu lực',
    `display_order` INT          NOT NULL DEFAULT 0 COMMENT 'Thứ tự hiển thị',
    `created_at`    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX `idx_method_code` (`method_code`),
    INDEX `idx_is_active` (`is_active`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='Danh mục phương thức thanh toán';
INSERT INTO payment_methods (method_code, method_name, description, display_order)
VALUES ('TM', 'Tiền mặt', 'Thanh toán bằng tiền mặt trực tiếp', 1),
       ('CK', 'Chuyển khoản', 'Thanh toán qua chuyển khoản ngân hàng', 2),
       ('TM/CK', 'Tiền mặt/Chuyển khoản', 'Kết hợp cả hai hình thức', 3),
       ('POS', 'Thẻ POS', 'Thanh toán qua máy POS', 4),
       ('VÍ ĐIỆN TỬ', 'Ví điện tử', 'Thanh toán qua ví điện tử', 5),
       ('THẺ', 'Thẻ ngân hàng', 'Thanh toán bằng thẻ tín dụng/ghi nợ', 6);

-- 4. VAT Rates - Danh mục thuế suất GTGT
CREATE TABLE `vat_rates`
(
    `id`            BIGINT AUTO_INCREMENT PRIMARY KEY,
    `rate_code`     VARCHAR(10)   NOT NULL UNIQUE COMMENT 'Mã thuế suất: 0, 5, 8, 10, KCT',
    `rate_percent`  DECIMAL(5, 2) NOT NULL COMMENT 'Giá trị phần trăm thuế suất',
    `rate_name`     VARCHAR(100)  NOT NULL COMMENT 'Tên thuế suất',
    `description`   TEXT          NULL COMMENT 'Mô tả chi tiết',
    `is_active`     BOOLEAN       NOT NULL DEFAULT TRUE COMMENT 'Còn hiệu lực',
    `display_order` INT           NOT NULL DEFAULT 0 COMMENT 'Thứ tự hiển thị',
    `created_at`    TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Thời điểm tạo',
    `updated_at`    TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Thời điểm cập nhật',
    CONSTRAINT `chk_vat_rate_percent` CHECK (rate_percent >= 0),
    INDEX `idx_rate_code` (`rate_code`),
    INDEX `idx_rate_percent` (`rate_percent`),
    INDEX `idx_is_active` (`is_active`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='Danh mục thuế suất GTGT';
INSERT INTO vat_rates (rate_code, rate_percent, rate_name, description, display_order)
VALUES ('0', 0.00, 'Thuế suất 0%', 'Áp dụng cho hàng hóa, dịch vụ xuất khẩu', 1),
       ('5', 5.00, 'Thuế suất 5%', 'Áp dụng cho hàng hóa, dịch vụ thiết yếu', 2),
       ('8', 8.00, 'Thuế suất 8%', 'Giảm thuế theo Nghị quyết 43/2022/QH15', 3),
       ('10', 10.00, 'Thuế suất 10%', 'Thuế suất thông thường GTGT', 4),
       ('KCT', 0.00, 'Không chịu thuế', 'Hàng hóa, dịch vụ không chịu thuế GTGT', 5);

-- 5. Service Providers - Danh mục nhà cung cấp hóa đơn điện tử
CREATE TABLE `service_providers`
(
    `id`                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    `provider_code`       VARCHAR(20)  NOT NULL UNIQUE COMMENT 'Mã định danh (VNPT, VIETTEL...)',
    `provider_name`       VARCHAR(100) NOT NULL COMMENT 'Tên nhà cung cấp',
    `official_api_url`    VARCHAR(500) NOT NULL COMMENT 'URL tích hợp',
    `lookup_url`          VARCHAR(500) NULL COMMENT 'URL tra cứu hóa đơn',
    `documentation_url`   VARCHAR(500) NULL COMMENT 'Link tài liệu API',
    `support_email`       VARCHAR(255) NULL,
    `support_phone`       VARCHAR(20)  NULL,
    `is_active`           BOOLEAN      NOT NULL DEFAULT TRUE COMMENT 'Kiểm tra còn hoạt động không',
    `is_default`          BOOLEAN      NOT NULL DEFAULT FALSE COMMENT 'Provider mặc định hệ thống',
    `display_order`       INT          NOT NULL DEFAULT 0,
    `extra_config_schema` JSON         NULL COMMENT 'Cấu hình đặc thù của từng hãng',
    `created_at`          TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`          TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX `idx_provider_code` (`provider_code`),
    INDEX `idx_is_active` (`is_active`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='Danh mục nhà cung cấp hóa đơn điện tử';
INSERT INTO service_providers (provider_code, provider_name, official_api_url, lookup_url, is_active, display_order)
VALUES ('VNPT', 'VNPT e-Invoice', 'https://api-itg.vnpt.vn', 'https://tracuu.vnpt.vn', TRUE, 1),
       ('VIETTEL', 'SInvoice Viettel', 'https://sinvoice.viettel.vn/api', 'https://sinvoice.viettel.vn/tracuu', TRUE,
        2),
       ('BKAV', 'BKAV eInvoice', 'https://einvoice.bkav.com/api', 'https://tracuuhdon.bkav.com', TRUE, 3),
       ('MOBIFONE', 'MobiFone Invoice', 'https://thongke.mobifoneinvoice.vn/api/v1',
        'https://mobifoneinvoice.vn/tracuu', TRUE, 4);



-- SECTION 2: CORE JPA TABLES (Business Logic Foundation)
-- CORE TABLE 1: Merchants (Root Entity)
CREATE TABLE `merchants`
(
    -- Core Fields (Required for JPA compatibility)
    `id`                   BIGINT AUTO_INCREMENT PRIMARY KEY,
    `tax_code`             VARCHAR(20)                        NOT NULL UNIQUE COMMENT 'Mã số thuế - định danh doanh nghiệp',
    `company_name`         VARCHAR(255)                       NOT NULL COMMENT 'Tên công ty đầy đủ',
    `short_name`           VARCHAR(100) COMMENT 'Tên viết tắt',
    `address`              TEXT COMMENT 'Địa chỉ trụ sở chính',
    `district`             VARCHAR(100) COMMENT 'Quận/Huyện',
    `city`                 VARCHAR(100) COMMENT 'Tỉnh/Thành phố',
    `email`                VARCHAR(255) COMMENT 'Email liên hệ',
    `phone`                VARCHAR(20) COMMENT 'Số điện thoại',
    `representative_name`  VARCHAR(100) COMMENT 'Tên người đại diện',
    `representative_title` VARCHAR(100) COMMENT 'Chức danh người đại diện',
    `tax_authority_code`   VARCHAR(10)                        NULL COMMENT 'Mã cơ quan thuế quản lý (VD: 01GD, 02HT...)',

    -- Enterprise Shell Fields (SaaS Extensions - NULLABLE for JPA compatibility)
    `subscription_plan`    ENUM ('TRIAL', 'BASIC', 'PREMIUM') NOT NULL DEFAULT 'TRIAL' COMMENT 'Gói dịch vụ: TRIAL, BASIC, PREMIUM',
    `invoice_quota`        INT                                NOT NULL DEFAULT 100 COMMENT 'Hạn mức hóa đơn/tháng',
    `invoice_used`         INT                                NOT NULL DEFAULT 0 COMMENT 'Hóa đơn đã sử dụng trong tháng',
    `is_using_hsm`         BOOLEAN                            NOT NULL DEFAULT FALSE COMMENT 'Sử dụng HSM hay USB Token để ký điện tử',
    `is_deleted`           BOOLEAN                            NOT NULL DEFAULT FALSE COMMENT 'Cờ xóa mềm',
    `deleted_at`           TIMESTAMP                          NULL COMMENT 'Thời điểm xóa',
    `created_at`           TIMESTAMP                          NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Thời điểm tạo',
    `updated_at`           TIMESTAMP                          NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Thời điểm cập nhật',

    INDEX `idx_tax_code` (`tax_code`),
    INDEX `idx_subscription_plan` (`subscription_plan`),
    INDEX `idx_created_at` (`created_at`),
    INDEX `idx_tax_authority` (`tax_authority_code`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='Quản lý doanh nghiệp sử dụng dịch vụ hóa đơn điện tử';

-- CORE TABLE 2: Merchant User (Child of Merchant)
CREATE TABLE `merchant_user`
(
    `id`                    BIGINT AUTO_INCREMENT PRIMARY KEY,
    `merchant_id`           BIGINT                                       NOT NULL COMMENT 'Liên kết với doanh nghiệp',
    `username`              VARCHAR(50)                                  NOT NULL UNIQUE COMMENT 'Tên đăng nhập',
    `email`                 VARCHAR(255)                                 NOT NULL UNIQUE COMMENT 'Email người dùng',
    `password_hash`         VARCHAR(255)                                 NOT NULL COMMENT 'Mật khẩu đã mã hóa BCrypt',
    `full_name`             VARCHAR(100) COMMENT 'Họ và tên đầy đủ',
    `phone`                 VARCHAR(20) COMMENT 'Số điện thoại',
    `avatar_url`            VARCHAR(500) COMMENT 'URL ảnh đại diện',
    `role`                  ENUM ('ADMIN', 'MANAGER', 'STAFF', 'VIEWER') NOT NULL DEFAULT 'STAFF' COMMENT 'Vai trò trong hệ thống',
    `is_active`             BOOLEAN                                      NOT NULL DEFAULT TRUE COMMENT 'Kích hoạt tài khoản',
    `is_primary`            BOOLEAN                                      NOT NULL DEFAULT FALSE COMMENT 'Tài khoản chính của doanh nghiệp',
    `last_login_at`         TIMESTAMP                                    NULL COMMENT 'Đăng nhập lần cuối',
    `failed_login_attempts` INT                                          NOT NULL DEFAULT 0 COMMENT 'Số lần đăng nhập sai',
    `locked_until`          TIMESTAMP                                    NULL COMMENT 'Khóa tài khoản đến thời điểm',
    `created_at`            TIMESTAMP                                    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Thời điểm tạo',
    `updated_at`            TIMESTAMP                                    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Thời điểm cập nhật',

    CONSTRAINT `fk_merchant_user_merchant` FOREIGN KEY (`merchant_id`)
        REFERENCES `merchants` (`id`) ON DELETE CASCADE,

    INDEX `idx_merchant_id` (`merchant_id`),
    INDEX `idx_username` (`username`),
    INDEX `idx_email` (`email`),
    INDEX `idx_role` (`role`),
    INDEX `idx_is_active` (`is_active`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='Tài khoản người dùng thuộc Merchant';

-- CORE TABLE 3: Merchant Provider Configs (Child of Merchant)
CREATE TABLE `merchant_provider_configs`
(
    -- Core Fields
    `id`                         BIGINT AUTO_INCREMENT PRIMARY KEY,
    `merchant_id`                BIGINT       NOT NULL COMMENT 'Doanh nghiệp sở hữu',
    `provider_id`                BIGINT       NOT NULL COMMENT 'Nhà cung cấp được chọn',

    -- Connection Credentials
    `partner_id`                 VARCHAR(50)  NULL COMMENT 'ID đối tác (partner_id)',
    `partner_token`              VARCHAR(500) NULL COMMENT 'Token truy cập (partner_token)',
    `tax_code`                   VARCHAR(20)  NULL COMMENT 'Mã số thuế kết nối',
    `integration_url`            VARCHAR(500) NULL COMMENT 'URL tích hợp riêng',
    `integrated_date`            TIMESTAMP    NULL COMMENT 'Ngày tích hợp',

    -- Service Account
    `username_service`           VARCHAR(100) NOT NULL COMMENT 'Tài khoản API',
    `password_service_encrypted` VARCHAR(500) NOT NULL COMMENT 'Mật khẩu API (đã mã hóa)',

    -- Digital Certificate
    `certificate_serial`         VARCHAR(100) NULL,
    `certificate_data`           TEXT         NULL,
    `certificate_chain`          LONGTEXT     NULL,
    `certificate_expired_at`     TIMESTAMP    NULL,

    -- Extra Configuration
    `extra_config`               JSON         NULL COMMENT 'Cấu hình bổ sung (pattern, serial...)',

    -- Enterprise Shell Fields (NULLABLE for JPA compatibility)
    `is_test_mode`               BOOLEAN      NOT NULL DEFAULT FALSE COMMENT 'Chế độ kiểm thử (Sandbox)',
    `encrypted_password_storage` VARCHAR(500) NULL COMMENT 'Lưu trữ mật khẩu đã mã hóa (AES256)',

    -- Status & Timestamps
    `is_active`                  BOOLEAN      NOT NULL DEFAULT TRUE COMMENT 'Trạng thái kích hoạt',
    `is_default`                 BOOLEAN      NOT NULL DEFAULT FALSE COMMENT 'Cấu hình mặc định',
    `last_sync_at`               TIMESTAMP    NULL COMMENT 'Thời điểm đồng bộ cuối',
    `created_at`                 TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`                 TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT `fk_mpc_merchant` FOREIGN KEY (`merchant_id`) REFERENCES `merchants` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_mpc_provider` FOREIGN KEY (`provider_id`) REFERENCES `service_providers` (`id`) ON DELETE RESTRICT,
    CONSTRAINT `uq_merchant_provider` UNIQUE (`merchant_id`, `provider_id`),

    INDEX `idx_merchant_provider` (`merchant_id`, `provider_id`),
    INDEX `idx_is_active` (`is_active`),
    INDEX `idx_is_test_mode` (`is_test_mode`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='Bảng cấu hình chi tiết NCC cho từng Merchant';

-- CORE TABLE 4: Invoice Template (Child of Merchant)
CREATE TABLE `invoice_template`
(
    `id`                      BIGINT AUTO_INCREMENT PRIMARY KEY,
    `merchant_id`             BIGINT      NOT NULL COMMENT 'Doanh nghiệp sở hữu',
    `invoice_type_id`         BIGINT      NULL COMMENT 'Loại hóa đơn',
    `invoice_registration_id` BIGINT      NULL COMMENT 'Liên kết đăng ký dải số',
    `provider_id`             VARCHAR(36) NULL COMMENT 'Mã NCC',
    `provider_serial_id`      VARCHAR(50) NULL COMMENT 'ID dải số phía NCC',

    -- Template Configuration
    `template_code`           VARCHAR(20) NOT NULL COMMENT 'Mẫu hóa đơn: 01GTKT0/001',
    `symbol_code`             VARCHAR(10) NOT NULL COMMENT 'Ký hiệu hóa đơn: AA/22E',

    -- Serial Number Management
    `current_number`          INT         NOT NULL DEFAULT 0 COMMENT 'Số hiện tại',
    `min_number`              INT         NOT NULL DEFAULT 1,
    `max_number`              INT         NOT NULL DEFAULT 99999999,
    `start_date`              TIMESTAMP   NULL COMMENT 'Ngày bắt đầu hiệu lực',

    -- Status
    `status_id`               INT         NULL COMMENT 'ID trạng thái',
    `is_active`               BOOLEAN     NOT NULL DEFAULT TRUE,

    -- Timestamps
    `created_at`              TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`              TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT `fk_template_merchant` FOREIGN KEY (`merchant_id`) REFERENCES `merchants` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_template_type` FOREIGN KEY (`invoice_type_id`) REFERENCES `invoice_types` (`id`) ON DELETE SET NULL,
    CONSTRAINT `uq_merchant_template_symbol` UNIQUE (`merchant_id`, `template_code`, `symbol_code`),

    INDEX `idx_template_codes` (`template_code`, `symbol_code`),
    INDEX `idx_is_active` (`is_active`),
    INDEX `idx_merchant_id` (`merchant_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='Quản lý mẫu số và ký hiệu hóa đơn';

-- CORE TABLE 5: Invoice Registrations (Supporting Core Table)
-- Registration Statuses
CREATE TABLE `registration_statuses`
(
    `id`          INT PRIMARY KEY COMMENT 'ID trạng thái',
    `name`        VARCHAR(100) NOT NULL UNIQUE COMMENT 'Tên trạng thái',
    `description` VARCHAR(255) NULL COMMENT 'Mô tả chi tiết',
    `note`        VARCHAR(500) NULL COMMENT 'Ghi chú nghiệp vụ',
    `created_at`  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at`  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='Danh mục trạng thái đăng ký dải số';
INSERT INTO registration_statuses (id, name, description, note)
VALUES (1, 'ACTIVE', 'Đang hiệu lực', 'Dải số đang được sử dụng'),
       (2, 'EXHAUSTED', 'Đã dùng hết', 'Toàn bộ số trong dải đã phát hành'),
       (3, 'EXPIRED', 'Đã hết hạn', 'Dải số đã quá ngày hiệu lực'),
       (4, 'CANCELLED', 'Đã hủy', 'Dải số bị hủy bỏ');

CREATE TABLE `invoice_registrations`
(
    `id`                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    `merchant_id`         BIGINT       NOT NULL COMMENT 'Doanh nghiệp sở hữu dải số',
    `registration_number` VARCHAR(50)  NULL COMMENT 'Số quyết định/phê duyệt của CQT',
    `from_number`         BIGINT       NOT NULL COMMENT 'Số bắt đầu của dải',
    `to_number`           BIGINT       NOT NULL COMMENT 'Số kết thúc của dải',
    `quantity`            BIGINT       NOT NULL COMMENT 'Số lượng hóa đơn trong dải',
    `current_number`      BIGINT       NOT NULL DEFAULT 0 COMMENT 'Số hiện tại đã sử dụng',
    `effective_date`      DATE         NOT NULL COMMENT 'Ngày có hiệu lực',
    `expiration_date`     DATE         NULL COMMENT 'Ngày hết hạn',
    `status_id`           INT          NOT NULL DEFAULT 1 COMMENT 'Trạng thái đăng ký',
    `issued_by`           VARCHAR(255) NULL COMMENT 'Người phê duyệt/Cơ quan Thuế',
    `note`                TEXT         NULL COMMENT 'Ghi chú bổ sung',
    `created_at`          TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`          TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT `fk_invoice_registrations_registration_statuses` FOREIGN KEY (`status_id`) REFERENCES `registration_statuses` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_registration_merchant` FOREIGN KEY (`merchant_id`) REFERENCES `merchants` (`id`) ON DELETE CASCADE,
    CONSTRAINT `chk_registration_range` CHECK (`from_number` <= `to_number`),

    INDEX `idx_registration_merchant` (`merchant_id`),
    INDEX `idx_registration_status` (`status_id`),
    INDEX `idx_registration_effective` (`effective_date`),
    INDEX `idx_registration_number` (`registration_number`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='Quản lý đăng ký dải số hóa đơn với Cơ quan Thuế';

-- CORE TABLE 6: Invoice Metadata (Central Transaction Table)
CREATE TABLE `invoice_metadata`
(
    -- Primary Key
    `id`                        BIGINT AUTO_INCREMENT PRIMARY KEY,

    -- Foreign Keys (Hierarchical references)
    `merchant_id`               BIGINT                                             NOT NULL COMMENT 'Liên kết doanh nghiệp sở hữu',
    `provider_id`               BIGINT                                             NULL COMMENT 'Liên kết nhà cung cấp dịch vụ',
    `provider_config_id`        BIGINT                                             NULL COMMENT 'Cấu hình Provider được sử dụng',
    `invoice_template_id`       BIGINT                                             NULL COMMENT 'Mẫu hóa đơn được sử dụng',
    `payment_method`            VARCHAR(10)                                        NULL COMMENT 'Mã phương thức thanh toán',

    -- Identifiers & References
    `client_request_id`         VARCHAR(100)                                       NULL COMMENT 'ID request từ hệ thống Merchant',
    `partner_invoice_id`        VARCHAR(50)                                        NULL COMMENT 'ID định danh từ đối tác',

    -- Invoice Information
    `invoice_number`            VARCHAR(20)                                        NULL COMMENT 'Số hóa đơn từ Provider trả về',
    `symbol_code`               VARCHAR(10)                                        NULL COMMENT 'Ký hiệu hóa đơn',
    `template_code`             VARCHAR(20)                                        NULL COMMENT 'Mẫu hóa đơn',
    `invoice_type_code`         VARCHAR(10)                                        NULL COMMENT 'Mã loại hóa đơn',
    `is_summary_invoice`        BOOLEAN                                            NOT NULL DEFAULT FALSE COMMENT 'Hóa đơn tổng hợp',
    `issuance_method`           ENUM ('STANDARD', 'POS')                           NOT NULL DEFAULT 'STANDARD' COMMENT 'Phương thức phát hành',

    -- Enterprise Shell Fields (NULLABLE for JPA compatibility)
    `lookup_code`               VARCHAR(50)                                        NULL COMMENT 'Mã tra cứu hóa đơn (Unique)',
    `cqt_code`                  VARCHAR(50)                                        NULL COMMENT 'Mã Cơ quan Thuế cấp',
    `provider_transaction_id`   VARCHAR(100)                                       NULL COMMENT 'ID giao dịch từ Provider',

    -- Seller Information (Flattened from Merchant)
    `seller_name`               VARCHAR(255)                                       NULL COMMENT 'Tên người bán',
    `seller_tax_code`           VARCHAR(20)                                        NULL COMMENT 'Mã số thuế người bán',
    `seller_address`            TEXT                                               NULL COMMENT 'Địa chỉ người bán',

    -- Buyer Information (FLATTENED - No Customer FK to prevent circular dependencies)
    `buyer_name`                VARCHAR(255)                                       NULL COMMENT 'Tên đơn vị người mua',
    `buyer_tax_code`            VARCHAR(20)                                        NULL COMMENT 'MST người mua',
    `buyer_id_no`               VARCHAR(20)                                        NULL COMMENT 'Số CMND/CCCD/Hộ chiếu',
    `buyer_full_name`           VARCHAR(200)                                       NULL COMMENT 'Họ tên người mua hàng',
    `buyer_email`               VARCHAR(255)                                       NULL COMMENT 'Email người mua',
    `buyer_phone`               VARCHAR(20)                                        NULL COMMENT 'Điện thoại người mua',
    `buyer_mobile`              VARCHAR(50)                                        NULL COMMENT 'Di động người mua',
    `buyer_address`             TEXT                                               NULL COMMENT 'Địa chỉ người mua',
    `buyer_bank_account`        VARCHAR(50)                                        NULL COMMENT 'Tài khoản ngân hàng',
    `buyer_bank_name`           VARCHAR(200)                                       NULL COMMENT 'Tên ngân hàng',
    `buyer_budget_code`         VARCHAR(20)                                        NULL COMMENT 'Mã chương/NSNN',

    -- Amounts & Currency (DECIMAL(18,2) for precision)
    `subtotal_amount`           DECIMAL(18, 2)                                     NOT NULL DEFAULT 0.00 COMMENT 'Tổng tiền trước thuế',
    `tax_amount`                DECIMAL(18, 2)                                     NOT NULL DEFAULT 0.00 COMMENT 'Tổng tiền thuế',
    `discount_amount`           DECIMAL(18, 2)                                     NOT NULL DEFAULT 0.00 COMMENT 'Tổng chiết khấu',
    `total_amount`              DECIMAL(18, 2)                                     NOT NULL DEFAULT 0.00 COMMENT 'Tổng tiền thanh toán',
    `gross_amount`              DECIMAL(18, 2)                                              DEFAULT 0.00 COMMENT 'Tổng tiền thô',
    `net_amount`                DECIMAL(18, 2)                                              DEFAULT 0.00 COMMENT 'Tổng tiền thuần',
    `currency_code`             VARCHAR(3)                                         NOT NULL DEFAULT 'VND' COMMENT 'Đơn vị tiền tệ',
    `exchange_rate`             DECIMAL(10, 4)                                     NOT NULL DEFAULT 1.0000 COMMENT 'Tỷ giá',

    -- Adjustment / Original Invoice Info
    `org_invoice_id`            VARCHAR(36)                                        NULL COMMENT 'ID hóa đơn gốc',
    `org_invoice_form`          VARCHAR(50)                                        NULL COMMENT 'Mẫu hóa đơn gốc',
    `org_invoice_series`        VARCHAR(50)                                        NULL COMMENT 'Ký hiệu hóa đơn gốc',
    `org_invoice_no`            VARCHAR(50)                                        NULL COMMENT 'Số hóa đơn gốc',
    `org_invoice_date`          TIMESTAMP                                          NULL COMMENT 'Ngày hóa đơn gốc',
    `org_invoice_reason`        VARCHAR(500)                                       NULL COMMENT 'Lý do điều chỉnh',
    `adjustment_type`           ENUM ('ADJUSTMENT', 'REPLACEMENT', 'CANCELLATION') NULL COMMENT 'Loại điều chỉnh',
    `cancellation_reason`       VARCHAR(500)                                       NULL COMMENT 'Lý do hủy',
    `replaced_by_invoice_id`    BIGINT                                             NULL COMMENT 'Hóa đơn thay thế',

    -- Status & Dates
    `status_id`                 INT                                                NOT NULL DEFAULT 1 COMMENT 'Trạng thái hóa đơn',
    `status_message`            TEXT                                               NULL COMMENT 'Thông báo lỗi hoặc trạng thái chi tiết',
    `issue_date`                DATE                                               NULL COMMENT 'Ngày lập hóa đơn',
    `accounting_date`           DATE                                               NULL COMMENT 'Ngày hạch toán',
    `due_date`                  DATE                                               NULL COMMENT 'Ngày hạn thanh toán',

    -- Timestamps for Audit Trail
    `signed_at`                 TIMESTAMP                                          NULL COMMENT 'Thời điểm ký số',
    `sent_to_provider_at`       TIMESTAMP                                          NULL COMMENT 'Thời điểm gửi NCC',
    `received_from_provider_at` TIMESTAMP                                          NULL COMMENT 'Thời điểm nhận phản hồi',
    `delivered_to_buyer_at`     TIMESTAMP                                          NULL COMMENT 'Thời điểm gửi khách hàng',

    -- Provider Feedback
    `provider_error_code`       VARCHAR(50)                                        NULL COMMENT 'Mã lỗi từ Provider',
    `provider_error_message`    TEXT                                               NULL COMMENT 'Chi tiết lỗi từ Provider',

    -- Soft Delete
    `is_deleted`                BOOLEAN                                            NOT NULL DEFAULT FALSE,
    `deleted_at`                TIMESTAMP                                          NULL COMMENT 'Thời điểm xóa',
    `deleted_by`                BIGINT                                             NULL COMMENT 'Người xóa',

    -- System Audit Timestamps
    `created_at`                TIMESTAMP                                          NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`                TIMESTAMP                                          NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Thời điểm cập nhật cuối',

    -- Constraints
    CONSTRAINT `fk_invoice_merchant` FOREIGN KEY (`merchant_id`) REFERENCES `merchants` (`id`),
    CONSTRAINT `fk_invoice_status` FOREIGN KEY (`status_id`) REFERENCES `invoice_statuses` (`id`),
    CONSTRAINT `fk_invoice_provider` FOREIGN KEY (`provider_id`) REFERENCES `service_providers` (`id`),
    CONSTRAINT `fk_invoice_template` FOREIGN KEY (`invoice_template_id`) REFERENCES `invoice_template` (`id`) ON DELETE SET NULL,
    CONSTRAINT `fk_invoice_config` FOREIGN KEY (`provider_config_id`) REFERENCES `merchant_provider_configs` (`id`) ON DELETE SET NULL,
    CONSTRAINT `fk_invoice_payment_method` FOREIGN KEY (`payment_method`) REFERENCES `payment_methods` (`method_code`) ON DELETE SET NULL,
    CONSTRAINT `fk_invoice_replaced_by` FOREIGN KEY (`replaced_by_invoice_id`) REFERENCES `invoice_metadata` (`id`) ON DELETE SET NULL,
    CONSTRAINT `fk_invoice_deleted_by` FOREIGN KEY (`deleted_by`) REFERENCES `merchant_user` (`id`) ON DELETE SET NULL,
    CONSTRAINT `uq_invoice_lookup_code` UNIQUE (`lookup_code`),

    -- Indexes for Search Optimization
    INDEX `idx_merchant_status` (`merchant_id`, `status_id`),
    INDEX `idx_invoice_number` (`invoice_number`),
    INDEX `idx_client_request_id` (`client_request_id`),
    INDEX `idx_partner_invoice` (`partner_invoice_id`),
    INDEX `idx_buyer_tax_code` (`buyer_tax_code`),
    INDEX `idx_issue_date` (`issue_date`),
    INDEX `idx_created_at` (`created_at`),
    INDEX `idx_cqt_code` (`cqt_code`),
    INDEX `idx_template_code` (`template_code`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='Bảng lưu trữ Metadata hóa đơn tổng hợp';

-- CORE TABLE 7: Invoice Items (Child of Invoice)
CREATE TABLE `invoice_items`
(
    `id`              BIGINT AUTO_INCREMENT PRIMARY KEY,
    `invoice_id`      BIGINT         NOT NULL COMMENT 'ID hóa đơn cha',
    `line_number`     INT            NOT NULL DEFAULT 1 COMMENT 'Số thứ tự dòng',

    -- Product Information
    `is_free`         BOOLEAN                 DEFAULT FALSE COMMENT 'Hàng khuyến mãi/biếu tặng',
    `product_type_id` INT            NULL COMMENT 'Loại hàng hóa/dịch vụ',
    `product_code`    VARCHAR(100)   NULL COMMENT 'Mã sản phẩm',
    `product_name`    VARCHAR(500)   NOT NULL COMMENT 'Tên hàng hóa/dịch vụ',
    `unit_name`       VARCHAR(50)    NULL COMMENT 'Đơn vị tính',

    -- Quantity & Unit Price
    `quantity`        DECIMAL(18, 4) NOT NULL DEFAULT 0 COMMENT 'Số lượng',
    `unit_price`      DECIMAL(18, 4) NOT NULL DEFAULT 0 COMMENT 'Đơn giá chưa thuế',

    -- Amount Calculations
    `gross_amount`    DECIMAL(18, 2)          DEFAULT 0 COMMENT 'Thành tiền thô',
    `discount_rate`   DECIMAL(5, 2)           DEFAULT 0 COMMENT 'Tỷ lệ chiết khấu (%)',
    `discount_amount` DECIMAL(18, 2)          DEFAULT 0 COMMENT 'Tiền chiết khấu',
    `net_price`       DECIMAL(18, 4)          DEFAULT 0 COMMENT 'Giá sau chiết khấu chưa thuế',
    `net_amount`      DECIMAL(18, 2)          DEFAULT 0 COMMENT 'Thành tiền sau chiết khấu trước thuế',

    -- Tax Information
    `vat_rate_id`     BIGINT                  NULL COMMENT 'Phân loại thuế',
    `tax_rate`        DECIMAL(5, 2)           DEFAULT 0 COMMENT 'Mức thuế suất (%)',
    `tax_amount`      DECIMAL(18, 2)          DEFAULT 0 COMMENT 'Tiền thuế GTGT',

    -- Total
    `total_amount`    DECIMAL(18, 2) NOT NULL DEFAULT 0 COMMENT 'Tổng thanh toán dòng',

    -- Description
    `description`     TEXT           NULL COMMENT 'Mô tả sản phẩm',
    `notes`           VARCHAR(500)   NULL COMMENT 'Ghi chú thêm',

    `created_at`      TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT `fk_item_invoice` FOREIGN KEY (`invoice_id`) REFERENCES `invoice_metadata` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_item_vat_rate` FOREIGN KEY (`vat_rate_id`) REFERENCES `vat_rates` (`id`) ON DELETE CASCADE,

    INDEX `idx_item_invoice` (`invoice_id`),
    INDEX `idx_product_code` (`product_code`),
    INDEX `idx_line_number` (`line_number`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='Chi tiết từng dòng hàng hóa/dịch vụ trong hóa đơn';

-- CORE TABLE 8: Invoice Tax Breakdowns (Supporting Core Table)
CREATE TABLE `invoice_tax_breakdowns`
(
    `id`               BIGINT AUTO_INCREMENT PRIMARY KEY,
    `invoice_id`       BIGINT         NOT NULL COMMENT 'ID hóa đơn cha',
    `vat_rate_id`      BIGINT         NOT NULL COMMENT 'Liên kết với danh mục thuế suất',
    `vat_rate_code`    VARCHAR(10)    NOT NULL COMMENT 'Mã thuế suất',
    `vat_rate_percent` DECIMAL(5, 2)  NOT NULL COMMENT 'Giá trị phần trăm thuế suất',

    `subtotal_amount`  DECIMAL(18, 2) NOT NULL DEFAULT 0.00 COMMENT 'Tổng tiền trước thuế',
    `discount_amount`  DECIMAL(18, 2) NOT NULL DEFAULT 0.00 COMMENT 'Tổng tiền chiết khấu',
    `taxable_amount`   DECIMAL(18, 2) NOT NULL DEFAULT 0.00 COMMENT 'Tổng tiền chịu thuế',
    `tax_amount`       DECIMAL(18, 2) NOT NULL DEFAULT 0.00 COMMENT 'Tổng tiền thuế GTGT',
    `total_amount`     DECIMAL(18, 2) NOT NULL DEFAULT 0.00 COMMENT 'Tổng tiền bao gồm thuế',

    `created_at`       TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Thời điểm tạo',

    CONSTRAINT `fk_tax_breakdown_invoice` FOREIGN KEY (`invoice_id`)
        REFERENCES `invoice_metadata` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_tax_breakdown_vat_rate` FOREIGN KEY (`vat_rate_id`)
        REFERENCES `vat_rates` (`id`) ON DELETE RESTRICT,
    CONSTRAINT `uq_invoice_vat_rate` UNIQUE (`invoice_id`, `vat_rate_id`),

    INDEX `idx_breakdown_invoice` (`invoice_id`),
    INDEX `idx_breakdown_vat_rate` (`vat_rate_id`),
    INDEX `idx_vat_rate_code` (`vat_rate_code`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='Phân rã thuế suất trên từng hóa đơn';

-- CORE TABLE 9: Invoice Adjustments (Supporting Core Table)
CREATE TABLE `invoice_adjustments`
(
    `id`                   BIGINT AUTO_INCREMENT PRIMARY KEY,
    `original_invoice_id`  BIGINT                                                NOT NULL COMMENT 'Hóa đơn gốc bị điều chỉnh/thay thế/hủy',
    `adjustment_type`      ENUM ('ADJUSTMENT', 'REPLACEMENT', 'CANCELLATION')    NOT NULL COMMENT 'Loại điều chỉnh',

    `agreement_number`     VARCHAR(50)                                           NULL COMMENT 'Số biên bản thỏa thuận',
    `agreement_date`       DATE                                                  NULL COMMENT 'Ngày lập biên bản',
    `agreement_content`    TEXT                                                  NULL COMMENT 'Nội dung biên bản thỏa thuận',
    `signers`              TEXT                                                  NULL COMMENT 'Người ký biên bản (JSON array)',

    `reason_code`          VARCHAR(50)                                           NULL COMMENT 'Mã lý do',
    `reason_description`   VARCHAR(500)                                          NULL COMMENT 'Mô tả lý do chi tiết',

    `old_subtotal_amount`  DECIMAL(18, 2)                                        NULL COMMENT 'Tổng tiền trước thuế cũ',
    `old_tax_amount`       DECIMAL(18, 2)                                        NULL COMMENT 'Tiền thuế cũ',
    `old_total_amount`     DECIMAL(18, 2)                                        NULL COMMENT 'Tổng tiền cũ',
    `new_subtotal_amount`  DECIMAL(18, 2)                                        NULL COMMENT 'Tổng tiền trước thuế mới',
    `new_tax_amount`       DECIMAL(18, 2)                                        NULL COMMENT 'Tiền thuế mới',
    `new_total_amount`     DECIMAL(18, 2)                                        NULL COMMENT 'Tổng tiền mới',
    `difference_amount`    DECIMAL(18, 2)                                        NULL COMMENT 'Chênh lệch tiền',

    `status`               ENUM ('PENDING', 'APPROVED', 'REJECTED', 'CANCELLED') NOT NULL DEFAULT 'PENDING' COMMENT 'Trạng thái duyệt',
    `approved_at`          TIMESTAMP                                             NULL COMMENT 'Thời điểm duyệt',

    `submitted_to_cqt`     BOOLEAN                                               NOT NULL DEFAULT FALSE COMMENT 'Đã gửi Cơ quan Thuế',
    `cqt_response_code`    VARCHAR(50)                                           NULL COMMENT 'Mã phản hồi từ CQT',
    `cqt_response_message` TEXT                                                  NULL COMMENT 'Nội dung phản hồi từ CQT',

    `created_at`           TIMESTAMP                                             NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Thời điểm tạo',
    `updated_at`           TIMESTAMP                                             NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Thời điểm cập nhật',

    CONSTRAINT `fk_adjustment_original_invoice` FOREIGN KEY (`original_invoice_id`)
        REFERENCES `invoice_metadata` (`id`) ON DELETE RESTRICT,

    INDEX `idx_adjustment_original` (`original_invoice_id`),
    INDEX `idx_adjustment_type` (`adjustment_type`),
    INDEX `idx_adjustment_status` (`status`),
    INDEX `idx_agreement_date` (`agreement_date`),
    INDEX `idx_submitted_cqt` (`submitted_to_cqt`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='Thông tin biên bản thỏa thuận cho hóa đơn điều chỉnh/thay thế/hủy';


-- SECTION 3: ENTERPRISE SHELL MODULES (Management & Auxiliary Tables)
-- MANAGEMENT TABLE 1: Invoice Payloads (One-to-One with Metadata)
CREATE TABLE `invoice_payloads`
(
    `invoice_id`   BIGINT    NOT NULL PRIMARY KEY COMMENT 'ID hóa đơn - khóa chính và khóa ngoại',
    `raw_data`     JSON      NULL COMMENT 'Dữ liệu thô request/response dạng JSON',
    `xml_content`  LONGTEXT  NULL COMMENT 'Nội dung XML gốc của hóa đơn',
    `json_content` LONGTEXT  NULL COMMENT 'Nội dung JSON đầy đủ của hóa đơn',
    `signed_xml`   LONGTEXT  NULL COMMENT 'XML đã ký điện tử',
    `pdf_data`     LONGTEXT  NULL COMMENT 'Dữ liệu PDF đã tạo (base64)',
    `extra_data`   JSON      NULL COMMENT 'Dữ liệu bổ sung khác',
    `created_at`   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Thời điểm lưu',
    `updated_at`   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Thời điểm cập nhật',

    CONSTRAINT `fk_invoice_payload_invoice` FOREIGN KEY (`invoice_id`)
        REFERENCES `invoice_metadata` (`id`) ON DELETE CASCADE,

    INDEX `idx_payload_created` (`created_at`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='Lưu trữ nội dung XML/JSON gốc hóa đơn';

-- MANAGEMENT TABLE 2: API Credentials
CREATE TABLE `api_credentials`
(
    `id`                        BIGINT AUTO_INCREMENT PRIMARY KEY,
    `merchant_id`               BIGINT       NOT NULL COMMENT 'Doanh nghiệp sở hữu credential',
    `client_id`                 VARCHAR(50)  NOT NULL UNIQUE COMMENT 'ID công khai của ứng dụng',
    `api_key_hash`              VARCHAR(255) NOT NULL COMMENT 'Hash của API Key (SHA-256)',
    `api_key_prefix`            VARCHAR(8)   NOT NULL COMMENT '8 ký tự đầu của API Key',
    `scopes`                    TEXT         NOT NULL COMMENT 'Quyền hạn - JSON array',
    `ip_whitelist`              TEXT         NULL COMMENT 'Danh sách IP được phép - JSON array',
    `rate_limit_per_hour`       INT          NOT NULL DEFAULT 1000 COMMENT 'Giới hạn request/giờ',
    `rate_limit_per_day`        INT          NOT NULL DEFAULT 10000 COMMENT 'Giới hạn request/ngày',
    `request_count_hour`        INT          NOT NULL DEFAULT 0 COMMENT 'Request đã dùng trong giờ',
    `request_count_day`         INT          NOT NULL DEFAULT 0 COMMENT 'Request đã dùng trong ngày',
    `request_count_resetted_at` TIMESTAMP    NULL COMMENT 'Thời điểm reset counter',
    `expired_at`                TIMESTAMP    NULL COMMENT 'Ngày hết hạn - NULL không giới hạn',
    `last_used_at`              TIMESTAMP    NULL COMMENT 'Lần sử dụng cuối',
    `is_active`                 BOOLEAN      NOT NULL DEFAULT TRUE COMMENT 'Còn hiệu lực',
    `created_at`                TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Thời điểm tạo',
    `updated_at`                TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Thời điểm cập nhật',

    -- Using INDEX instead of FK for performance (decoupled from merchant table)
    INDEX `idx_merchant_id` (`merchant_id`),
    INDEX `idx_client_id` (`client_id`),
    INDEX `idx_api_key_hash` (`api_key_hash`),
    INDEX `idx_expired_at` (`expired_at`),
    INDEX `idx_is_active` (`is_active`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='Credentials cho API access';

-- MANAGEMENT TABLE 3: Audit Logs
CREATE TABLE `audit_logs`
(
    `id`          BIGINT AUTO_INCREMENT PRIMARY KEY,
    `merchant_id` BIGINT       NULL COMMENT 'Doanh nghiệp liên quan',
    `user_id`     BIGINT       NULL COMMENT 'Người thực hiện',
    `entity_type` VARCHAR(50)  NOT NULL COMMENT 'Loại entity: Invoice, Merchant, Config...',
    `entity_id`   BIGINT       NOT NULL COMMENT 'ID của entity',
    `action`      VARCHAR(50)  NOT NULL COMMENT 'Hành động: CREATE, UPDATE, DELETE, STATUS_CHANGE',
    `old_value`   JSON         NULL COMMENT 'Giá trị cũ',
    `new_value`   JSON         NULL COMMENT 'Giá trị mới',
    `ip_address`  VARCHAR(45)  NULL COMMENT 'IP thực hiện',
    `user_agent`  VARCHAR(500) NULL COMMENT 'User Agent của browser/client',
    `request_id`  VARCHAR(100) NULL COMMENT 'ID request từ frontend',
    `created_at`  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Thời điểm xảy ra',

    -- Using INDEX instead of FK for high-performance logging
    INDEX `idx_merchant_id` (`merchant_id`),
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_entity` (`entity_type`, `entity_id`),
    INDEX `idx_action` (`action`),
    INDEX `idx_created_at` (`created_at`),
    INDEX `idx_merchant_entity` (`merchant_id`, `entity_type`, `entity_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='Nhật ký thay đổi hệ thống';

-- MANAGEMENT TABLE 4: Invoice Sync Queue
CREATE TABLE `invoice_sync_queue`
(
    `id`                      BIGINT AUTO_INCREMENT PRIMARY KEY,
    `invoice_id`              BIGINT                                                                        NOT NULL COMMENT 'ID hóa đơn cần đồng bộ',
    `sync_type`               ENUM ('SEND_TO_PROVIDER', 'SEND_TO_CQT', 'CANCEL_INVOICE', 'REPLACE_INVOICE') NOT NULL COMMENT 'Loại thao tác đồng bộ',
    `priority`                INT                                                                           NOT NULL DEFAULT 5 COMMENT 'Độ ưu tiên (1=cao nhất, 10=thấp nhất)',

    `status`                  ENUM ('PENDING', 'PROCESSING', 'COMPLETED', 'FAILED', 'RETRYING')             NOT NULL DEFAULT 'PENDING' COMMENT 'Trạng thái xử lý',
    `attempt_count`           INT                                                                           NOT NULL DEFAULT 0 COMMENT 'Số lần thử',
    `max_attempts`            INT                                                                           NOT NULL DEFAULT 3 COMMENT 'Số lần thử tối đa',
    `last_attempt_at`         TIMESTAMP                                                                     NULL COMMENT 'Thời điểm thử lần cuối',
    `next_retry_at`           TIMESTAMP                                                                     NULL COMMENT 'Thời điểm thử tiếp theo',

    `request_data`            JSON                                                                          NULL COMMENT 'Dữ liệu request gửi đi',
    `response_data`           JSON                                                                          NULL COMMENT 'Dữ liệu response nhận về',
    `error_code`              VARCHAR(50)                                                                   NULL COMMENT 'Mã lỗi',
    `error_message`           TEXT                                                                          NULL COMMENT 'Chi tiết lỗi',

    `processed_by`            VARCHAR(100)                                                                  NULL COMMENT 'Worker xử lý',
    `processing_started_at`   TIMESTAMP                                                                     NULL COMMENT 'Thời điểm bắt đầu xử lý',
    `processing_completed_at` TIMESTAMP                                                                     NULL COMMENT 'Thời điểm hoàn thành',

    `created_at`              TIMESTAMP                                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Thời điểm tạo',
    `updated_at`              TIMESTAMP                                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Thời điểm cập nhật',

    CONSTRAINT `fk_sync_queue_invoice` FOREIGN KEY (`invoice_id`)
        REFERENCES `invoice_metadata` (`id`) ON DELETE CASCADE,

    CONSTRAINT `chk_sync_attempt_count` CHECK (`attempt_count` <= `max_attempts`),

    INDEX `idx_sync_queue_status` (`status`),
    INDEX `idx_sync_queue_invoice` (`invoice_id`),
    INDEX `idx_sync_queue_merchant` (`invoice_id`),
    INDEX `idx_sync_queue_priority` (`priority`),
    INDEX `idx_sync_queue_next_retry` (`next_retry_at`),
    INDEX `idx_sync_queue_created` (`created_at`),
    INDEX `idx_sync_queue_status_priority` (`status`, `priority`, `next_retry_at`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='Hàng đợi gửi hóa đơn sang Provider/CQT';

-- MANAGEMENT TABLE 5: Tax Authority Responses
CREATE TABLE `tax_authority_responses`
(
    `id`              BIGINT AUTO_INCREMENT PRIMARY KEY,
    `invoice_id`      BIGINT       NOT NULL COMMENT 'Hóa đơn liên quan',
    `cqt_code`        VARCHAR(50)  NOT NULL COMMENT 'Mã Cơ quan Thuế',
    `status_from_cqt` VARCHAR(50)  NOT NULL COMMENT 'Trạng thái từ Cơ quan Thuế',
    `processing_code` VARCHAR(100) NULL COMMENT 'Mã xử lý từ Cơ quan Thuế',
    `signature_data`  TEXT         NULL COMMENT 'Dữ liệu chữ ký từ Cơ quan Thuế',
    `raw_response`    JSON         NULL COMMENT 'Response thô từ Cơ quan Thuế',
    `error_code`      VARCHAR(50)  NULL COMMENT 'Mã lỗi từ Cơ quan Thuế',
    `error_message`   TEXT         NULL COMMENT 'Thông báo lỗi từ Cơ quan Thuế',
    `received_at`     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Thời điểm nhận phản hồi',
    `processed_at`    TIMESTAMP    NULL COMMENT 'Thời điểm xử lý hoàn tất',
    `created_at`      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Thời điểm tạo bản ghi',

    CONSTRAINT `fk_tax_response_invoice` FOREIGN KEY (`invoice_id`)
        REFERENCES `invoice_metadata` (`id`) ON DELETE CASCADE,

    INDEX `idx_tax_response_invoice` (`invoice_id`),
    INDEX `idx_cqt_code` (`cqt_code`),
    INDEX `idx_status_cqt` (`status_from_cqt`),
    INDEX `idx_received_at` (`received_at`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='Lưu vết tương tác với Cơ quan Thuế';

-- MANAGEMENT TABLE 6: System Configuration
CREATE TABLE `system_config`
(
    `id`           BIGINT AUTO_INCREMENT PRIMARY KEY,
    `config_key`   VARCHAR(100) NOT NULL UNIQUE COMMENT 'Khóa cấu hình',
    `config_value` TEXT         NOT NULL COMMENT 'Giá trị cấu hình',
    `config_type`  VARCHAR(20)  NOT NULL DEFAULT 'STRING' COMMENT 'Loại: STRING, NUMBER, BOOLEAN, JSON',
    `description`  VARCHAR(500) NULL COMMENT 'Mô tả cấu hình',
    `is_encrypted` BOOLEAN      NOT NULL DEFAULT FALSE COMMENT 'Có cần mã hóa không',
    `is_editable`  BOOLEAN      NOT NULL DEFAULT TRUE COMMENT 'Có thể chỉnh sửa không',
    `updated_by`   BIGINT       NULL COMMENT 'Người cập nhật cuối',
    `updated_at`   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Thời điểm cập nhật',

    INDEX `idx_config_key` (`config_key`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='Cấu hình hệ thống';
INSERT INTO system_config (config_key, config_value, config_type, description)
VALUES ('SYSTEM_NAME', 'EInvoiceHub', 'STRING', 'Tên hiển thị của hệ thống'),
       ('INVOICE_EXPIRY_DAYS', '30', 'NUMBER', 'Số ngày hóa đơn có hiệu lực sau khi phát hành'),
       ('MAX_RETRY_ATTEMPTS', '3', 'NUMBER', 'Số lần thử lại khi gọi Provider thất bại'),
       ('RETRY_DELAY_MS', '1000', 'NUMBER', 'Thời gian chờ giữa các lần thử lại (milliseconds)'),
       ('SESSION_TIMEOUT_MINUTES', '60', 'NUMBER', 'Thời gian timeout của session'),
       ('PASSWORD_MIN_LENGTH', '8', 'NUMBER', 'Độ dài tối thiểu của mật khẩu'),
       ('API_KEY_PREFIX_LENGTH', '8', 'NUMBER', 'Độ dài prefix hiển thị của API Key'),
       ('DEFAULT_CURRENCY', 'VND', 'STRING', 'Đơn vị tiền tệ mặc định'),
       ('TAX_DEFAULT_RATE', '10.00', 'NUMBER', 'Thuế suất mặc định (%)'),
       ('BACKGROUND_JOB_ENABLED', 'true', 'BOOLEAN', 'Kích hoạt xử lý background jobs'),
       ('INVOICE_LOOKUP_CODE_LENGTH', '6', 'NUMBER', 'Độ dài mã tra cứu hóa đơn ngẫu nhiên');

-- SECTION 4: SYSTEM VIEWS
-- View for user authorization
CREATE OR REPLACE VIEW `vw_sys_user_author` AS
SELECT CAST(id AS CHAR) AS id,
       username         AS name,
       full_name        AS full_name
FROM merchant_user
WHERE is_active = TRUE;

SET FOREIGN_KEY_CHECKS = 1;










-- SCHEMA SUMMARY
/*
Architecture Overview:
======================

CORE JPA TABLES (9 tables - Hierarchical Flow):
-------------------------------------------------
1. merchants              -> Root entity (contains Shell fields: subscription_plan, invoice_quota, updated_at)
2. merchant_user         -> Child of merchants
3. merchant_provider_configs -> Child of merchants (contains Shell fields: is_test_mode, encrypted_password_storage)
4. invoice_template      -> Child of merchants
5. invoice_registrations -> Child of merchants
6. invoice_metadata      -> Central transaction (contains Shell fields: lookup_code, cqt_code, provider_transaction_id)
7. invoice_items         -> Child of invoice_metadata
8. invoice_tax_breakdowns -> Child of invoice_metadata
9. invoice_adjustments   -> Related to invoice_metadata

ENTERPRISE SHELL MODULES (Management Tables):
----------------------------------------------
- invoice_payloads       -> One-to-One with invoice_metadata
- api_credentials        -> INDEX-based (decoupled)
- audit_logs             -> INDEX-based (high-performance)
- invoice_sync_queue     -> Background processing
- tax_authority_responses -> External system integration
- system_config          -> Key-value configuration

Data Flow (Hierarchical - No Reverse Dependencies):
---------------------------------------------------
Merchants -> ProviderConfigs -> Templates -> Invoices -> InvoiceItems
     |
     +-> Users
     +-> Registrations

Key Design Decisions:
---------------------
1. Flat Metadata Strategy: Buyer information stored directly in invoice_metadata
   to prevent circular dependencies when generating historical reports.

2. Hard FK for Core Relations: Ensures data integrity for business-critical
   relationships (Invoice->Items, Invoice->Template, etc.)

3. INDEX for Auxiliary Tables: audit_logs and system_config use INDEX instead
   of FK to maintain high performance and prevent locking issues.

4. NULLABLE Shell Fields: All new enterprise fields are NULLABLE to maintain
   JPA compatibility with existing Spring Boot save() methods.

5. DECIMAL(18,2): Used for all monetary values to ensure precision.

6. utf8mb4_unicode_ci: Full Unicode support including emojis.
*/
