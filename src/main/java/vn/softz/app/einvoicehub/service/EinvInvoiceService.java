package vn.softz.app.einvoicehub.service;

import vn.softz.app.einvoicehub.dto.EinvInvoiceDto;
import vn.softz.app.einvoicehub.dto.request.GetInvoicesRequest;
import vn.softz.app.einvoicehub.dto.request.SignInvoicesRequest;
import vn.softz.app.einvoicehub.dto.request.SubmitInvoiceRequest;
import vn.softz.app.einvoicehub.dto.response.GetInvoicesResponse;
import vn.softz.app.einvoicehub.dto.response.ListInvoicesResponse;
import vn.softz.app.einvoicehub.dto.response.SignInvoiceResponse;
import vn.softz.app.einvoicehub.dto.response.SubmitInvoiceResponse;

import java.util.Optional;

public interface EinvInvoiceService {

    SubmitInvoiceResponse submitInvoice(SubmitInvoiceRequest request);

    SignInvoiceResponse signInvoice(SignInvoicesRequest request);

    void handleCallback(String invoiceId, String callbackPayload, String providerId);

    ListInvoicesResponse getInvoices(GetInvoicesRequest request);

    Optional<EinvInvoiceDto> getInvoiceById(String invoiceId);

    GetInvoicesResponse getInvoiceFromProvider(GetInvoicesRequest request);

    EinvInvoiceDto cancelInvoice(String invoiceId, String reason);

    void createAuditLog(String action,
                        String entityName,
                        String entityId,
                        String payload,
                        String result,
                        String errorMsg);

    final class RepositoryExtensionNotes {
        private RepositoryExtensionNotes() {}
    }
}
