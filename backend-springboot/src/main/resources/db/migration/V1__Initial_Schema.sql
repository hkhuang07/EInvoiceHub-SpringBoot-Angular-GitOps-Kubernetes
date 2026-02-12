-- EInvoiceHub Database Schema
-- Database: MariaDB   |   Character Set: utf8mb4_unicode_ci

--  I: NHÓM DANH MỤC
-- 1. Invoice Types - Danh mục loại hóa đơn
CREATE TABLE invoice_types
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    type_code  VARCHAR(10)  NOT NULL UNIQUE COMMENT 'Mã loại (VD: 01GTKT)',
    type_name  VARCHAR(100) NOT NULL COMMENT 'Tên loại hóa đơn',
    is_active  BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP             DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 2. Invoice Status - Trạng thái hóa đơn
CREATE TABLE invoice_status
(
    id          INT PRIMARY KEY,
    name        VARCHAR(100) NOT NULL UNIQUE COMMENT 'DRAFT, SUCCESS, FAILED...',
    description VARCHAR(255) NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 3. Payment Method - Phương thức thanh toán
CREATE TABLE payment_method
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    method_code VARCHAR(10)  NOT NULL UNIQUE COMMENT 'TM, CK...',
    method_name VARCHAR(100) NOT NULL,
    is_active   BOOLEAN      NOT NULL DEFAULT TRUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- II: NHÓM QUẢN TRỊ DOANH NGHIỆP
-- 4. Merchants - Thông tin doanh nghiệp
CREATE TABLE merchants
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    tax_code     VARCHAR(20)  NOT NULL UNIQUE,
    company_name VARCHAR(255) NOT NULL,
    address      TEXT,
    email        VARCHAR(255),
    phone        VARCHAR(20),
    is_active    BOOLEAN   DEFAULT TRUE,
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 5. Merchant User - Người dùng (UserAuthor)
CREATE TABLE merchant_user
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    merchant_id   BIGINT       NOT NULL,
    username      VARCHAR(50)  NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    full_name     VARCHAR(100),
    role          VARCHAR(20) DEFAULT 'STAFF',
    CONSTRAINT fk_user_merchant FOREIGN KEY (merchant_id) REFERENCES merchants (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 6. Merchant Provider Configs - Cấu hình kết nối NCC (StoreProvider)
CREATE TABLE merchant_provider_configs
(
    id                         BIGINT AUTO_INCREMENT PRIMARY KEY,
    merchant_id                BIGINT       NOT NULL,
    provider_code              VARCHAR(20)  NOT NULL COMMENT 'VNPT, VIETTEL, BKAV...',
    username_service           VARCHAR(100) NOT NULL,
    password_service_encrypted VARCHAR(500) NOT NULL,
    is_active                  BOOLEAN DEFAULT TRUE,
    CONSTRAINT fk_config_merchant FOREIGN KEY (merchant_id) REFERENCES merchants (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- III.NHÓM NGHIỆP VỤ HÓA ĐƠN
-- 7. Invoice Template - Mẫu số & Ký hiệu (StoreSerial)
CREATE TABLE invoice_template
(
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    merchant_id    BIGINT      NOT NULL,
    config_id      BIGINT      NOT NULL COMMENT 'Liên kết StoreProvider',
    template_code  VARCHAR(20) NOT NULL COMMENT 'Mẫu: 01GTKT0/001',
    symbol_code    VARCHAR(10) NOT NULL COMMENT 'Ký hiệu: 1C23TML',
    current_number INT DEFAULT 0,
    CONSTRAINT fk_template_merchant FOREIGN KEY (merchant_id) REFERENCES merchants (id),
    CONSTRAINT fk_template_config FOREIGN KEY (config_id) REFERENCES merchant_provider_configs (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 8. Invoices Metadata - Thông tin tổng quát hóa đơn
CREATE TABLE invoice_metadata
(
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    merchant_id       BIGINT NOT NULL,
    user_author_id    BIGINT NOT NULL,
    template_id       BIGINT NOT NULL,
    type_id           BIGINT NOT NULL,
    status_id         INT    NOT NULL,
    payment_method_id BIGINT NOT NULL,

    invoice_number    VARCHAR(20),
    issue_date        DATE,

    -- Thông tin người mua (Flattened từ bảng Customers cũ)
    buyer_name        VARCHAR(255),
    buyer_tax_code    VARCHAR(20),
    buyer_email       VARCHAR(255),
    buyer_address     TEXT,

    -- Tiền bạc
    subtotal_amount   DECIMAL(18, 2) DEFAULT 0,
    tax_amount        DECIMAL(18, 2) DEFAULT 0,
    total_amount      DECIMAL(18, 2) DEFAULT 0,

    created_at        TIMESTAMP      DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_inv_merchant FOREIGN KEY (merchant_id) REFERENCES merchants (id),
    CONSTRAINT fk_inv_user FOREIGN KEY (user_author_id) REFERENCES merchant_user (id),
    CONSTRAINT fk_inv_template FOREIGN KEY (template_id) REFERENCES invoice_template (id),
    CONSTRAINT fk_inv_type FOREIGN KEY (type_id) REFERENCES invoice_types (id),
    CONSTRAINT fk_inv_status FOREIGN KEY (status_id) REFERENCES invoice_status (id),
    CONSTRAINT fk_inv_payment FOREIGN KEY (payment_method_id) REFERENCES payment_method (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 9. Invoice Items - Chi tiết dòng hàng
CREATE TABLE invoice_items
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    invoice_id   BIGINT       NOT NULL,
    line_number  INT          NOT NULL,
    product_name VARCHAR(500) NOT NULL,
    quantity     DECIMAL(18, 4) DEFAULT 0,
    unit_price   DECIMAL(18, 4) DEFAULT 0,
    tax_rate     DECIMAL(5, 2)  DEFAULT 0,
    total_amount DECIMAL(18, 2) DEFAULT 0,
    CONSTRAINT fk_items_invoice FOREIGN KEY (invoice_id) REFERENCES invoice_metadata (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;