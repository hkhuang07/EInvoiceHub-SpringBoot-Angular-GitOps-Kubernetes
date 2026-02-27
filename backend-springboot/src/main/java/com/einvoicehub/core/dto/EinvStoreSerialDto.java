package com.einvoicehub.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EinvStoreSerialDto {

    @JsonProperty("ID")
    private String id; // UUID

    @JsonProperty("StoreProviderID")
    private String storeProviderId;

    @JsonProperty("InvoiceTypeID")
    private Integer invoiceTypeId;

    @JsonProperty("InvoiceTypeName")
    private String invoiceTypeName;

    @JsonProperty("ProviderSerialID")
    private String providerSerialId;

    @JsonProperty("InvoiceForm")
    private String invoiceForm;

    @JsonProperty("InvoiceSerial")
    private String invoiceSerial;

    @JsonProperty("StartDate")
    private LocalDateTime startDate;

    @JsonProperty("Status")
    private Integer status;
}