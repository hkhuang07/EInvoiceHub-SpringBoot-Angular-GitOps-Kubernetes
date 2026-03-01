create table category_tax_type
(
    id           varchar(36) collate latin1_general_ci not null
        primary key,
    tax_name     varchar(100) null,
    tax_name_en  varchar(100) null,
    description  varchar(100) null,
    vat          decimal(15, 2) null,
    created_by   varchar(36) null,
    updated_by   varchar(36) null,
    created_date datetime null,
    updated_date datetime null
);

create table einv_invoice_status
(
    id          int          not null
        primary key,
    name        varchar(255) not null,
    description varchar(255) not null,
    note        varchar(255) null,
    constraint name
        unique (name)
);

create table einv_invoice_type
(
    id   int          not null
        primary key,
    name varchar(255) not null,
    note varchar(255) null,
    constraint name
        unique (name)
);

create table einv_invoices
(
    id                  varchar(36) collate latin1_general_ci default uuid_v7() not null
        primary key,
    tenant_id           varchar(36) collate latin1_general_ci null,
    store_id            varchar(36) collate latin1_general_ci null,
    partner_invoice_id  varchar(50) collate latin1_general_ci null comment 'KhoaPhieuGoc (Original Receipt ID)',
    provider_id         varchar(36) collate latin1_general_ci null comment 'DoiTacLienKet (Linked Partner)',
    provider_invoice_id varchar(50) collate latin1_general_ci null comment 'ID Hóa đơn do Provider cung cấp',
    invoice_type_id     int null comment 'Loại hóa đơn',
    reference_type_id   int null comment 'Tính chất hóa đơn',
    status_id           int null comment 'Internal InvoiceStatusID',
    invoice_form        varchar(50) null comment 'HoaDon_Mau (Invoice Form)',
    invoice_series      varchar(50) null comment 'HoaDon_KyHieu (Invoice series)',
    invoice_no          varchar(50) null comment 'HoaDon_So (Invoice Number)',
    invoice_date        datetime null comment 'HoaDon_Ngay (Invoice Date)',
    payment_method_id   int null comment 'HoaDon_HinhThucThanhToan (Payment Method)',
    buyer_tax_code      varchar(50) null comment 'HoaDon_MaSoThue (Tax Code)',
    buyer_company       varchar(300) null comment 'HoaDon_DonVi (Company/Organization)',
    buyer_id_no         varchar(20) collate latin1_general_ci null comment 'CCCD/Hộ chiếu',
    buyer_full_name     varchar(200) null comment 'HoaDon_NguoiMua (Name)',
    buyer_address       varchar(300) null comment 'HoaDon_DiaChi (Address)',
    buyer_mobile        varchar(50) null,
    buyer_bank_account  varchar(50) null comment 'HoaDon_SoTaiKhoan (Bank Account)',
    buyer_bank_name     varchar(200) null comment 'Tên ngân hàng người mua',
    buyer_budget_code   varchar(20) null comment 'MSĐVCQHVNS',
    receive_type_id     int null comment 'Kiểu nhận hóa đơn (sms, email...)',
    receiver_email      varchar(300) null comment 'Email của người mua',
    currency_code       varchar(20) null comment 'Mã tiền tệ',
    exchange_rate       decimal(10, 2) null comment 'Tỷ giá',
    signed_date         datetime null comment 'NgayPhatHanh (Release Date)',
    tax_authority_code  varchar(50) null comment 'MaCuaCQT (Tax Authority Code)',
    org_invoice_id      varchar(36) collate latin1_general_ci null,
    org_invoice_form    varchar(50) null comment 'HoaDon_Mau Gốc (Invoice Form)',
    org_invoice_series  varchar(50) null comment 'HoaDon_KyHieu Gốc (Invoice series)',
    org_invoice_no      varchar(50) null comment 'HoaDon_So Gốc (Invoice Number)',
    org_invoice_date    datetime null comment 'HoaDon_Ngay Gốc (Invoice Date)',
    org_invoice_reason  varchar(500) null comment 'HoaDon_LyDo (Reason)',
    invoice_lookup_code varchar(50) null comment 'HoaDon_MaTraCuu (reference code)',
    gross_amount        decimal(15, 2) null comment 'Thành tiền hàng hóa',
    discount_amount     decimal(15, 2) null comment 'Số tiền chiết khấu',
    net_amount          decimal(15, 2) null comment 'Thành tiền trước thuế',
    tax_amount          decimal(15, 2) null comment 'Số tiền thuế',
    total_amount        decimal(15, 2) null comment 'Trị giá thanh toán',
    created_by          varchar(36) collate latin1_general_ci null,
    updated_by          varchar(36) collate latin1_general_ci null,
    created_date        datetime null,
    updated_date        datetime null
);

