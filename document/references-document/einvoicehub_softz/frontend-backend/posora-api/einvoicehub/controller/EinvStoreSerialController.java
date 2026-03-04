package vn.softz.app.einvoicehub.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import vn.softz.app.einvoicehub.dto.EinvStoreSerialDto;
import vn.softz.app.einvoicehub.dto.EinvStoreSerialRequest;
import vn.softz.app.einvoicehub.service.EinvStoreSerialService;
import vn.softz.core.dto.ApiRequestDto;
import vn.softz.core.dto.ApiResponseDto;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/einvoice/serial")
@RequiredArgsConstructor
public class EinvStoreSerialController {

    private final EinvStoreSerialService service;

    @PostMapping("/list")
    public ApiResponseDto<List<EinvStoreSerialDto>> getList(@RequestBody @Valid ApiRequestDto<?> request) {
        List<EinvStoreSerialDto> list = service.getList();
        return ApiResponseDto.success(list);
    }

    @PostMapping("/list-by-provider")
    public ApiResponseDto<List<EinvStoreSerialDto>> getListByProvider(@RequestBody @Valid ApiRequestDto<Map<String, String>> request) {
        String providerId = request.getData().get("providerId");
        List<EinvStoreSerialDto> list = service.getListByProvider(providerId);
        return ApiResponseDto.success(list);
    }

    @PostMapping("/detail")
    public ApiResponseDto<EinvStoreSerialDto> getDetail(@RequestBody @Valid ApiRequestDto<Map<String, String>> request) {
        String id = request.getData().get("id");
        return service.getById(id)
                .map(ApiResponseDto::success)
                .orElse(ApiResponseDto.success(null));
    }

    @PostMapping("/save")
    public ApiResponseDto<EinvStoreSerialDto> save(@RequestBody @Valid ApiRequestDto<EinvStoreSerialRequest> request) {
        EinvStoreSerialDto saved = service.save(request.getData());
        return ApiResponseDto.success(saved);
    }

    @PostMapping("/delete")
    public ApiResponseDto<Map<String, Object>> delete(@RequestBody @Valid ApiRequestDto<Map<String, String>> request) {
        String id = request.getData().get("id");
        service.delete(id);
        return ApiResponseDto.success(Map.of("message", "Deleted successfully"));
    }

    @PostMapping("/approve")
    public ApiResponseDto<EinvStoreSerialDto> approve(@RequestBody @Valid ApiRequestDto<Map<String, String>> request) {
        String id = request.getData().get("id");
        EinvStoreSerialDto approved = service.approve(id);
        return ApiResponseDto.success(approved);
    }

    @PostMapping("/deactivate")
    public ApiResponseDto<EinvStoreSerialDto> deactivate(@RequestBody @Valid ApiRequestDto<Map<String, String>> request) {
        String id = request.getData().get("id");
        EinvStoreSerialDto deactivated = service.deactivate(id);
        return ApiResponseDto.success(deactivated);
    }
}
