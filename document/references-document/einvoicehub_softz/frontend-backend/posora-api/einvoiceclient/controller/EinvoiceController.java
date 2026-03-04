package vn.softz.app.einvoiceclient.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;
import vn.softz.app.einvoiceclient.dto.EinvoiceSubmitRequest;
import vn.softz.app.einvoiceclient.service.EinvoiceService;
import vn.softz.core.dto.ApiRequestDto;
import vn.softz.app.einvoiceclient.dto.SignInvoicesRequest;
import java.util.List;
import vn.softz.app.einvoiceclient.dto.GetInvoicesRequest;

@RestController
@RequestMapping("/v1/einvoice")
@RequiredArgsConstructor
public class EinvoiceController {
    
    private final EinvoiceService einvoiceService;

    @PostMapping("/submit")
    public Object submitInvoice(
            @Valid @RequestBody ApiRequestDto<EinvoiceSubmitRequest> request) {
        var result = einvoiceService.submitInvoice(request.getData());
        return result;
    }

    @PostMapping("/sign")
    public Object signInvoices(
            @Valid @RequestBody ApiRequestDto<List<SignInvoicesRequest>> request) {
        var result = einvoiceService.signInvoices(request.getData());
        return result;
    }

    @PostMapping("/detail")
    public Object getInvoiceDetail(
            @Valid @RequestBody ApiRequestDto<List<GetInvoicesRequest>> request) {
        var result = einvoiceService.getInvoiceDetail(request.getData());
        return result;
    }
}