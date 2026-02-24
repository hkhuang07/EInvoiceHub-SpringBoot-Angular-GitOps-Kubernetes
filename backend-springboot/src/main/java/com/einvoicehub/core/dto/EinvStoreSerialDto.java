package com.einvoicehub.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EinvStoreSerialDto {

    @JsonProperty("ID")
    private Long id;

    @JsonProperty("MerchantID")
    private Long merchantId;

    @JsonProperty("InvoiceTypeID")
    private Long invoiceTypeId;

    @JsonProperty("InvoiceTypeName")
    private String invoiceTypeName;

    @JsonProperty("InvoiceForm")
    private String invoiceForm;

    @JsonProperty("InvoiceSerial")
    private String invoiceSerial;

    @JsonProperty("CurrentNumber")
    private Integer currentNumber;

    @JsonProperty("MaxNumber")
    private Integer maxNumber;

    @JsonProperty("ProviderSerialID")
    private String providerSerialId;

    @JsonProperty("IsActive")
    private Boolean isActive;

    @JsonProperty("StartDate")
    private String startDate;
}