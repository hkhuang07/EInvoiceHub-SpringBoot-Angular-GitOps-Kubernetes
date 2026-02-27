package com.einvoicehub.core.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EinvoiceHubResponse<T> {

    @JsonProperty("ResponseCode")
    private String responseCode;

    @JsonProperty("RequestID")
    private String requestId;

    @JsonProperty("Status")
    private Integer status; // 0: Success, 1: Business Error, 2: System Error

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("ResponseDesc")
    private String responseDesc;

    @JsonProperty("Validation")
    private EinvValidationResult validation;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("Data")
    private T data;

    @Builder.Default
    @JsonProperty("ResponseTime")
    private LocalDateTime responseTime = LocalDateTime.now();

    public static <T> EinvoiceHubResponse<T> success(String requestId, T data) {
        return EinvoiceHubResponse.<T>builder()
                .responseCode("200")
                .requestId(requestId)
                .status(0)
                .validation(EinvValidationResult.ok("Success"))
                .data(data)
                .build();
    }

    public static <T> EinvoiceHubResponse<T> error(String requestId,String code,String message) {
        return EinvoiceHubResponse.<T>builder()
                .responseCode(code)
                .requestId(requestId)
                .status(1)
                .responseDesc(message)
                .validation(EinvValidationResult.fail(message))
                .build();
    }

    /*
    public static <T> EinvoiceHubResponse<T> validationError(String requestId, String message) {
        return EinvoiceHubResponse.<T>builder()
                .responseCode("400")
                .requestId(requestId)
                .status(1)
                .responseDesc(message)
                .validation(EinvValidationResult.fail(message))
                .build();
    }

    public static <T> EinvoiceHubResponse<T> providerError(String requestId, String message) {
        return EinvoiceHubResponse.<T>builder()
                .responseCode("502")
                .requestId(requestId)
                .status(1)
                .responseDesc("Lỗi từ nhà cung cấp dịch vụ")
                .validation(EinvValidationResult.fail(message))
                .build();
    }*/
}