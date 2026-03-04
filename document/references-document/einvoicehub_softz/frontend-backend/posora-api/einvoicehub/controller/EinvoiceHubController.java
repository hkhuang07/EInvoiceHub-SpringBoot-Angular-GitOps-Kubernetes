package vn.softz.app.einvoicehub.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import vn.softz.app.einvoicehub.dto.EinvoiceHubResponse;
import vn.softz.app.einvoicehub.dto.PaginationResponseDto;
import vn.softz.app.einvoicehub.dto.request.SignInvoicesRequest;
import vn.softz.app.einvoicehub.dto.request.GetInvoicesRequest;
import vn.softz.app.einvoicehub.dto.EinvoiceHubRequest;
import vn.softz.app.einvoicehub.service.EinvoiceHubService;
import vn.softz.app.einvoicehub.dto.response.ListInvoicesResponse;
import vn.softz.app.einvoicehub.dto.PaginationRequestDto;

import java.util.List;

@RestController
@RequestMapping("/v1/einvoice/hub")
@RequiredArgsConstructor
public class EinvoiceHubController {

    private final EinvoiceHubService hubService;

    @PostMapping("/submit")
    public Object submitInvoice(@Valid @RequestBody EinvoiceHubRequest<Object> request) {
        return hubService.submitInvoice(request.getData(), 100);
    }

    @PostMapping("/submit-type-101")
    public Object submitInvoiceType101(@Valid @RequestBody EinvoiceHubRequest<Object> request) {
        return hubService.submitInvoice(request.getData(), 101);
    }

    @PostMapping("/submit-type-102")
    public Object submitInvoiceType102(@Valid @RequestBody EinvoiceHubRequest<Object> request) {
        return hubService.submitInvoice(request.getData(), 102);
    }

    @PostMapping("/sign")
    public Object signInvoices(@Valid @RequestBody EinvoiceHubRequest<List<SignInvoicesRequest>> request) {
        return hubService.signInvoices(request.getData());
    }

    @PostMapping("/detail")
    public Object getInvoices(@Valid @RequestBody EinvoiceHubRequest<List<GetInvoicesRequest>> request) {
        return hubService.getInvoices(request.getData());
    }

    @PostMapping("/list")
    public EinvoiceHubResponse<PaginationResponseDto<ListInvoicesResponse>> listInvoices(@Valid @RequestBody EinvoiceHubRequest<PaginationRequestDto> request) {
        PaginationResponseDto<ListInvoicesResponse> results = hubService.listInvoices(request.getData());
        return EinvoiceHubResponse.success("requestId", results);
    }
}
