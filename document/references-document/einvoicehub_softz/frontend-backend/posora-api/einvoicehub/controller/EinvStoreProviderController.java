package vn.softz.app.einvoicehub.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import vn.softz.app.einvoicehub.dto.EinvStoreProviderDto;
import vn.softz.app.einvoicehub.dto.EinvStoreProviderRequest;
import vn.softz.app.einvoicehub.dto.EinvValidationResult;
import vn.softz.app.einvoicehub.service.EinvStoreProviderService;
import vn.softz.core.dto.ApiRequestDto;
import vn.softz.core.dto.ApiResponseDto;

@RestController
@RequestMapping("/v1/einvoice/config")
@RequiredArgsConstructor
public class EinvStoreProviderController {

    private final EinvStoreProviderService service;

    @PostMapping("/detail")
    public ApiResponseDto<EinvStoreProviderDto> getConfig(@RequestBody @Valid ApiRequestDto<?> request) {
        return service.getConfig()
                .map(ApiResponseDto::success)
                .orElse(ApiResponseDto.success(null));
    }

    @PostMapping("/save")
    public ApiResponseDto<EinvStoreProviderDto> saveConfig(@RequestBody @Valid ApiRequestDto<EinvStoreProviderRequest> request) {
        EinvStoreProviderDto saved = service.saveConfig(request.getData());
        return ApiResponseDto.success(saved);
    }

    @PostMapping("/deactivate")
    public ApiResponseDto<EinvValidationResult> deactivate(@RequestBody @Valid ApiRequestDto<?> request) {
        EinvValidationResult result = service.deactivate();
        return ApiResponseDto.success(result);
    }

    @PostMapping("/validate")
    public ApiResponseDto<EinvValidationResult> validateConfig(@RequestBody @Valid ApiRequestDto<EinvStoreProviderRequest> request) {
        EinvValidationResult result = service.validateConfig(request.getData());
        return ApiResponseDto.success(result);
    }
}
