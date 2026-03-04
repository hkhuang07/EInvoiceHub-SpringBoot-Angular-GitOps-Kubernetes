package com.einvoicehub.core.controller;

import com.einvoicehub.core.context.TenantContext;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/invoices")
public class InvoiceController {

    //!uỳên STAFF, MANAGER, ADMIN xem được hóa đơn
    @GetMapping
    @PreAuthorize("hasAnyRole('STAFF', 'MANAGER', 'ADMIN', 'API_USER')")
    public String getAllInvoices() {
        return "Danh sách hóa đơn của Merchant ID: " + TenantContext.getMerchantId();
    }

    //ADMIN và MANAGER được cấu hình hệ thống
    @PostMapping("/config")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public String updateConfig() {
        return "Cấu hình đã được cập nhật";
    }

    // ADMIN  được xóa dải số
    @DeleteMapping("/registrations/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteRegistration(@PathVariable Long id) {
        return "Đã xóa dải số " + id;
    }
}