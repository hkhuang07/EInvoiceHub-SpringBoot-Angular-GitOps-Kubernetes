-- EInvoiceHub Database Schema [MySQL - MongoDB]

--Table
-- merchants , merchant_users,
-- api_credentials, service_providers,merchant_provider_configs
-- invoices_metadata,
-- audit_logs, system_config

-- 1. ENUM Types Definition
-- Subscription Plan: Trial, Basic, Premium
DROP TYPE IF EXISTS subscription_plan;
CREATE TYPE subscription_plan AS ENUM ('TRIAL', 'BASIC', 'PREMIUM');

-- Entity Status: Active, Inactive, Suspended
DROP TYPE IF EXISTS entity_status;
CREATE TYPE entity_status AS ENUM ('ACTIVE', 'INACTIVE', 'SUSPENDED');

-- Invoice Status: Workflow states
DROP TYPE IF EXISTS invoice_status;
CREATE TYPE invoice_status AS ENUM ('DRAFT', 'PENDING', 'SIGNING', 'SENT_TO_PROVIDER', 'SUCCESS', 'FAILED', 'CANCELLED', 'REPLACED');

-- User Role: Phân quyền trong Merchant
DROP TYPE IF EXISTS user_role;
CREATE TYPE user_role AS ENUM ('ADMIN', 'MANAGER', 'STAFF', 'VIEWER');


-- 2. Merchants Table - Quản lý doanh nghiệp
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
    subscription_plan subscription_plan NOT NULL DEFAULT 'TRIAL' COMMENT 'Gói dịch vụ',
    invoice_quota INT NOT NULL DEFAULT 100 COMMENT 'Hạn mức hóa đơn/tháng',
    invoice_used INT NOT NULL DEFAULT 0 COMMENT 'Hóa đơn đã sử dụng trong tháng',
    status entity_status NOT NULL DEFAULT 'ACTIVE' COMMENT 'Trạng thái hoạt động',
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE COMMENT 'Soft delete flag',
    deleted_at TIMESTAMP NULL COMMENT 'Thời điểm xóa',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Thời điểm tạo',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Thời điểm cập nhật',
    
    INDEX idx_tax_code (tax_code),
    INDEX idx_status (status),
    INDEX idx_subscription_plan (subscription_plan),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Quản lý doanh nghiệp sử dụng dịch vụ';


