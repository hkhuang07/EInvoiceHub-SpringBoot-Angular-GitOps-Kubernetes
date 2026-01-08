-- EInvoiceHub Database Schema
-- Đọc file resource.db.migration sau, hãy cho biết cơ sở dữ liệu nay có diểm gì?
-- Database này đáp ưứng được những yêu cầu nghiệp vụ nào của hệ thống
-- Chỉ ra nhưững hạn chế của database này , database này không thể đáp ứng những gì cho môt hệ thống trung tâm hóa đơn điệm tử chuyên mghiệp
-- Cần phải bổ sung thêm các bảng nào, trường nào, cột nào để đảm bảo database đủ các bảng phục vụ cho đồ án Trung tâm hóa đơn điện tử
-- Viết một form hoàn chỉnh hướng dẫn cho Minimax Agent phát trien những ưu điểm hiêện tại của database và khắc phục các hạn chees hiện tại của dâtabase

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

