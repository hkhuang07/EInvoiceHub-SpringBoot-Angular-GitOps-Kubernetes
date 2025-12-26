CREATE TABLE merchants (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    external_id VARCHAR(36) UNIQUE NOT NULL,
    tax_code VARCHAR(20) UNIQUE NOT NULL,
    company_name VARCHAR(255),
    status ENUM('ACTIVE', 'INACTIVE', 'SUSPENDED') DEFAULT 'ACTIVE',
    subscription_plan ENUM('TRIAL', 'BASIC', 'PREMIUM'),
    is_deleted BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE invoices_metadata (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    merchant_id BIGINT,
    invoice_number VARCHAR(20),
    symbol VARCHAR(10),
    total_amount DECIMAL(15,2),
    status ENUM('DRAFT', 'SUCCESS', 'FAILED', 'CANCELLED'),
    mongo_payload_id VARCHAR(50),
    issue_date TIMESTAMP,
    FOREIGN KEY (merchant_id) REFERENCES merchants(id)
);

-- Tối ưu truy vấn tìm kiếm nhanh
CREATE INDEX idx_merchant_tax_code ON merchants(tax_code);
CREATE INDEX idx_invoice_number ON invoices_metadata(invoice_number);