-- 3. Merchant Users - Tài khoản người dùng của Merchant
CREATE TABLE merchant_users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    merchant_id BIGINT NOT NULL COMMENT 'Liên kết với doanh nghiệp',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT 'Tên đăng nhập',
    email VARCHAR(255) NOT NULL UNIQUE COMMENT 'Email người dùng',
    password_hash VARCHAR(255) NOT NULL COMMENT 'Mật khẩu đã mã hóa BCrypt',
    full_name VARCHAR(100) COMMENT 'Họ và tên đầy đủ',
    phone VARCHAR(20) COMMENT 'Số điện thoại',
    avatar_url VARCHAR(500) COMMENT 'URL ảnh đại diện',
    role user_role NOT NULL DEFAULT 'STAFF' COMMENT 'Vai trò trong hệ thống',
    is_active BOOLEAN NOT NULL DEFAULT TRUE COMMENT 'Kích hoạt tài khoản',
    is_primary BOOLEAN NOT NULL DEFAULT FALSE COMMENT 'Tài khoản chính của doanh nghiệp',
    last_login_at TIMESTAMP NULL COMMENT 'Đăng nhập lần cuối',
    failed_login_attempts INT NOT NULL DEFAULT 0 COMMENT 'Số lần đăng nhập sai',
    locked_until TIMESTAMP NULL COMMENT 'Khóa tài khoản đến thời điểm',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_merchant_users_merchant FOREIGN KEY (merchant_id) 
        REFERENCES merchants(id) ON DELETE CASCADE,
    
    INDEX idx_merchant_id (merchant_id),
    INDEX idx_username (username),
    INDEX idx_email (email),
    INDEX idx_role (role),
    INDEX idx_is_active (is_active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Tài khoản người dùng thuộc Merchant';


-- 4. API Credentials - Bảo mật và tích hợp API
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
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
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


-- 5. Service Providers - Nhà cung cấp hóa đơn điện tử
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
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_provider_code (provider_code),
    INDEX idx_is_active (is_active),
    INDEX idx_is_default (is_default),
    INDEX idx_display_order (display_order)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Danh mục nhà cung cấp hóa đơn điện tử';

-- Insert default providers
INSERT INTO service_providers (provider_code, provider_name, official_api_url, is_active, is_default, display_order) VALUES
('VNPT', 'VNPT Invoice', 'https://api.vnptinvoice.com.vn/v1', TRUE, FALSE, 1),
('VIETTEL', 'Viettel Business Invoice', 'https://ebill.vietteltelecom.vn/api/v1', TRUE, FALSE, 2),
('MISA', 'MISA Mimosa', 'https://api.misa.com.vn/einvoice/v1', FALSE, FALSE, 3),
('BKAV', 'BKAV eInvoice', 'https://einvoice.bkav.com/api/v1', FALSE, FALSE, 4);


-- 6. Merchant Provider Configs - Cấu hình riêng của Merchant
CREATE TABLE merchant_provider_configs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    merchant_id BIGINT NOT NULL COMMENT 'Doanh nghiệp sở hữu',
    provider_id BIGINT NOT NULL COMMENT 'Nhà cung cấp được chọn',
    username_service VARCHAR(100) NOT NULL COMMENT 'Tài khoản API do Provider cấp',
    password_service_encrypted VARCHAR(500) NOT NULL COMMENT 'Mật khẩu API - đã mã hóa AES-256',
    certificate_serial VARCHAR(100) NULL COMMENT 'Serial number của chữ ký số',
    certificate_data TEXT NULL COMMENT 'Chứng thư số - đã mã hóa',
    certificate_expired_at TIMESTAMP NULL COMMENT 'Hạn chứng thư số',
    extra_config JSON NULL COMMENT 'Cấu hình bổ sung: pattern, serial, template',
    is_active BOOLEAN NOT NULL DEFAULT TRUE COMMENT 'Cấu hình còn hiệu lực',
    is_default BOOLEAN NOT NULL DEFAULT FALSE COMMENT 'Provider mặc định của Merchant',
    last_sync_at TIMESTAMP NULL COMMENT 'Lần đồng bộ cuối với Provider',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
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


-- 7. Invoice Metadata - Thông tin tóm tắt hóa đơn
CREATE TABLE invoices_metadata (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    merchant_id BIGINT NOT NULL COMMENT 'Doanh nghiệp phát hành',
    provider_id BIGINT NULL COMMENT 'Provider xử lý hóa đơn',
    provider_config_id BIGINT NULL COMMENT 'Cấu hình Provider được sử dụng',
    client_request_id VARCHAR(100) NULL COMMENT 'ID request từ Merchant',
    
    -- Thông tin hóa đơn
    invoice_number VARCHAR(20) NULL COMMENT 'Số hóa đơn từ Provider trả về',
    symbol_code VARCHAR(10) NULL COMMENT 'Ký hiệu hóa đơn ',
    invoice_type_code VARCHAR(10) NULL COMMENT 'Mã loại hóa đơn: 01GTKT, 02GTTT, 07KPTQ...',
    template_code VARCHAR(20) NULL COMMENT 'Mẫu hóa đơn: 01GTKT0/001, 02GTTT0/001...',
    
    -- Thông tin người bán (từ merchant data)
    seller_name VARCHAR(255) NULL COMMENT 'Tên người bán',
    seller_tax_code VARCHAR(20) NULL COMMENT 'Mã số thuế người bán',
    seller_address TEXT NULL COMMENT 'Địa chỉ người bán',
    
    -- Thông tin người mua
    buyer_name VARCHAR(255) NULL COMMENT 'Tên người mua',
    buyer_tax_code VARCHAR(20) NULL COMMENT 'Mã số thuế người mua',
    buyer_email VARCHAR(255) NULL COMMENT 'Email người mua (nhận hóa đơn)',
    buyer_phone VARCHAR(20) NULL COMMENT 'Điện thoại người mua',
    buyer_address TEXT NULL COMMENT 'Địa chỉ người mua',
    
    -- Thông tin tài chính
    subtotal_amount DECIMAL(18,2) NOT NULL DEFAULT 0.00 COMMENT 'Tổng tiền trước thuế',
    tax_amount DECIMAL(18,2) NOT NULL DEFAULT 0.00 COMMENT 'Tổng tiền thuế',
    discount_amount DECIMAL(18,2) NOT NULL DEFAULT 0.00 COMMENT 'Tổng chiết khấu',
    total_amount DECIMAL(18,2) NOT NULL DEFAULT 0.00 COMMENT 'Tổng tiền thanh toán',
    currency_code VARCHAR(3) NOT NULL DEFAULT 'VND' COMMENT 'Đơn vị tiền tệ',
    exchange_rate DECIMAL(10,4) NOT NULL DEFAULT 1.0000 COMMENT 'Tỷ giá với VND',
    
    -- Thông tin thời gian
    issue_date DATE NULL COMMENT 'Ngày lập hóa đơn',
    accounting_date DATE NULL COMMENT 'Ngày hạch toán',
    due_date DATE NULL COMMENT 'Ngày thanh toán đến hạn',
    
    -- Trạng thái và điều khoản
    status invoice_status NOT NULL DEFAULT 'DRAFT' COMMENT 'Trạng thái xử lý',
    status_message TEXT NULL COMMENT 'Thông báo trạng thái chi tiết',
    cancellation_reason VARCHAR(500) NULL COMMENT 'Lý do hủy/thay thế',
    replaced_by_invoice_id BIGINT NULL COMMENT 'ID hóa đơn thay thế',
    
    -- Liên kết MongoDB
    mongo_payload_id VARCHAR(50) NULL COMMENT 'ObjectId trong collection invoice_payloads',
    mongo_transaction_id VARCHAR(50) NULL COMMENT 'ObjectId trong collection provider_transactions',
    
    -- Tracking
    signed_at TIMESTAMP NULL COMMENT 'Thời điểm ký điện tử',
    sent_to_provider_at TIMESTAMP NULL COMMENT 'Thời điểm gửi sang Provider',
    received_from_provider_at TIMESTAMP NULL COMMENT 'Thời điểm nhận phản hồi',
    delivered_to_buyer_at TIMESTAMP NULL COMMENT 'Thời điểm gửi hóa đơn cho người mua',
    
    -- Provider response reference
    provider_transaction_code VARCHAR(100) NULL COMMENT 'Mã giao dịch từ Provider',
    provider_error_code VARCHAR(50) NULL COMMENT 'Mã lỗi từ Provider',
    provider_error_message TEXT NULL COMMENT 'Chi tiết lỗi từ Provider',
    
    -- Soft delete
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE COMMENT 'Hóa đơn đã bị xóa',
    deleted_at TIMESTAMP NULL COMMENT 'Thời điểm xóa',
    deleted_by BIGINT NULL COMMENT 'Người xóa - merchant_users.id',
    
    -- Timestamps
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Thời điểm tạo',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Thời điểm cập nhật',
    
    -- Foreign Keys
    CONSTRAINT fk_invoice_merchant FOREIGN KEY (merchant_id) 
        REFERENCES merchants(id) ON DELETE RESTRICT,
    CONSTRAINT fk_invoice_provider FOREIGN KEY (provider_id) 
        REFERENCES service_providers(id) ON DELETE SET NULL,
    CONSTRAINT fk_invoice_config FOREIGN KEY (provider_config_id) 
        REFERENCES merchant_provider_configs(id) ON DELETE SET NULL,
    CONSTRAINT fk_invoice_replaced_by FOREIGN KEY (replaced_by_invoice_id) 
        REFERENCES invoices_metadata(id) ON DELETE SET NULL,
    CONSTRAINT fk_invoice_deleted_by FOREIGN KEY (deleted_by) 
        REFERENCES merchant_users(id) ON DELETE SET NULL,
    
    -- Indexes for performance
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
    INDEX idx_merchant_created (merchant_id, created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Metadata hóa đơn - phục vụ tra cứu nhanh';


-- 8. Audit Log - Nhật ký thao tác
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


-- 9. System Configuration - Cấu hình hệ thống
CREATE TABLE system_config (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    config_key VARCHAR(100) NOT NULL UNIQUE COMMENT 'Khóa cấu hình',
    config_value TEXT NOT NULL COMMENT 'Giá trị cấu hình',
    config_type VARCHAR(20) NOT NULL DEFAULT 'STRING' COMMENT 'Loại: STRING, NUMBER, BOOLEAN, JSON',
    description VARCHAR(500) NULL COMMENT 'Mô tả cấu hình',
    is_encrypted BOOLEAN NOT NULL DEFAULT FALSE COMMENT 'Có cần mã hóa không',
    is_editable BOOLEAN NOT NULL DEFAULT TRUE COMMENT 'Có thể chỉnh sửa không',
    updated_by BIGINT NULL COMMENT 'Người cập nhật cuối',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
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
('TAX_DEFAULT_RATE', '10.00', 'NUMBER', 'Thuế suất mặc định (%)');


/* TRIGGER VERSION 3
-- PHẦN 3: TRIGGERS
-- Trigger 1: Tự động cập nhật merchants.invoice_used khi hóa đơn chuyển sang SUCCESS
DELIMITER //

CREATE TRIGGER trg_invoice_status_success
    AFTER UPDATE ON invoices_metadata
    FOR EACH ROW
BEGIN
    -- Kiểm tra nếu trạng thái chuyển sang SUCCESS và merchant_id tồn tại
    IF NEW.status = 'SUCCESS' AND OLD.status != 'SUCCESS' AND NEW.merchant_id IS NOT NULL THEN
    UPDATE merchants
    SET invoice_used = invoice_used + 1,
        updated_at = NOW()
    WHERE id = NEW.merchant_id;

    -- Ghi audit log
    INSERT INTO audit_logs (merchant_id, user_id, entity_type, entity_id, action, old_value, new_value)
    VALUES (
               NEW.merchant_id,
               NULL,
               'Invoice',
               NEW.id,
               'STATUS_CHANGE',
               CONCAT('{"status": "', OLD.status, '"}'),
               CONCAT('{"status": "', NEW.status, '", "invoice_used_incremented": true}')
           );
END IF;

-- Xử lý trường hợp hủy hóa đơn: giảm invoice_used
IF NEW.status = 'CANCELLED' AND OLD.status = 'SUCCESS' AND NEW.merchant_id IS NOT NULL THEN
UPDATE merchants
SET invoice_used = GREATEST(0, invoice_used - 1),
    updated_at = NOW()
WHERE id = NEW.merchant_id;
END IF;
END //

DELIMITER ;


-- Trigger 2: Tự động tính toán total_amount từ invoice_items
DELIMITER //

CREATE TRIGGER trg_calculate_invoice_totals
    AFTER INSERT ON invoice_items
    FOR EACH ROW
BEGIN
    -- Tính toán lại các tổng tiền cho hóa đơn
    UPDATE invoices_metadata SET
                                 subtotal_amount = (
                                     SELECT COALESCE(SUM(amount), 0)
                                     FROM invoice_items
                                     WHERE invoice_id = NEW.invoice_id
                                 ),
                                 tax_amount = (
                                     SELECT COALESCE(SUM(tax_amount), 0)
                                     FROM invoice_items
                                     WHERE invoice_id = NEW.invoice_id
                                 ),
                                 discount_amount = (
                                     SELECT COALESCE(SUM(discount_amount), 0)
                                     FROM invoice_items
                                     WHERE invoice_id = NEW.invoice_id
                                 ),
                                 total_amount = (
                                     SELECT COALESCE(SUM(total_amount), 0)
                                     FROM invoice_items
                                     WHERE invoice_id = NEW.invoice_id
                                 ),
                                 updated_at = NOW()
    WHERE id = NEW.invoice_id;
END //

DELIMITER ;


-- Trigger 3: Tự động cập nhật totals khi invoice_items được cập nhật
DELIMITER //

CREATE TRIGGER trg_update_invoice_totals
    AFTER UPDATE ON invoice_items
    FOR EACH ROW
BEGIN
    UPDATE invoices_metadata SET
                                 subtotal_amount = (
                                     SELECT COALESCE(SUM(amount), 0)
                                     FROM invoice_items
                                     WHERE invoice_id = NEW.invoice_id
                                 ),
                                 tax_amount = (
                                     SELECT COALESCE(SUM(tax_amount), 0)
                                     FROM invoice_items
                                     WHERE invoice_id = NEW.invoice_id
                                 ),
                                 discount_amount = (
                                     SELECT COALESCE(SUM(discount_amount), 0)
                                     FROM invoice_items
                                     WHERE invoice_id = NEW.invoice_id
                                 ),
                                 total_amount = (
                                     SELECT COALESCE(SUM(total_amount), 0)
                                     FROM invoice_items
                                     WHERE invoice_id = NEW.invoice_id
                                 ),
                                 updated_at = NOW()
    WHERE id = NEW.invoice_id;
END //

DELIMITER ;


-- Trigger 4: Tự động cập nhật totals khi invoice_items bị xóa
DELIMITER //

CREATE TRIGGER trg_delete_invoice_totals
    AFTER DELETE ON invoice_items
    FOR EACH ROW
BEGIN
    UPDATE invoices_metadata SET
                                 subtotal_amount = (
                                     SELECT COALESCE(SUM(amount), 0)
                                     FROM invoice_items
                                     WHERE invoice_id = OLD.invoice_id
                                 ),
                                 tax_amount = (
                                     SELECT COALESCE(SUM(tax_amount), 0)
                                     FROM invoice_items
                                     WHERE invoice_id = OLD.invoice_id
                                 ),
                                 discount_amount = (
                                     SELECT COALESCE(SUM(discount_amount), 0)
                                     FROM invoice_items
                                     WHERE invoice_id = OLD.invoice_id
                                 ),
                                 total_amount = (
                                     SELECT COALESCE(SUM(total_amount), 0)
                                     FROM invoice_items
                                     WHERE invoice_id = OLD.invoice_id
                                 ),
                                 updated_at = NOW()
    WHERE id = OLD.invoice_id;
END //

DELIMITER ;


-- Trigger 5: Tự động tạo invoice_tax_breakdowns khi invoice được tạo
DELIMITER //

CREATE TRIGGER trg_create_tax_breakdowns
    AFTER INSERT ON invoice_items
    FOR EACH ROW
BEGIN
    -- Chỉ chạy một lần sau khi tất cả items được insert (kiểm tra bằng count)
    INSERT INTO invoice_tax_breakdowns
    (invoice_id, vat_rate_id, vat_rate_code, vat_rate_percent,
     subtotal_amount, discount_amount, taxable_amount, tax_amount, total_amount)
    SELECT
        NEW.invoice_id,
        vr.id,
        vr.rate_code,
        vr.rate_percent,
        COALESCE(SUM(ii.amount), 0) AS subtotal_amount,
        COALESCE(SUM(ii.discount_amount), 0) AS discount_amount,
        COALESCE(SUM(ii.amount - ii.discount_amount), 0) AS taxable_amount,
        COALESCE(SUM(ii.tax_amount), 0) AS tax_amount,
        COALESCE(SUM(ii.total_amount), 0) AS total_amount
    FROM invoice_items ii
             INNER JOIN vat_rates vr ON vr.id = NEW.vat_rate_id
    WHERE ii.invoice_id = NEW.invoice_id
        ON DUPLICATE KEY UPDATE
                             subtotal_amount = VALUES(subtotal_amount),
                             discount_amount = VALUES(discount_amount),
                             taxable_amount = VALUES(taxable_amount),
                             tax_amount = VALUES(tax_amount),
                             total_amount = VALUES(total_amount);
END //

DELIMITER ;


-- Trigger 6: Tự động cập nhật current_number trong invoice_registrations
DELIMITER //

CREATE TRIGGER trg_update_registration_number
    AFTER INSERT ON invoices_metadata
    FOR EACH ROW
BEGIN
    IF NEW.invoice_registration_id IS NOT NULL AND NEW.status = 'SUCCESS' THEN
    UPDATE invoice_registrations SET
                                     current_number = current_number + 1,
                                     status = CASE
                                                  WHEN current_number + 1 >= to_number THEN 'EXHAUSTED'
                                                  ELSE 'ACTIVE'
                                         END,
                                     updated_at = NOW()
    WHERE id = NEW.invoice_registration_id;
END IF;
END //

DELIMITER ;


-- Trigger 7: Tự động tạo lookup_code khi hóa đơn được tạo
DELIMITER //

CREATE TRIGGER trg_generate_lookup_code
    BEFORE INSERT ON invoices_metadata
    FOR EACH ROW
BEGIN
    -- Tự động tạo lookup_code nếu chưa có
    IF NEW.lookup_code IS NULL AND NEW.invoice_number IS NOT NULL THEN
        SET NEW.lookup_code = CONCAT(
            NEW.invoice_number,
            '-',
            LPAD(FLOOR(RAND() * 1000000), 6, '0')
        );
END IF;
END //

DELIMITER ;


-- View: Tổng hợp thông tin hóa đơn đầy đủ
CREATE VIEW v_invoices_full AS
SELECT
    im.id AS invoice_id,
    im.invoice_number,
    im.symbol_code,
    im.template_code,
    im.cqt_code,
    im.status,
    im.issue_date,
    im.total_amount,
    im.tax_amount,
    im.subtotal_amount,
    im.discount_amount,
    im.currency_code,
    im.payment_method,
    im.issuance_method,
    m.tax_code AS merchant_tax_code,
    m.company_name AS merchant_name,
    im.seller_name,
    im.seller_tax_code,
    im.buyer_name,
    im.buyer_tax_code,
    im.buyer_email,
    im.created_at,
    im.provider_transaction_code
FROM invoices_metadata im
         INNER JOIN merchants m ON m.id = im.merchant_id;


-- View: Tổng hợp tax breakdown theo hóa đơn
CREATE VIEW v_invoice_tax_summary AS
SELECT
    itb.invoice_id,
    im.invoice_number,
    im.symbol_code,
    itb.vat_rate_code,
    itb.vat_rate_percent,
    itb.subtotal_amount,
    itb.taxable_amount,
    itb.tax_amount,
    itb.total_amount
FROM invoice_tax_breakdowns itb
         INNER JOIN invoices_metadata im ON im.id = itb.invoice_id
ORDER BY im.invoice_number, itb.vat_rate_percent DESC;


-- View: Thống kê số lượng hóa đơn theo trạng thái
CREATE VIEW v_invoice_stats_by_status AS
SELECT
    m.id AS merchant_id,
    m.tax_code AS merchant_tax_code,
    m.company_name AS merchant_name,
    im.status,
    COUNT(*) AS invoice_count,
    COALESCE(SUM(im.total_amount), 0) AS total_amount,
    COALESCE(SUM(im.tax_amount), 0) AS total_tax_amount
FROM invoices_metadata im
         INNER JOIN merchants m ON m.id = im.merchant_id
WHERE im.is_deleted = FALSE
GROUP BY m.id, m.tax_code, m.company_name, im.status;


-- View: Danh sách pending sync jobs
CREATE VIEW v_pending_sync_jobs AS
SELECT
    isq.id AS queue_id,
    isq.invoice_id,
    im.invoice_number,
    im.symbol_code,
    m.tax_code AS merchant_tax_code,
    sp.provider_code,
    isq.sync_type,
    isq.priority,
    isq.attempt_count,
    isq.max_attempts,
    isq.status,
    isq.error_message,
    isq.next_retry_at
FROM invoice_sync_queue isq
         INNER JOIN invoices_metadata im ON im.id = isq.invoice_id
         INNER JOIN merchants m ON m.id = isq.merchant_id
         LEFT JOIN service_providers sp ON sp.id = isq.provider_id
WHERE isq.status IN ('PENDING', 'RETRYING')
ORDER BY isq.priority ASC, isq.next_retry_at ASC;


-- ============================================================================
-- PHẦN 5: STORED PROCEDURES (THỦ TỤC THƯỜNG DÙNG)
-- ============================================================================

-- Procedure: Cập nhật invoice_registration khi sử dụng số hóa đơn
DELIMITER //

CREATE PROCEDURE sp_use_invoice_number(
    IN p_registration_id BIGINT,
    OUT p_new_number BIGINT
)
BEGIN
    DECLARE v_current_number BIGINT;
    DECLARE v_max_number BIGINT;

    -- Lấy số hiện tại và max
SELECT current_number, max_number INTO v_current_number, v_max_number
FROM invoice_registrations
WHERE id = p_registration_id
    FOR UPDATE;

-- Kiểm tra còn số không
IF v_current_number < v_max_number THEN
        -- Tăng số hiện tại
UPDATE invoice_registrations SET
                                 current_number = current_number + 1,
                                 updated_at = NOW()
WHERE id = p_registration_id;

SET p_new_number = v_current_number + 1;
ELSE
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Dải số hóa đơn đã hết';
END IF;
END //

DELIMITER ;


-- Procedure: Reset invoice_used đầu tháng cho tất cả merchants
DELIMITER //

CREATE PROCEDURE sp_reset_monthly_invoice_usage()
BEGIN
UPDATE merchants SET
                     invoice_used = 0,
                     updated_at = NOW()
WHERE status = 'ACTIVE';

SELECT ROW_COUNT() AS merchants_reset;
END //

DELIMITER ;


-- Procedure: Tạo adjustment cho hóa đơn điều chỉnh
DELIMITER //

CREATE PROCEDURE sp_create_adjustment(
    IN p_original_invoice_id BIGINT,
    IN p_adjustment_type ENUM('ADJUSTMENT', 'REPLACEMENT', 'CANCELLATION'),
    IN p_agreement_number VARCHAR(50),
    IN p_agreement_date DATE,
    IN p_agreement_content TEXT,
    IN p_reason_code VARCHAR(50),
    IN p_reason_description VARCHAR(500),
    OUT p_adjustment_id BIGINT
        )
BEGIN
    DECLARE v_old_subtotal DECIMAL(18,2);
    DECLARE v_old_tax DECIMAL(18,2);
    DECLARE v_old_total DECIMAL(18,2);

    -- Lấy thông tin hóa đơn gốc
SELECT subtotal_amount, tax_amount, total_amount
INTO v_old_subtotal, v_old_tax, v_old_total
FROM invoices_metadata
WHERE id = p_original_invoice_id;

-- Tạo bản ghi adjustment
INSERT INTO invoice_adjustments (
    original_invoice_id,
    adjustment_type,
    agreement_number,
    agreement_date,
    agreement_content,
    reason_code,
    reason_description,
    old_subtotal_amount,
    old_tax_amount,
    old_total_amount,
    status,
    created_at
) VALUES (
             p_original_invoice_id,
             p_adjustment_type,
             p_agreement_number,
             p_agreement_date,
             p_agreement_content,
             p_reason_code,
             p_reason_description,
             v_old_subtotal,
             v_old_tax,
             v_old_total,
             'PENDING',
             NOW()
         );

SET p_adjustment_id = LAST_INSERT_ID();
END //

DELIMITER ;


-- Procedure: Retry failed sync jobs
DELIMITER //

CREATE PROCEDURE sp_retry_failed_sync_jobs(
    IN p_limit INT
)
BEGIN
    -- Cập nhật các job failed để retry
UPDATE invoice_sync_queue SET
                              status = 'RETRYING',
                              next_retry_at = DATE_ADD(NOW(), INTERVAL 5 MINUTE),
                              updated_at = NOW()
WHERE status = 'FAILED'
  AND attempt_count < max_attempts
  AND next_retry_at <= NOW()
    LIMIT p_limit;

SELECT ROW_COUNT() AS jobs_requeued;
END //

DELIMITER ;
*/

-- End of Database Schema Version 3
