package com.einvoicehub.core.entity.enums;

public enum  UserRole {
        ADMIN("Quản trị viên", "Toàn quyền trên tài khoản Merchant"),
        MANAGER("Quản lý", "Quản lý hóa đơn và xem báo cáo"),
        STAFF("Nhân viên", "Tạo và xem hóa đơn"),
        VIEWER("Người xem", "Chỉ được xem, không được thay đổi");

        private final String displayName;
        private final String description;

        UserRole(String displayName, String description) {
                this.displayName = displayName;
                this.description = description;
        }

        public String getDisplayName() {
                return displayName;
        }

        public String getDescription() {
                return description;
                }

        public boolean canManageUsers() {
                return this == ADMIN;
                }

        public boolean canCreateInvoice() {
                return this == ADMIN || this == MANAGER || this == STAFF;
                }

        public boolean canViewReports() {
                return this == ADMIN || this == MANAGER || this == VIEWER;
                }
}