create table einv_invoices_detail
(
    id              varchar(36) collate latin1_general_ci default uuid_v7() not null
        primary key,
    tenant_id       varchar(36) collate latin1_general_ci null,
    store_id        varchar(36) collate latin1_general_ci null,
    doc_id          varchar(36) collate latin1_general_ci null,
    line_no         int null comment 'Số thứ tự',
    is_free         tinyint(1)                                              null comment 'Check hàng tặng, đơn giá = 0',
    item_type_id    int null comment 'id_table: einv_item_type',
    quantity        decimal(15, 2) null comment 'Số lượng',
    item_id         varchar(36) collate latin1_general_ci null comment 'Mã hàng hóa, dịch vụ',
    item_name       varchar(500) null comment 'Tên hàng hóa, dịch vụ',
    unit            varchar(50) null comment 'Đơn vị tính',
    price           decimal(15, 2) null comment 'Đơn giá',
    gross_amount    decimal(15, 2) null comment 'Thành tiền = quantity*price',
    discount_rate   decimal(15, 2) null comment 'Tỷ lệ CK',
    discount_amount decimal(15, 2) null comment 'Số tiền CK',
    net_price_vat   decimal(15, 2) null comment 'Đơn giá có VAT = Thanh toán / Số lượng',
    net_price       decimal(15, 2) null,
    net_amount      decimal(15, 2) null,
    tax_type_id     varchar(36) collate latin1_general_ci null comment 'id_table: category_tax_type',
    tax_rate        decimal(15, 2) null comment 'Thuế suất',
    tax_amount      decimal(15, 2) null comment 'Số tiền thuế',
    total_amount    decimal(15, 2) null,
    notes           varchar(500) null comment 'Ghi chú',
    created_by      varchar(36) collate latin1_general_ci null,
    updated_by      varchar(36) collate latin1_general_ci null,
    created_date    datetime null,
    updated_date    datetime null,
    constraint einv_invoices_detail_ibfk_1
        foreign key (doc_id) references einv_invoices (id)
);

create index biz_retail_detail_biz_retail_FK
    on einv_invoices_detail (doc_id);

create index biz_retail_detail_tenant_id_IDX
    on einv_invoices_detail (tenant_id);

create table einv_item_type
(
    id   int          not null
        primary key,
    name varchar(255) not null,
    note varchar(255) null,
    constraint name
        unique (name)
);

create table einv_mapping_invoice_status
(
    id                         varchar(36) collate latin1_general_ci default uuid_v7() not null
        primary key,
    provider_id                varchar(36) collate latin1_general_ci null,
    invoice_status_id          int null,
    provider_invoice_status_id varchar(36) null,
    inactive                   tinyint(1)                            default 0         null comment '0: Mặc định; 1: Disable',
    note                       varchar(200) null
);

create table einv_mapping_invoice_type
(
    id                       varchar(36) collate latin1_general_ci default uuid_v7() not null
        primary key,
    provider_id              varchar(36) collate latin1_general_ci null,
    invoice_type_id          int null,
    provider_invoice_type_id varchar(36) null,
    inactive                 tinyint(1)                            default 0         null comment '0: Mặc định; 1: Disable',
    note                     varchar(200) null
);

create table einv_mapping_item_type
(
    id                    varchar(36) collate latin1_general_ci default uuid_v7() not null
        primary key,
    provider_id           varchar(36) collate latin1_general_ci null,
    item_type_id          int null,
    provider_item_type_id varchar(36) null,
    inactive              tinyint(1)                            default 0         null comment '0: Mặc định; 1: Disable',
    note                  varchar(200) null
);

create table einv_mapping_payment_method
(
    id                         varchar(36) collate latin1_general_ci default uuid_v7() not null
        primary key,
    provider_id                varchar(36) collate latin1_general_ci null,
    payment_method_id          int null,
    provider_payment_method_id varchar(36) null,
    inactive                   tinyint(1)                            default 0         null comment '0: Mặc định; 1: Disable',
    note                       varchar(200) null
);

