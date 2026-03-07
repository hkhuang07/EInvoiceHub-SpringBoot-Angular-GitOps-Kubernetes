package com.einvoicehub.server.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SignInvoiceResponse {

    @JsonProperty("total_requested")
    private Integer totalRequested;

    @JsonProperty("total_success")
    private Integer totalSuccess;

    @JsonProperty("total_failed")
    private Integer totalFailed;

    @JsonProperty("message")
    private String message;

    @JsonProperty("results")
    private List<SignInvoiceResult> results;
}