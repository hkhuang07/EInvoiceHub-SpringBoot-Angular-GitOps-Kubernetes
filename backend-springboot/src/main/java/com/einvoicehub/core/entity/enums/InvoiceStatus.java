package com.einvoicehub.core.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum InvoiceStatus {
    DRAFT("Draft", "Invoice is being drafted"),
    PENDING("Pending", "Request received, awaiting processing"),
    SIGNING("Signing", "Digital signature in progress"),
    SENT_TO_PROVIDER("Sent to Provider", "Forwarded to the service provider"),
    SUCCESS("Success", "Invoice issued successfully"),
    FAILED("Failed", "An error occurred during processing"),
    CANCELLED("Cancelled", "Invoice has been voided"),
    REPLACED("Replaced", "Invoice has been replaced by another");

    private final String displayName;
    private final String description;

    /* Terminal states where no further processing is expected */
    public boolean isTerminal() {
        return this == SUCCESS || this == FAILED || this == CANCELLED || this == REPLACED;
    }

    public boolean isError() {
        return this == FAILED || this == CANCELLED;
    }
}