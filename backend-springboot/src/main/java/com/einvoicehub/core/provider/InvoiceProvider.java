package com.einvoicehub.core.provider;

import com.einvoicehub.core.domain.enums.InvoiceStatus;
import com.einvoicehub.core.provider.model.InvoiceRequest;
import com.einvoicehub.core.provider.model.InvoiceResponse;

public interface InvoiceProvider {
    String getProviderCode();
    String getProviderName();
    boolean isAvailable();

    InvoiceResponse issueInvoice(InvoiceRequest request, ProviderConfig config);
    InvoiceStatus getInvoiceStatus(String invoiceNumber, ProviderConfig config);
    InvoiceResponse cancelInvoice(String invoiceNumber, String reason, ProviderConfig config);
    InvoiceResponse replaceInvoice(String oldInvoiceNumber, InvoiceRequest newRequest, ProviderConfig config);

    /* Returns PDF content as Base64 or URL */
    String getInvoicePdf(String invoiceNumber, ProviderConfig config);

    /* Returns legal XML content as Base64 or String */
    String getInvoiceXml(String invoiceNumber, ProviderConfig config);

    String authenticate(ProviderConfig config);
    boolean testConnection(ProviderConfig config);
}