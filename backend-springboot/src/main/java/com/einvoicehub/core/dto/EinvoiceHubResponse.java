package com.einvoicehub.core.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EinvoiceHubResponse<T> {

    private String responseCode;
    private String requestId;
    private Integer status;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String responseDesc;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public static <T> EinvoiceHubResponse<T> success(String requestId, T data) {
        return EinvoiceHubResponse.<T>builder()
                .responseCode("200")
                .requestId(requestId)
                .status(0)
                .data(data)
                .build();
    }

    public static <T> EinvoiceHubResponse<T> error(String requestId, String code, String message) {
        return EinvoiceHubResponse.<T>builder()
                .responseCode(code)
                .requestId(requestId)
                .status(1)
                .responseDesc(message)
                .build();
    }

    public static <T> EinvoiceHubResponse<T> badRequest(String requestId, String message) {
        return error(requestId, "400", message);
    }

    public static <T> EinvoiceHubResponse<T> internalError(String requestId, String message) {
        return error(requestId, "500", message);
    }

    public static <T> EinvoiceHubResponse<T> providerError(String requestId, String message) {
        return error(requestId, "502", message);
    }
}