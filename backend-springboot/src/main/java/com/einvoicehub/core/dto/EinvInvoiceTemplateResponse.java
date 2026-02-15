package com.einvoicehub.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EinvInvoiceTemplateResponse {

    @JsonProperty("ID")
    private Long id;

    @JsonProperty("MerchantID")
    private Long merchantId;

    @JsonProperty("MerchantName")
    private String merchantName;

    @JsonProperty("InvoiceTypeID")
    private Long invoiceTypeId;

    @JsonProperty("InvoiceTypeName")
    private String invoiceTypeName;

    @JsonProperty("RegistrationID")
    private Long registrationId;

    @JsonProperty("RegistrationNumber")
    private String registrationNumber;

    @JsonProperty("TemplateCode")
    private String templateCode;

    @JsonProperty("SymbolCode")
    private String symbolCode;

    @JsonProperty("CurrentNumber")
    private Integer currentNumber;

    @JsonProperty("MinNumber")
    private Integer minNumber;

    @JsonProperty("MaxNumber")
    private Integer maxNumber;

    @JsonProperty("IsActive")
    private Boolean isActive;

    @JsonProperty("ProviderID")
    private String providerId;

    @JsonProperty("CreatedAt")
    private LocalDateTime createdAt;

    @JsonProperty("UpdatedAt")
    private LocalDateTime updatedAt;
}