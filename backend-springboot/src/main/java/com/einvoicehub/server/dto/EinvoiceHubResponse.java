package com.einvoicehub.server.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EinvoiceHubResponse<T> {

    @JsonProperty("request_id")
    private String requestId;

    @JsonProperty("success")
    private Boolean success;

    @JsonProperty("error_code")
    private String errorCode;

    @JsonProperty("message")
    private String message;

    @JsonProperty("data")
    private T data;

    @JsonProperty("timestamp")
    private Long timestamp;

    public static <T> EinvoiceHubResponse<T> success(T data) {
        return EinvoiceHubResponse.<T>builder()
                .success(true)
                .data(data)
                .timestamp(System.currentTimeMillis())
                .build();
    }

    public static <T> EinvoiceHubResponse<T> success(String requestId, T data) {
        return EinvoiceHubResponse.<T>builder()
                .requestId(requestId)
                .success(true)
                .data(data)
                .timestamp(System.currentTimeMillis())
                .build();
    }

    public static <T> EinvoiceHubResponse<T> error(String errorCode, String message) {
        return EinvoiceHubResponse.<T>builder()
                .success(false)
                .errorCode(errorCode)
                .message(message)
                .timestamp(System.currentTimeMillis())
                .build();
    }

    public static <T> EinvoiceHubResponse<T> error(String requestId, String errorCode, String message) {
        return EinvoiceHubResponse.<T>builder()
                .requestId(requestId)
                .success(false)
                .errorCode(errorCode)
                .message(message)
                .timestamp(System.currentTimeMillis())
                .build();
    }
}