package vn.softz.app.einvoicehub.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import vn.softz.app.einvoicehub.domain.entity.EinvInvoiceStatusEntity;
import vn.softz.app.einvoicehub.service.EinvInvoiceStatusService;
import vn.softz.core.dto.ApiRequestDto;
import vn.softz.core.dto.ApiResponseDto;

import java.util.List;

@RestController
@RequestMapping("/v1/einvoice/invoice-status")
@RequiredArgsConstructor
public class EinvInvoiceStatusController {

    private final EinvInvoiceStatusService service;

    @PostMapping("/list")
    public ApiResponseDto<List<EinvInvoiceStatusEntity>> getList(@RequestBody @Valid ApiRequestDto<?> request) {
        List<EinvInvoiceStatusEntity> list = service.getAll();
        return ApiResponseDto.success(list);
    }
}