create table einv_mapping_reference_type
(
    id                         varchar(36) collate latin1_general_ci default uuid_v7() not null
        primary key,
    provider_id                varchar(36) collate latin1_general_ci null,
    reference_type_id          int null,
    provider_reference_type_id varchar(36) null,
    inactive                   tinyint(1)                            default 0         null comment '0: Mặc định; 1: Disable',
    note                       varchar(200) null
);

create table einv_mapping_tax_type
(
    id                   varchar(36) collate latin1_general_ci default uuid_v7() not null
        primary key,
    provider_id          varchar(36) collate latin1_general_ci null,
    tax_type_id          varchar(36) collate latin1_general_ci null,
    provider_tax_type_id varchar(36) null,
    provider_tax_rate    varchar(36) null,
    inactive             tinyint(1)                            default 0         null comment '0: Mặc định; 1: Disable',
    note                 varchar(200) null
);

create table einv_payment_method
(
    id   int          not null
        primary key,
    name varchar(255) not null,
    note varchar(255) null,
    constraint name
        unique (name)
);

create table einv_provider
(
    id              varchar(36) collate latin1_general_ci not null
        primary key,
    provider_name   varchar(200) null,
    integration_url varchar(200) null,
    lookup_url      varchar(200) null,
    inactive        tinyint(1)                            null,
    created_by      varchar(36) collate latin1_general_ci null,
    updated_by      varchar(36) collate latin1_general_ci null,
    created_date    datetime null,
    updated_date    datetime null
);

create table einv_reference_type
(
    id   int          not null
        primary key,
    name varchar(255) not null,
    note varchar(255) null,
    constraint name
        unique (name)
);

create table einv_store_provider
(
    id              varchar(36) collate latin1_general_ci default uuid_v7() not null
        primary key,
    tenant_id       varchar(36) collate latin1_general_ci null,
    store_id        varchar(36) collate latin1_general_ci null,
    provider_id     varchar(36) collate latin1_general_ci null,
    partner_id      varchar(200) null comment 'PartnerGUID, vnpt_Account',
    partner_token   varchar(200) null comment 'PartnerToken; vnpt_ACPass',
    partner_usr     varchar(200) null comment 'MISA: app_id, vnpt_username',
    partner_pwd     varchar(200) null comment 'vnpt_password',
    status          tinyint(1)                                                        null comment '0: Chưa tích hợp; 1: Tích hợp thành công; 8: đã hủy tích hợp',
    integrated_date datetime null,
    integration_url varchar(200) null comment 'nếu = VNPT thì hiển thị và bắt buộc nhập',
    tax_code        varchar(200) null comment 'Mã số thuế',
    created_by      varchar(36) collate latin1_general_ci null,
    updated_by      varchar(36) collate latin1_general_ci null,
    created_date    timestamp                             default current_timestamp() null,
    updated_date    timestamp                             default current_timestamp() null
);

create table einv_store_provider_history
(
    id           varchar(36) collate latin1_general_ci default uuid_v7() not null
        primary key,
    tenant_id    varchar(36) null,
    store_id     varchar(36) collate latin1_general_ci null,
    provider_id  varchar(36) null,
    action_type  varchar(50) null,
    status       tinyint null,
    notes        varchar(100) null,
    created_by   varchar(36) charset latin1                              null,
    created_date varchar(36) charset latin1                              null
);

create table einv_store_serial
(
    id                 varchar(36) collate latin1_general_ci default uuid_v7() not null
        primary key,
    tenant_id          varchar(36) collate latin1_general_ci null,
    store_id           varchar(36) collate latin1_general_ci null,
    provider_id        varchar(36) collate latin1_general_ci null,
    invoice_type_id    int null,
    provider_serial_id varchar(50) null comment 'qlkhsdung_id',
    invoice_form       varchar(20) null comment 'Mẫu số hóa đơn',
    invoice_serial     varchar(20) null comment 'Ký hiệu hóa đơn',
    start_date         datetime null comment 'Ngày bắt đầu sử dụng',
    status             int null comment '0: Chờ duyệt; 1: Đã duyệt; 8: Ngưng sử dụng',
    created_by         varchar(36) null,
    updated_by         varchar(36) null,
    created_date       datetime null,
    updated_date       datetime null
);

