package vn.softz.app.einvoicehub.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import vn.softz.app.einvoicehub.domain.entity.EinvProviderEntity;
import vn.softz.app.einvoicehub.service.EinvProviderService;
import vn.softz.core.dto.ApiRequestDto;
import vn.softz.core.dto.ApiResponseDto;

import java.util.List;

@RestController
@RequestMapping("/v1/einvoice/provider")
@RequiredArgsConstructor
public class EinvProviderController {

    private final EinvProviderService service;

    @PostMapping("/list")
    public ApiResponseDto<List<EinvProviderEntity>> getList(@RequestBody @Valid ApiRequestDto<?> request) {
        List<EinvProviderEntity> list = service.getActiveProviders();
        return ApiResponseDto.success(list);
    }
}
