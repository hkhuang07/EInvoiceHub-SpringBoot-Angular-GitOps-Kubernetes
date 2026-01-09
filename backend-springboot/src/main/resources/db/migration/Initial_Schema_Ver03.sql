-- EInvoiceHub Database Schema - Version 3
-- Database: MariaDB   |   Character Set: utf8mb4_unicode_ci

--  I: CÁC BẢNG DANH MỤC (CATALOG TABLES)
-- 1. Invoice Types - Danh mục loại hóa đơn chuẩn TCT
CREATE TABLE invoice_types (
       id BIGINT AUTO_INCREMENT PRIMARY KEY,
       type_code VARCHAR(10) NOT NULL UNIQUE COMMENT 'Mã loại hóa đơn: 01GTKT, 02GTTT, 07KPTQ, 03KPTQ...',
       type_name VARCHAR(100) NOT NULL COMMENT 'Tên loại hóa đơn',
       description TEXT NULL COMMENT 'Mô tả chi tiết',
       is_active BOOLEAN NOT NULL DEFAULT TRUE COMMENT 'Còn hiệu lực',
       display_order INT NOT NULL DEFAULT 0 COMMENT 'Thứ tự hiển thị',
       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Thời điểm tạo',
       updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Thời điểm cập nhật',
       INDEX idx_type_code (type_code),
       INDEX idx_is_active (is_active),
       INDEX idx_display_order (display_order)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Danh mục loại hóa đơn theo quy định TCT';

-- Insert danh mục loại hóa đơn chuẩn
INSERT INTO invoice_types (type_code, type_name, description, display_order) VALUES
     ('01GTKT', 'Hóa đơn giá trị gia tăng', 'Hóa đơn GTGT mẫu 01GTKT theo Thông tư 78/2021/TT-BTC', 1),
     ('02GTTT', 'Hóa đơn bán hàng', 'Hóa đơn bán hàng mẫu 02GTTT theo Thông tư 78/2021/TT-BTC', 2),
     ('07KPTQ', 'Hóa đơn điện tử kiểm soát', 'Hóa đơn điện tử kiểm soát theo Nghị định 123/2020/NĐ-CP', 3),
     ('03KPTQ', 'Hóa đơn điện tử không kiểm soát', 'Hóa đơn điện tử không kiểm soát theo Nghị định 123/2020/NĐ-CP', 4),
     ('04HGDL', 'Hóa đơn điện tử hàng không', 'Hóa đơn điện tử cho dịch vụ hàng không', 5),
     ('01TTDN', 'Hóa đơn điện tử tài chính', 'Hóa đơn điện tử cho tổ chức tín dụng', 6);


-- 2. Payment Methods - Danh mục phương thức thanh toán chuẩn
CREATE TABLE payment_methods (
     id BIGINT AUTO_INCREMENT PRIMARY KEY,
     method_code VARCHAR(10) NOT NULL UNIQUE COMMENT 'Mã phương thức: TM (Tiền mặt), CK (Chuyển khoản), TM/CK...',
     method_name VARCHAR(100) NOT NULL COMMENT 'Tên phương thức thanh toán',
     description TEXT NULL COMMENT 'Mô tả chi tiết',
     is_active BOOLEAN NOT NULL DEFAULT TRUE COMMENT 'Còn hiệu lực',
     display_order INT NOT NULL DEFAULT 0 COMMENT 'Thứ tự hiển thị',
     created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Thời điểm tạo',
     updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Thời điểm cập nhật',

     INDEX idx_method_code (method_code),
     INDEX idx_is_active (is_active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Danh mục phương thức thanh toán chuẩn';

-- Insert danh mục phương thức thanh toán
INSERT INTO payment_methods (method_code, method_name, description, display_order) VALUES
   ('TM', 'Tiền mặt', 'Thanh toán bằng tiền mặt trực tiếp', 1),
   ('CK', 'Chuyển khoản', 'Thanh toán qua chuyển khoản ngân hàng', 2),
   ('TM/CK', 'Tiền mặt/Chuyển khoản', 'Kết hợp cả hai hình thức', 3),
   ('POS', 'Thẻ POS', 'Thanh toán qua máy POS', 4),
   ('VÍ ĐIỆN TỬ', 'Ví điện tử', 'Thanh toán qua ví điện tử (MoMo, ZaloPay, VNPay...)', 5),
   ('THẺ', 'Thẻ ngân hàng', 'Thanh toán bằng thẻ tín dụng/ghi nợ', 6);


-- 3. VAT Rates - Danh mục thuế suất GTGT
CREATE TABLE vat_rates (
   id BIGINT AUTO_INCREMENT PRIMARY KEY,
   rate_code VARCHAR(10) NOT NULL UNIQUE COMMENT 'Mã thuế suất: 0, 5, 8, 10, KCT, KKKNT',
   rate_percent DECIMAL(5,2) NOT NULL COMMENT 'Giá trị phần trăm thuế suất',
   rate_name VARCHAR(100) NOT NULL COMMENT 'Tên thuế suất',
   description TEXT NULL COMMENT 'Mô tả chi tiết',
   is_active BOOLEAN NOT NULL DEFAULT TRUE COMMENT 'Còn hiệu lực',
   display_order INT NOT NULL DEFAULT 0 COMMENT 'Thứ tự hiển thị',
   created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Thời điểm tạo',
   updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Thời điểm cập nhật',
   CONSTRAINT chk_vat_rate_percent CHECK (rate_percent >= 0),
   INDEX idx_rate_code (rate_code),
   INDEX idx_rate_percent (rate_percent),
   INDEX idx_is_active (is_active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Danh mục thuế suất GTGT theo quy định';

-- Insert danh mục thuế suất
INSERT INTO vat_rates (rate_code, rate_percent, rate_name, description, display_order) VALUES
   ('0', 0.00, 'Thuế suất 0%', 'Áp dụng cho hàng hóa, dịch vụ xuất khẩu', 1),
   ('5', 5.00, 'Thuế suất 5%', 'Áp dụng cho hàng hóa, dịch vụ thiết yếu', 2),
   ('8', 8.00, 'Thuế suất 8%', 'Giảm thuế theo Nghị quyết 43/2022/QH15', 3),
   ('10', 10.00, 'Thuế suất 10%', 'Thuế suất thông thường GTGT', 4),
   ('KCT', 0.00, 'Không chịu thuế', 'Hàng hóa, dịch vụ không chịu thuế GTGT', 5),
   ('KKKNT', 0.00, 'Không kê khai, tính nộp', 'Hàng hóa, dịch vụ không kê khai nộp thuế GTGT', 6);


-- II: CÁC BẢNG NGHIỆP VỤ CHÍNH (CORE BUSINESS TABLES)
-- 4. Merchants Table - Quản lý doanh nghiệp (Đã cập nhật)
CREATE TABLE merchants (
   id BIGINT AUTO_INCREMENT PRIMARY KEY,
   tax_code VARCHAR(20) NOT NULL UNIQUE COMMENT 'Mã số thuế - định danh doanh nghiệp',
   company_name VARCHAR(255) NOT NULL COMMENT 'Tên công ty đầy đủ',
   short_name VARCHAR(100) COMMENT 'Tên viết tắt',
   address TEXT COMMENT 'Địa chỉ trụ sở chính',
   district VARCHAR(100) COMMENT 'Quận/Huyện',
   city VARCHAR(100) COMMENT 'Tỉnh/Thành phố',
   email VARCHAR(255) COMMENT 'Email liên hệ',
   phone VARCHAR(20) COMMENT 'Số điện thoại',
   representative_name VARCHAR(100) COMMENT 'Tên người đại diện',
   representative_title VARCHAR(100) COMMENT 'Chức danh người đại diện',
   tax_authority_code VARCHAR(10) NULL COMMENT 'Mã cơ quan thuế quản lý (VD: 01GD, 02HT...)',

   subscription_plan ENUM('TRIAL', 'BASIC', 'PREMIUM') NOT NULL DEFAULT 'TRIAL' COMMENT 'Gói dịch vụ: TRIAL, BASIC, PREMIUM',
   invoice_quota INT NOT NULL DEFAULT 100 COMMENT 'Hạn mức hóa đơn/tháng',
   invoice_used INT NOT NULL DEFAULT 0 COMMENT 'Hóa đơn đã sử dụng trong tháng',
   is_using_hsm BOOLEAN NOT NULL DEFAULT FALSE COMMENT 'Sử dụng HSM hay USB Token để ký điện tử',
   status ENUM('ACTIVE', 'INACTIVE', 'SUSPENDED') NOT NULL DEFAULT 'ACTIVE' COMMENT 'Trạng thái hoạt động',
   is_deleted BOOLEAN NOT NULL DEFAULT FALSE COMMENT 'Cờ xóa mềm',
   deleted_at TIMESTAMP NULL COMMENT 'Thời điểm xóa',
   created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Thời điểm tạo',
   updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Thời điểm cập nhật',

   INDEX idx_tax_code (tax_code),
   INDEX idx_status (status),
   INDEX idx_subscription_plan (subscription_plan),
   INDEX idx_created_at (created_at),
   INDEX idx_tax_authority (tax_authority_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Quản lý doanh nghiệp sử dụng dịch vụ hóa đơn điện tử';


-- 5. Merchant Users - Tài khoản người dùng của Merchant
CREATE TABLE merchant_users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    merchant_id BIGINT NOT NULL COMMENT 'Liên kết với doanh nghiệp',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT 'Tên đăng nhập',
    email VARCHAR(255) NOT NULL UNIQUE COMMENT 'Email người dùng',
    password_hash VARCHAR(255) NOT NULL COMMENT 'Mật khẩu đã mã hóa BCrypt',
    full_name VARCHAR(100) COMMENT 'Họ và tên đầy đủ',
    phone VARCHAR(20) COMMENT 'Số điện thoại',
    avatar_url VARCHAR(500) COMMENT 'URL ảnh đại diện',
    role ENUM('ADMIN', 'MANAGER', 'STAFF', 'VIEWER') NOT NULL DEFAULT 'STAFF' COMMENT 'Vai trò trong hệ thống: ADMIN, MANAGER, STAFF, VIEWER',
    is_active BOOLEAN NOT NULL DEFAULT TRUE COMMENT 'Kích hoạt tài khoản',
    is_primary BOOLEAN NOT NULL DEFAULT FALSE COMMENT 'Tài khoản chính của doanh nghiệp',
    last_login_at TIMESTAMP NULL COMMENT 'Đăng nhập lần cuối',
    failed_login_attempts INT NOT NULL DEFAULT 0 COMMENT 'Số lần đăng nhập sai',
    locked_until TIMESTAMP NULL COMMENT 'Khóa tài khoản đến thời điểm',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Thời điểm tạo',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Thời điểm cập nhật',

    CONSTRAINT fk_merchant_users_merchant FOREIGN KEY (merchant_id)
        REFERENCES merchants(id) ON DELETE CASCADE,

    INDEX idx_merchant_id (merchant_id),
    INDEX idx_username (username),
    INDEX idx_email (email),
    INDEX idx_role (role),
    INDEX idx_is_active (is_active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Tài khoản người dùng thuộc Merchant';

-- 6. API Credentials - Bảo mật và tích hợp API
CREATE TABLE api_credentials (
     id BIGINT AUTO_INCREMENT PRIMARY KEY,
     merchant_id BIGINT NOT NULL COMMENT 'Doanh nghiệp sở hữu credential',
     client_id VARCHAR(50) NOT NULL UNIQUE COMMENT 'ID công khai của ứng dụng',
     api_key_hash VARCHAR(255) NOT NULL COMMENT 'Hash của API Key (SHA-256)',
     api_key_prefix VARCHAR(8) NOT NULL COMMENT '8 ký tự đầu của API Key (hiển thị)',
     scopes TEXT NOT NULL COMMENT 'Quyền hạn - JSON array: [invoice.create, invoice.view]',
     ip_whitelist TEXT NULL COMMENT 'Danh sách IP được phép - JSON array hoặc NULL',
     rate_limit_per_hour INT NOT NULL DEFAULT 1000 COMMENT 'Giới hạn request/giờ',
     rate_limit_per_day INT NOT NULL DEFAULT 10000 COMMENT 'Giới hạn request/ngày',
     request_count_hour INT NOT NULL DEFAULT 0 COMMENT 'Request đã dùng trong giờ',
     request_count_day INT NOT NULL DEFAULT 0 COMMENT 'Request đã dùng trong ngày',
     request_count_resetted_at TIMESTAMP NULL COMMENT 'Thời điểm reset counter',
     expired_at TIMESTAMP NULL COMMENT 'Ngày hết hạn - NULL là không giới hạn',
     last_used_at TIMESTAMP NULL COMMENT 'Lần sử dụng cuối',
     is_active BOOLEAN NOT NULL DEFAULT TRUE COMMENT 'Còn hiệu lực',
     created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Thời điểm tạo',
     updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Thời điểm cập nhật',
     created_by BIGINT NULL COMMENT 'Người tạo - merchant_users.id',

     CONSTRAINT fk_api_credentials_merchant FOREIGN KEY (merchant_id)
         REFERENCES merchants(id) ON DELETE CASCADE,
     CONSTRAINT fk_api_credentials_created_by FOREIGN KEY (created_by)
         REFERENCES merchant_users(id) ON DELETE SET NULL,

     INDEX idx_merchant_id (merchant_id),
     INDEX idx_client_id (client_id),
     INDEX idx_api_key_hash (api_key_hash),
     INDEX idx_expired_at (expired_at),
     INDEX idx_is_active (is_active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Credentials cho API access';


-- 7. Service Providers - Nhà cung cấp hóa đơn điện tử
CREATE TABLE service_providers (
       id BIGINT AUTO_INCREMENT PRIMARY KEY,
       provider_code VARCHAR(20) NOT NULL UNIQUE COMMENT 'Mã provider: VNPT, VIETTEL, MISA, BKAV',
       provider_name VARCHAR(100) NOT NULL COMMENT 'Tên đầy đủ nhà cung cấp',
       official_api_url VARCHAR(500) NOT NULL COMMENT 'URL API chính thức',
       documentation_url VARCHAR(500) NULL COMMENT 'Link tài liệu API',
       support_email VARCHAR(255) NULL COMMENT 'Email hỗ trợ',
       support_phone VARCHAR(20) NULL COMMENT 'Hotline hỗ trợ',
       is_active BOOLEAN NOT NULL DEFAULT TRUE COMMENT 'Provider còn hoạt động',
       is_default BOOLEAN NOT NULL DEFAULT FALSE COMMENT 'Provider mặc định của hệ thống',
       display_order INT NOT NULL DEFAULT 0 COMMENT 'Thứ tự hiển thị',
       extra_config_schema JSON NULL COMMENT 'Schema cấu hình bổ sung cho provider',
       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Thời điểm tạo',
       updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Thời điểm cập nhật',

       INDEX idx_provider_code (provider_code),
       INDEX idx_is_active (is_active),
       INDEX idx_is_default (is_default),
       INDEX idx_display_order (display_order)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Danh mục nhà cung cấp hóa đơn điện tử';

-- Insert default providers
INSERT INTO service_providers (provider_code, provider_name, official_api_url, is_active, is_default, display_order) VALUES
                                                                                                               ('BKAV', 'BKAV eInvoice', 'https://einvoice.bkav.com/api/v1', FALSE, FALSE, 4);
-- 8. Merchant Provider Configs - Cấu hình riêng của Merchant
CREATE TABLE merchant_provider_configs (
       id BIGINT AUTO_INCREMENT PRIMARY KEY,
       merchant_id BIGINT NOT NULL COMMENT 'Doanh nghiệp sở hữu',
       provider_id BIGINT NOT NULL COMMENT 'Nhà cung cấp được chọn',
       username_service VARCHAR(100) NOT NULL COMMENT 'Tài khoản API do Provider cấp',
       password_service_encrypted VARCHAR(500) NOT NULL COMMENT 'Mật khẩu API - đã mã hóa AES-256',
       certificate_serial VARCHAR(100) NULL COMMENT 'Serial number của chứng thư số',
       certificate_data TEXT NULL COMMENT 'Chứng thư số - đã mã hóa',
       certificate_chain LONGTEXT NULL COMMENT 'Chuỗi chứng thư số đầy đủ (Certificate Chain)',
       certificate_expired_at TIMESTAMP NULL COMMENT 'Hạn chứng thư số',
       extra_config JSON NULL COMMENT 'Cấu hình bổ sung: pattern, serial, template',
       is_active BOOLEAN NOT NULL DEFAULT TRUE COMMENT 'Cấu hình còn hiệu lực',
       is_default BOOLEAN NOT NULL DEFAULT FALSE COMMENT 'Provider mặc định của Merchant',
       last_sync_at TIMESTAMP NULL COMMENT 'Lần đồng bộ cuối với Provider',
       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Thời điểm tạo',
       updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Thời điểm cập nhật',

       CONSTRAINT fk_mpc_merchant FOREIGN KEY (merchant_id)
           REFERENCES merchants(id) ON DELETE CASCADE,
       CONSTRAINT fk_mpc_provider FOREIGN KEY (provider_id)
           REFERENCES service_providers(id) ON DELETE RESTRICT,
       CONSTRAINT uq_merchant_provider UNIQUE (merchant_id, provider_id),

       INDEX idx_merchant_id (merchant_id),
       INDEX idx_provider_id (provider_id),
       INDEX idx_is_active (is_active),
       INDEX idx_is_default (is_default)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Cấu hình kết nối Provider cho từng Merchant';


-- 9. Invoice Registrations - Quản lý đăng ký dải số hóa đơn với CQT
CREATE TABLE invoice_registrations (
   id BIGINT AUTO_INCREMENT PRIMARY KEY,
   merchant_id BIGINT NOT NULL COMMENT 'Doanh nghiệp sở hữu',
   provider_id BIGINT NULL COMMENT 'Provider phát hành (nếu có)',
   invoice_template_id BIGINT NULL COMMENT 'Mẫu hóa đơn liên kết',

   registration_number VARCHAR(50) NULL COMMENT 'Số quyết định/phê duyệt của CQT',
   from_number BIGINT NOT NULL COMMENT 'Số bắt đầu của dải (VD: 0000001)',
   to_number BIGINT NOT NULL COMMENT 'Số kết thúc của dải (VD: 0001000)',
   quantity BIGINT NOT NULL COMMENT 'Số lượng hóa đơn trong dải',
   current_number BIGINT NOT NULL DEFAULT 0 COMMENT 'Số hiện tại đã sử dụng',

   effective_date DATE NOT NULL COMMENT 'Ngày có hiệu lực',
   expiration_date DATE NULL COMMENT 'Ngày hết hạn (nếu có)',

   status ENUM('ACTIVE', 'EXHAUSTED', 'EXPIRED', 'CANCELLED') NOT NULL DEFAULT 'ACTIVE' COMMENT 'Trạng thái đăng ký',
   issued_by VARCHAR(255) NULL COMMENT 'Người phê duyệt/CQT',
   note TEXT NULL COMMENT 'Ghi chú bổ sung',

   created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Thời điểm tạo',
   updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Thời điểm cập nhật',

   CONSTRAINT fk_registration_merchant FOREIGN KEY (merchant_id)
       REFERENCES merchants(id) ON DELETE RESTRICT,
   CONSTRAINT fk_registration_provider FOREIGN KEY (provider_id)
       REFERENCES service_providers(id) ON DELETE SET NULL,
   CONSTRAINT fk_registration_template FOREIGN KEY (invoice_template_id)
       REFERENCES invoice_templates(id) ON DELETE SET NULL,
   CONSTRAINT chk_registration_range CHECK (from_number <= to_number),
   CONSTRAINT chk_registration_quantity CHECK (quantity = (to_number - from_number + 1)),

   INDEX idx_registration_merchant (merchant_id),
   INDEX idx_registration_status (status),
   INDEX idx_registration_effective (effective_date),
   INDEX idx_registration_expiration (expiration_date),
   INDEX idx_registration_number (registration_number)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Quản lý lịch sử đăng ký dải số hóa đơn với Cơ quan Thuế';


-- 10. Invoice Templates - Quản lý Mẫu số & Ký hiệu hóa đơn
CREATE TABLE invoice_templates (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    merchant_id BIGINT NOT NULL COMMENT 'Doanh nghiệp sở hữu mẫu',
    provider_id BIGINT NOT NULL COMMENT 'Nhà cung cấp hóa đơn',
    invoice_type_id BIGINT NULL COMMENT 'Loại hóa đơn (liên kết bảng invoice_types)',
    invoice_registration_id BIGINT NULL COMMENT 'Đăng ký dải số liên kết',

    template_code VARCHAR(20) NOT NULL COMMENT 'Mẫu hóa đơn: 01GTKT0/001, 02GTTT0/001...',
    symbol_code VARCHAR(10) NOT NULL COMMENT 'Ký hiệu hóa đơn: AA/22E, AB/23T...',
    current_number INT NOT NULL DEFAULT 0 COMMENT 'Số hiện hóa đơn hiện tại',
    prefix VARCHAR(20) NULL COMMENT 'Tiền tố số hóa đơn',
    suffix VARCHAR(20) NULL COMMENT 'Hậu tố số hóa đơn',
    min_number INT NOT NULL DEFAULT 1 COMMENT 'Số bắt đầu',
    max_number INT NOT NULL DEFAULT 99999 COMMENT 'Số kết thúc',

    CONSTRAINT chk_symbol_code_format CHECK (symbol_code REGEXP '^[A-Z0-9]{2,7}/[0-9]{2,4}[A-Z0-9]*$'),

    is_active BOOLEAN NOT NULL DEFAULT TRUE COMMENT 'Mẫu còn hiệu lực',
    is_sequential BOOLEAN NOT NULL DEFAULT TRUE COMMENT 'Sử dụng số thứ tự liên tục',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Thời điểm tạo',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Thời điểm cập nhật',

    CONSTRAINT fk_invoice_template_merchant FOREIGN KEY (merchant_id)
        REFERENCES merchants(id) ON DELETE CASCADE,
    CONSTRAINT fk_invoice_template_provider FOREIGN KEY (provider_id)
        REFERENCES service_providers(id) ON DELETE RESTRICT,
    CONSTRAINT fk_invoice_template_type FOREIGN KEY (invoice_type_id)
        REFERENCES invoice_types(id) ON DELETE SET NULL,
    CONSTRAINT fk_invoice_template_registration FOREIGN KEY (invoice_registration_id)
        REFERENCES invoice_registrations(id) ON DELETE SET NULL,
    CONSTRAINT uq_merchant_template_symbol UNIQUE (merchant_id, template_code, symbol_code),

    INDEX idx_template_merchant (merchant_id),
    INDEX idx_template_code (template_code),
    INDEX idx_symbol_code (symbol_code),
    INDEX idx_invoice_type (invoice_type_id),
    INDEX idx_is_active (is_active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Quản lý mẫu số và ký hiệu hóa đơn theo quy định';


-- 11. Customers - Danh mục khách hàng của Merchant
CREATE TABLE customers (
   id BIGINT AUTO_INCREMENT PRIMARY KEY,
   merchant_id BIGINT NOT NULL COMMENT 'Doanh nghiệp sở hữu khách hàng',
   customer_code VARCHAR(50) NULL COMMENT 'Mã khách hàng nội bộ',
   tax_code VARCHAR(20) NULL COMMENT 'Mã số thuế khách hàng',
   name VARCHAR(255) NOT NULL COMMENT 'Tên khách hàng/đơn vị',
   address TEXT NULL COMMENT 'Địa chỉ',
   email VARCHAR(255) NULL COMMENT 'Email nhận hóa đơn',
   phone VARCHAR(20) NULL COMMENT 'Điện thoại',
   contact_person VARCHAR(100) NULL COMMENT 'Người liên hệ',
   bank_account VARCHAR(50) NULL COMMENT 'Tài khoản ngân hàng',
   bank_name VARCHAR(100) NULL COMMENT 'Tên ngân hàng',
   note TEXT NULL COMMENT 'Ghi chú',
   is_active BOOLEAN NOT NULL DEFAULT TRUE COMMENT 'Còn hoạt động',
   created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Thời điểm tạo',
   updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Thời điểm cập nhật',

   CONSTRAINT fk_customer_merchant FOREIGN KEY (merchant_id)
       REFERENCES merchants(id) ON DELETE CASCADE,

   INDEX idx_customer_merchant (merchant_id),
   INDEX idx_customer_code (customer_code),
   INDEX idx_tax_code (tax_code),
   INDEX idx_name (name),
   INDEX idx_email (email),
   INDEX idx_is_active (is_active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Danh mục khách hàng thường xuyên của Merchant';


-- 12. Invoices Metadata - Thông tin tóm tắt hóa đơn
CREATE TABLE invoices_metadata (
   id BIGINT AUTO_INCREMENT PRIMARY KEY,
   merchant_id BIGINT NOT NULL COMMENT 'Doanh nghiệp phát hành',
   provider_id BIGINT NULL COMMENT 'Provider xử lý hóa đơn',
   provider_config_id BIGINT NULL COMMENT 'Cấu hình Provider được sử dụng',
   invoice_template_id BIGINT NULL COMMENT 'Mẫu hóa đơn được sử dụng',
   invoice_registration_id BIGINT NULL COMMENT 'Đăng ký dải số được sử dụng',
   client_request_id VARCHAR(100) NULL COMMENT 'ID request từ Merchant',

   invoice_number VARCHAR(20) NULL COMMENT 'Số hóa đơn từ Provider trả về',
   symbol_code VARCHAR(10) NULL COMMENT 'Ký hiệu hóa đơn (VD: 1C23TML)',
   template_code VARCHAR(20) NULL COMMENT 'Mẫu hóa đơn: 01GTKT0/001, 02GTTT0/001...',
   is_summary_invoice BOOLEAN NOT NULL DEFAULT FALSE COMMENT 'Hóa đơn tổng hợp hay hóa đơn chi tiết',

   cqt_code VARCHAR(50) NULL COMMENT 'Mã Cơ quan Thuế quản lý',

   payment_method VARCHAR(10) NULL COMMENT 'Mã phương thức thanh toán (liên kết bảng payment_methods)',
   lookup_code VARCHAR(50) NULL COMMENT 'Mã tra cứu hóa đơn cho khách lẻ (pattern: Số hóa đơn + Mã tra cứu)',
   issuance_method ENUM('STANDARD', 'POS') NOT NULL DEFAULT 'STANDARD' COMMENT 'Phương thức phát hành: STANDARD (thông thường), POS (máy tính tiền)',

   seller_name VARCHAR(255) NULL COMMENT 'Tên người bán',
   seller_tax_code VARCHAR(20) NULL COMMENT 'Mã số thuế người bán',
   seller_address TEXT NULL COMMENT 'Địa chỉ người bán',

   buyer_name VARCHAR(255) NULL COMMENT 'Tên người mua',
   buyer_tax_code VARCHAR(20) NULL COMMENT 'Mã số thuế người mua',
   buyer_email VARCHAR(255) NULL COMMENT 'Email người mua (nhận hóa đơn)',
   buyer_phone VARCHAR(20) NULL COMMENT 'Điện thoại người mua',
   buyer_address TEXT NULL COMMENT 'Địa chỉ người mua',
   customer_id BIGINT NULL COMMENT 'Liên kết với bảng customers',

   subtotal_amount DECIMAL(18,2) NOT NULL DEFAULT 0.00 COMMENT 'Tổng tiền trước thuế',
   tax_amount DECIMAL(18,2) NOT NULL DEFAULT 0.00 COMMENT 'Tổng tiền thuế',
   discount_amount DECIMAL(18,2) NOT NULL DEFAULT 0.00 COMMENT 'Tổng chiết khấu',
   total_amount DECIMAL(18,2) NOT NULL DEFAULT 0.00 COMMENT 'Tổng tiền thanh toán',
   currency_code VARCHAR(3) NOT NULL DEFAULT 'VND' COMMENT 'Đơn vị tiền tệ',
   exchange_rate DECIMAL(10,4) NOT NULL DEFAULT 1.0000 COMMENT 'Tỷ giá với VND',

   issue_date DATE NULL COMMENT 'Ngày lập hóa đơn',
   accounting_date DATE NULL COMMENT 'Ngày hạch toán',
   due_date DATE NULL COMMENT 'Ngày thanh toán đến hạn',

   status ENUM('DRAFT', 'PENDING', 'SIGNING', 'SENT_TO_PROVIDER', 'SUCCESS', 'FAILED', 'CANCELLED', 'REPLACED') NOT NULL DEFAULT 'DRAFT' COMMENT 'Trạng thái xử lý hóa đơn',
   status_message TEXT NULL COMMENT 'Thông báo trạng thái chi tiết',
   cancellation_reason VARCHAR(500) NULL COMMENT 'Lý do hủy/thay thế',
   replaced_by_invoice_id BIGINT NULL COMMENT 'ID hóa đơn thay thế',
   adjustment_type ENUM('ADJUSTMENT', 'REPLACEMENT', 'CANCELLATION') NULL COMMENT 'Loại điều chỉnh/thay thế/hủy',

   signed_at TIMESTAMP NULL COMMENT 'Thời điểm ký điện tử',
   sent_to_provider_at TIMESTAMP NULL COMMENT 'Thời điểm gửi sang Provider',
   received_from_provider_at TIMESTAMP NULL COMMENT 'Thời điểm nhận phản hồi',
   delivered_to_buyer_at TIMESTAMP NULL COMMENT 'Thời điểm gửi hóa đơn cho người mua',

   provider_transaction_code VARCHAR(100) NULL COMMENT 'Mã giao dịch từ Provider',
   provider_error_code VARCHAR(50) NULL COMMENT 'Mã lỗi từ Provider',
   provider_error_message TEXT NULL COMMENT 'Chi tiết lỗi từ Provider',

   is_deleted BOOLEAN NOT NULL DEFAULT FALSE COMMENT 'Hóa đơn đã bị xóa',
   deleted_at TIMESTAMP NULL COMMENT 'Thời điểm xóa',
   deleted_by BIGINT NULL COMMENT 'Người xóa - merchant_users.id',

   created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Thời điểm tạo',
   updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Thời điểm cập nhật',

   CONSTRAINT fk_invoice_merchant FOREIGN KEY (merchant_id)
       REFERENCES merchants(id) ON DELETE RESTRICT,
   CONSTRAINT fk_invoice_provider FOREIGN KEY (provider_id)
       REFERENCES service_providers(id) ON DELETE SET NULL,
   CONSTRAINT fk_invoice_config FOREIGN KEY (provider_config_id)
       REFERENCES merchant_provider_configs(id) ON DELETE SET NULL,
   CONSTRAINT fk_invoice_template FOREIGN KEY (invoice_template_id)
       REFERENCES invoice_templates(id) ON DELETE SET NULL,
   CONSTRAINT fk_invoice_registration FOREIGN KEY (invoice_registration_id)
       REFERENCES invoice_registrations(id) ON DELETE SET NULL,
   CONSTRAINT fk_invoice_customer FOREIGN KEY (customer_id)
       REFERENCES customers(id) ON DELETE SET NULL,
   CONSTRAINT fk_invoice_payment_method FOREIGN KEY (payment_method)
       REFERENCES payment_methods(method_code) ON DELETE SET NULL,
   CONSTRAINT fk_invoice_replaced_by FOREIGN KEY (replaced_by_invoice_id)
       REFERENCES invoices_metadata(id) ON DELETE SET NULL,
   CONSTRAINT fk_invoice_deleted_by FOREIGN KEY (deleted_by)
       REFERENCES merchant_users(id) ON DELETE SET NULL,
   CONSTRAINT uq_invoice_lookup_code UNIQUE (lookup_code) COMMENT 'Mã tra cứu phải duy nhất',

   INDEX idx_merchant_id (merchant_id),
   INDEX idx_provider_id (provider_id),
   INDEX idx_invoice_number (invoice_number),
   INDEX idx_symbol_code (symbol_code),
   INDEX idx_buyer_tax_code (buyer_tax_code),
   INDEX idx_status (status),
   INDEX idx_issue_date (issue_date),
   INDEX idx_created_at (created_at),
   INDEX idx_client_request_id (client_request_id),
   INDEX idx_merchant_status (merchant_id, status),
   INDEX idx_merchant_created (merchant_id, created_at),
   INDEX idx_cqt_code (cqt_code),
   INDEX idx_invoice_template (invoice_template_id),
   INDEX idx_lookup_code (lookup_code),
   INDEX idx_payment_method (payment_method)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Metadata hóa đơn - phục vụ tra cứu nhanh';


-- 13. Invoice Items - Chi tiết hàng hóa/dịch vụ trong hóa đơn (Đã cập nhật)
CREATE TABLE invoice_items (
   id BIGINT AUTO_INCREMENT PRIMARY KEY,
   invoice_id BIGINT NOT NULL COMMENT 'ID hóa đơn cha',
   line_number INT NOT NULL DEFAULT 1 COMMENT 'Số thứ tự dòng',
   product_name VARCHAR(500) NOT NULL COMMENT 'Tên sản phẩm/dịch vụ',
   product_code VARCHAR(100) NULL COMMENT 'Mã sản phẩm theo danh mục',
   unit_name VARCHAR(50) NOT NULL COMMENT 'Đơn vị tính: cái, kg, chiếc...',
   quantity DECIMAL(18,4) NOT NULL DEFAULT 0 COMMENT 'Số lượng',
   unit_price DECIMAL(18,4) NOT NULL DEFAULT 0 COMMENT 'Đơn giá trước thuế',
   amount DECIMAL(18,2) NOT NULL DEFAULT 0 COMMENT 'Thành tiền = quantity * unit_price',
   discount_rate DECIMAL(5,2) NOT NULL DEFAULT 0 COMMENT 'Tỷ lệ chiết khấu (%)',
   discount_amount DECIMAL(18,2) NOT NULL DEFAULT 0 COMMENT 'Tiền chiết khấu',
   tax_rate DECIMAL(5,2) NOT NULL DEFAULT 0 COMMENT 'Thuế suất GTGT (%)',
   tax_amount DECIMAL(18,2) NOT NULL DEFAULT 0 COMMENT 'Tiền thuế GTGT',
   total_amount DECIMAL(18,2) NOT NULL DEFAULT 0 COMMENT 'Thành tiền bao gồm thuế',
   description TEXT NULL COMMENT 'Mô tả chi tiết sản phẩm',

   tax_category_code VARCHAR(20) NULL COMMENT 'Mã nhóm hàng hóa theo thuế',
   vat_rate_id BIGINT NULL COMMENT 'Liên kết với danh mục thuế suất (vat_rates)',
   is_promotion BOOLEAN NOT NULL DEFAULT FALSE COMMENT 'Hàng khuyến mãi (TRUE) hay hàng thường (FALSE)',

   created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Thời điểm tạo',

   CONSTRAINT fk_invoice_item_invoice FOREIGN KEY (invoice_id)
       REFERENCES invoices_metadata(id) ON DELETE CASCADE,
   CONSTRAINT fk_invoice_item_vat_rate FOREIGN KEY (vat_rate_id)
       REFERENCES vat_rates(id) ON DELETE SET NULL,

   INDEX idx_item_invoice (invoice_id),
   INDEX idx_line_number (line_number),
   INDEX idx_vat_rate (vat_rate_id),
   INDEX idx_is_promotion (is_promotion)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Chi tiết từng dòng hàng hóa/dịch vụ trong hóa đơn';


-- 14. Invoice Tax Breakdowns - Phân rã thuế suất trên từng hóa đơn
CREATE TABLE invoice_tax_breakdowns (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    invoice_id BIGINT NOT NULL COMMENT 'ID hóa đơn cha',
    vat_rate_id BIGINT NOT NULL COMMENT 'Liên kết với danh mục thuế suất',
    vat_rate_code VARCHAR(10) NOT NULL COMMENT 'Mã thuế suất (0, 5, 8, 10, KCT, KKKNT)',
    vat_rate_percent DECIMAL(5,2) NOT NULL COMMENT 'Giá trị phần trăm thuế suất',

    subtotal_amount DECIMAL(18,2) NOT NULL DEFAULT 0.00 COMMENT 'Tổng tiền trước thuế với thuế suất này',
    discount_amount DECIMAL(18,2) NOT NULL DEFAULT 0.00 COMMENT 'Tổng tiền chiết khấu',
    taxable_amount DECIMAL(18,2) NOT NULL DEFAULT 0.00 COMMENT 'Tổng tiền chịu thuế (sau chiết khấu)',
    tax_amount DECIMAL(18,2) NOT NULL DEFAULT 0.00 COMMENT 'Tổng tiền thuế GTGT',
    total_amount DECIMAL(18,2) NOT NULL DEFAULT 0.00 COMMENT 'Tổng tiền bao gồm thuế',

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Thời điểm tạo',

    CONSTRAINT fk_tax_breakdown_invoice FOREIGN KEY (invoice_id)
        REFERENCES invoices_metadata(id) ON DELETE CASCADE,
    CONSTRAINT fk_tax_breakdown_vat_rate FOREIGN KEY (vat_rate_id)
        REFERENCES vat_rates(id) ON DELETE RESTRICT,
    CONSTRAINT uq_invoice_vat_rate UNIQUE (invoice_id, vat_rate_id),

    INDEX idx_breakdown_invoice (invoice_id),
    INDEX idx_breakdown_vat_rate (vat_rate_id),
    INDEX idx_vat_rate_code (vat_rate_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Lưu tổng tiền tóm tắt theo từng loại thuế suất phục vụ in ấn hóa đơn';


CREATE TABLE invoice_adjustments (
     id BIGINT AUTO_INCREMENT PRIMARY KEY,
     original_invoice_id BIGINT NOT NULL COMMENT 'Hóa đơn gốc bị điều chỉnh/thay thế/hủy',
     adjustment_type ENUM('ADJUSTMENT', 'REPLACEMENT', 'CANCELLATION') NOT NULL COMMENT 'Loại điều chỉnh',

     agreement_number VARCHAR(50) NULL COMMENT 'Số biên bản thỏa thuận',
     agreement_date DATE NULL COMMENT 'Ngày lập biên bản',
     agreement_content TEXT NULL COMMENT 'Nội dung biên bản thỏa thuận',
     signers TEXT NULL COMMENT 'Người ký biên bản (JSON array)',

     reason_code VARCHAR(50) NULL COMMENT 'Mã lý do (theo quy định)',
     reason_description VARCHAR(500) NULL COMMENT 'Mô tả lý do chi tiết',

     old_subtotal_amount DECIMAL(18,2) NULL COMMENT 'Tổng tiền trước thuế cũ',
     old_tax_amount DECIMAL(18,2) NULL COMMENT 'Tiền thuế cũ',
     old_total_amount DECIMAL(18,2) NULL COMMENT 'Tổng tiền cũ',
     new_subtotal_amount DECIMAL(18,2) NULL COMMENT 'Tổng tiền trước thuế mới',
     new_tax_amount DECIMAL(18,2) NULL COMMENT 'Tiền thuế mới',
     new_total_amount DECIMAL(18,2) NULL COMMENT 'Tổng tiền mới',
     difference_amount DECIMAL(18,2) NULL COMMENT 'Chênh lệch tiền',

     status ENUM('PENDING', 'APPROVED', 'REJECTED', 'CANCELLED') NOT NULL DEFAULT 'PENDING' COMMENT 'Trạng thái duyệt',
     approved_by BIGINT NULL COMMENT 'Người duyệt - merchant_users.id',
     approved_at TIMESTAMP NULL COMMENT 'Thời điểm duyệt',

    -- Thông tin gửi CQT
     submitted_to_cqt BOOLEAN NOT NULL DEFAULT FALSE COMMENT 'Đã gửi Cơ quan Thuế',
     cqt_response_code VARCHAR(50) NULL COMMENT 'Mã phản hồi từ CQT',
     cqt_response_message TEXT NULL COMMENT 'Nội dung phản hồi từ CQT',

     created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Thời điểm tạo',
     updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Thời điểm cập nhật',

     CONSTRAINT fk_adjustment_original_invoice FOREIGN KEY (original_invoice_id)
         REFERENCES invoices_metadata(id) ON DELETE RESTRICT,
     CONSTRAINT fk_adjustment_approved_by FOREIGN KEY (approved_by)
         REFERENCES merchant_users(id) ON DELETE SET NULL,

     INDEX idx_adjustment_original (original_invoice_id),
     INDEX idx_adjustment_type (adjustment_type),
     INDEX idx_adjustment_status (status),
     INDEX idx_agreement_date (agreement_date),
     INDEX idx_submitted_cqt (submitted_to_cqt)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Lưu thông tin biên bản thỏa thuận cho hóa đơn điều chỉnh/thay thế/hủy';


CREATE TABLE invoice_sync_queue (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    invoice_id BIGINT NOT NULL COMMENT 'ID hóa đơn cần đồng bộ',
    merchant_id BIGINT NOT NULL COMMENT 'Doanh nghiệp sở hữu',
    provider_id BIGINT NULL COMMENT 'Provider đích (nếu có)',

    sync_type ENUM('SEND_TO_PROVIDER', 'SEND_TO_CQT', 'CANCEL_INVOICE', 'REPLACE_INVOICE') NOT NULL COMMENT 'Loại thao tác đồng bộ',
    priority INT NOT NULL DEFAULT 5 COMMENT 'Độ ưu tiên (1=cao nhất, 10=thấp nhất)',

    status ENUM('PENDING', 'PROCESSING', 'COMPLETED', 'FAILED', 'RETRYING') NOT NULL DEFAULT 'PENDING' COMMENT 'Trạng thái xử lý',
    attempt_count INT NOT NULL DEFAULT 0 COMMENT 'Số lần thử',
    max_attempts INT NOT NULL DEFAULT 3 COMMENT 'Số lần thử tối đa',
    last_attempt_at TIMESTAMP NULL COMMENT 'Thời điểm thử lần cuối',
    next_retry_at TIMESTAMP NULL COMMENT 'Thời điểm thử tiếp theo',

    request_data JSON NULL COMMENT 'Dữ liệu request gửi đi',
    response_data JSON NULL COMMENT 'Dữ liệu response nhận về',
    error_code VARCHAR(50) NULL COMMENT 'Mã lỗi',
    error_message TEXT NULL COMMENT 'Chi tiết lỗi',

    processed_by VARCHAR(100) NULL COMMENT 'Worker xử lý',
    processing_started_at TIMESTAMP NULL COMMENT 'Thời điểm bắt đầu xử lý',
    processing_completed_at TIMESTAMP NULL COMMENT 'Thời điểm hoàn thành',

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Thời điểm tạo',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Thời điểm cập nhật',

    CONSTRAINT fk_sync_queue_invoice FOREIGN KEY (invoice_id)
        REFERENCES invoices_metadata(id) ON DELETE CASCADE,
    CONSTRAINT fk_sync_queue_merchant FOREIGN KEY (merchant_id)
        REFERENCES merchants(id) ON DELETE CASCADE,
    CONSTRAINT fk_sync_queue_provider FOREIGN KEY (provider_id)
        REFERENCES service_providers(id) ON DELETE SET NULL,
    CONSTRAINT chk_sync_attempt_count CHECK (attempt_count <= max_attempts),

    INDEX idx_sync_queue_status (status),
    INDEX idx_sync_queue_invoice (invoice_id),
    INDEX idx_sync_queue_merchant (merchant_id),
    INDEX idx_sync_queue_priority (priority),
    INDEX idx_sync_queue_next_retry (next_retry_at),
    INDEX idx_sync_queue_created (created_at),
    INDEX idx_sync_queue_status_priority (status, priority, next_retry_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Hàng đợi gửi hóa đơn sang Provider/CQT với cơ chế Retry';


-- 17. Invoice Payloads - Lưu trữ nội dung XML/JSON gốc
CREATE TABLE invoice_payloads (
      invoice_id BIGINT NOT NULL PRIMARY KEY COMMENT 'ID hóa đơn - khóa chính và khóa ngoại',
      raw_data JSON NULL COMMENT 'Dữ liệu thô request/response dạng JSON',
      xml_content LONGTEXT NULL COMMENT 'Nội dung XML gốc của hóa đơn (phục vụ in ấn)',
      json_content LONGTEXT NULL COMMENT 'Nội dung JSON đầy đủ của hóa đơn',
      signed_xml LONGTEXT NULL COMMENT 'XML đã ký điện tử (chữ ký số)',
      pdf_data LONGTEXT NULL COMMENT 'Dữ liệu PDF đã tạo (base64)',
      extra_data JSON NULL COMMENT 'Dữ liệu bổ sung khác',
      created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Thời điểm lưu',
      updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Thời điểm cập nhật',

      CONSTRAINT fk_invoice_payload_invoice FOREIGN KEY (invoice_id)
          REFERENCES invoices_metadata(id) ON DELETE CASCADE,

      INDEX idx_payload_created (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Lưu trữ nội dung XML/JSON gốc hóa đơn';


-- 18. Tax Authority Responses - Phản hồi từ Cơ quan Thuế
CREATE TABLE tax_authority_responses (
     id BIGINT AUTO_INCREMENT PRIMARY KEY,
     invoice_id BIGINT NOT NULL COMMENT 'Hóa đơn liên quan',
     cqt_code VARCHAR(50) NOT NULL COMMENT 'Mã Cơ quan Thuế',
     status_from_cqt VARCHAR(50) NOT NULL COMMENT 'Trạng thái từ Cơ quan Thuế: APPROVED, REJECTED, PENDING',
     processing_code VARCHAR(100) NULL COMMENT 'Mã xử lý từ Cơ quan Thuế',
     signature_data TEXT NULL COMMENT 'Dữ liệu chữ ký từ Cơ quan Thuế',
     raw_response JSON NULL COMMENT 'Response thô từ Cơ quan Thuế',
     error_code VARCHAR(50) NULL COMMENT 'Mã lỗi từ Cơ quan Thuế',
     error_message TEXT NULL COMMENT 'Thông báo lỗi từ Cơ quan Thuế',
     received_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Thời điểm nhận phản hồi',
     processed_at TIMESTAMP NULL COMMENT 'Thời điểm xử lý hoàn tất',
     created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Thời điểm tạo bản ghi',

     CONSTRAINT fk_tax_response_invoice FOREIGN KEY (invoice_id)
         REFERENCES invoices_metadata(id) ON DELETE CASCADE,

     INDEX idx_tax_response_invoice (invoice_id),
     INDEX idx_cqt_code (cqt_code),
     INDEX idx_status_cqt (status_from_cqt),
     INDEX idx_received_at (received_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Lưu vết tương tác với Cơ quan Thuế';


-- 19. Audit Log - Nhật ký thao tác
CREATE TABLE audit_logs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    merchant_id BIGINT NULL COMMENT 'Doanh nghiệp liên quan',
    user_id BIGINT NULL COMMENT 'Người thực hiện',
    entity_type VARCHAR(50) NOT NULL COMMENT 'Loại entity: Invoice, Merchant, Config...',
    entity_id BIGINT NOT NULL COMMENT 'ID của entity',
    action VARCHAR(50) NOT NULL COMMENT 'Hành động: CREATE, UPDATE, DELETE, STATUS_CHANGE',
    old_value JSON NULL COMMENT 'Giá trị cũ',
    new_value JSON NULL COMMENT 'Giá trị mới',
    ip_address VARCHAR(45) NULL COMMENT 'IP thực hiện',
    user_agent VARCHAR(500) NULL COMMENT 'User Agent của browser/client',
    request_id VARCHAR(100) NULL COMMENT 'ID request từ frontend',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Thời điểm xảy ra',

    INDEX idx_merchant_id (merchant_id),
    INDEX idx_user_id (user_id),
    INDEX idx_entity (entity_type, entity_id),
    INDEX idx_action (action),
    INDEX idx_created_at (created_at),
    INDEX idx_merchant_entity (merchant_id, entity_type, entity_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Nhật ký thay đổi hệ thống';


-- 20. System Configuration - Cấu hình hệ thống
CREATE TABLE system_config (
   id BIGINT AUTO_INCREMENT PRIMARY KEY,
   config_key VARCHAR(100) NOT NULL UNIQUE COMMENT 'Khóa cấu hình',
   config_value TEXT NOT NULL COMMENT 'Giá trị cấu hình',
   config_type VARCHAR(20) NOT NULL DEFAULT 'STRING' COMMENT 'Loại: STRING, NUMBER, BOOLEAN, JSON',
   description VARCHAR(500) NULL COMMENT 'Mô tả cấu hình',
   is_encrypted BOOLEAN NOT NULL DEFAULT FALSE COMMENT 'Có cần mã hóa không',
   is_editable BOOLEAN NOT NULL DEFAULT TRUE COMMENT 'Có thể chỉnh sửa không',
   updated_by BIGINT NULL COMMENT 'Người cập nhật cuối',
   updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Thời điểm cập nhật',

   CONSTRAINT fk_config_updated_by FOREIGN KEY (updated_by)
       REFERENCES merchant_users(id) ON DELETE SET NULL,

   INDEX idx_config_key (config_key)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Cấu hình hệ thống';

-- Insert default configurations
INSERT INTO system_config (config_key, config_value, config_type, description) VALUES
('SYSTEM_NAME', 'EInvoiceHub', 'STRING', 'Tên hiển thị của hệ thống'),
('INVOICE_EXPIRY_DAYS', '30', 'NUMBER', 'Số ngày hóa đơn có hiệu lực sau khi phát hành'),
('MAX_RETRY_ATTEMPTS', '3', 'NUMBER', 'Số lần thử lại khi gọi Provider thất bại'),
('RETRY_DELAY_MS', '1000', 'NUMBER', 'Thời gian chờ giữa các lần thử lại (milliseconds)'),
('SESSION_TIMEOUT_MINUTES', '60', 'NUMBER', 'Thời gian timeout của session'),
('PASSWORD_MIN_LENGTH', '8', 'NUMBER', 'Độ dài tối thiểu của mật khẩu'),
('API_KEY_PREFIX_LENGTH', '8', 'NUMBER', 'Độ dài prefix hiển thị của API Key'),
('DEFAULT_CURRENCY', 'VND', 'STRING', 'Đơn vị tiền tệ mặc định'),
('TAX_DEFAULT_RATE', '10.00', 'NUMBER', 'Thuế suất mặc định (%)'),
('INVOICE_NUMBER_FORMAT', 'PREFIX + NUMBER + SUFFIX', 'STRING', 'Cấu trúc số hóa đơn mặc định'),
('BACKGROUND_JOB_ENABLED', 'true', 'BOOLEAN', 'Kích hoạt xử lý background jobs'),
('INVOICE_LOOKUP_CODE_LENGTH', '6', 'NUMBER', 'Độ dài mã tra cứu hóa đơn ngẫu nhiên');



