package com.einvoicehub.core.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

/** request đăng ký hoặc cập nhật dải ký hiệu, mẫu số hóa đơn. */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EinvStoreSerialRequest {

    @JsonProperty("ID")
    private String id; // UUID

    @NotBlank(message = "StoreProviderID không được để trống")
    @JsonProperty("StoreProviderID")
    private String storeProviderId;

    @NotNull(message = "InvoiceTypeID không được để trống")
    @JsonProperty("InvoiceTypeID")
    private Integer invoiceTypeId;

    @JsonProperty("ProviderSerialID")
    private String providerSerialId;

    @NotBlank(message = "Mẫu số (InvoiceForm) không được để trống")
    @JsonProperty("InvoiceForm")
    private String invoiceForm;

    @NotBlank(message = "Ký hiệu (InvoiceSerial) không được để trống")
    @JsonProperty("InvoiceSerial")
    private String invoiceSerial;

    @JsonProperty("StartDate")
    private LocalDateTime startDate;

    @JsonProperty("Status")
    private Integer status;
}