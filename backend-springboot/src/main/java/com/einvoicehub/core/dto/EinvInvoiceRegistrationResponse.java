package com.einvoicehub.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EinvInvoiceRegistrationResponse {

    @JsonProperty("ID")
    private Long id;

    @JsonProperty("MerchantID")
    private Long merchantId;

    @JsonProperty("MerchantName")
    private String merchantName;

    @JsonProperty("RegistrationNumber")
    private String registrationNumber;

    @JsonProperty("FromNumber")
    private Long fromNumber;

    @JsonProperty("ToNumber")
    private Long toNumber;

    @JsonProperty("Quantity")
    private Long quantity;

    @JsonProperty("CurrentNumber")
    private Long currentNumber;

    @JsonProperty("EffectiveDate")
    private LocalDate effectiveDate;

    @JsonProperty("ExpirationDate")
    private LocalDate expirationDate;

    // --- Flattened Status Info ---
    @JsonProperty("StatusID")
    private Integer statusId;

    @JsonProperty("StatusName")
    private String statusName;

    @JsonProperty("IssuedBy")
    private String issuedBy;

    @JsonProperty("CreatedAt")
    private LocalDateTime createdAt;
}