package com.einvoicehub.core.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRole {
        ADMIN("Administrator", "Full access to merchant account"),
        MANAGER("Manager", "Manage invoices and view reports"),
        STAFF("Staff", "Create and view invoices"),
        VIEWER("Viewer", "Read-only access");

        private final String displayName;
        private final String description;

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