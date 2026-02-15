package com.einvoicehub.core.dto;

import jakarta.validation.Valid;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EinvoiceHubRequest<T> {

    private String requestDateTime;
    private String requestId;
    private String userAgent;
    @Valid
    private T data;
}
