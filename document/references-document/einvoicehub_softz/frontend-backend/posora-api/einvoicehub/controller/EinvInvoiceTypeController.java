package vn.softz.app.einvoicehub.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import vn.softz.app.einvoicehub.domain.entity.EinvInvoiceTypeEntity;
import vn.softz.app.einvoicehub.service.EinvInvoiceTypeService;
import vn.softz.core.dto.ApiRequestDto;
import vn.softz.core.dto.ApiResponseDto;

import java.util.List;

@RestController
@RequestMapping("/v1/einvoice/invoice-type")
@RequiredArgsConstructor
public class EinvInvoiceTypeController {

    private final EinvInvoiceTypeService service;

    @PostMapping("/list")
    public ApiResponseDto<List<EinvInvoiceTypeEntity>> getList(@RequestBody @Valid ApiRequestDto<?> request) {
        List<EinvInvoiceTypeEntity> list = service.getAll();
        return ApiResponseDto.success(list);
    }
}
