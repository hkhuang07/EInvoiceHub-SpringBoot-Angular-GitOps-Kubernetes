package com.einvoicehub.core.dto.request;

import com.einvoicehub.core.dto.EinvoiceHubRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Request đăng ký hoặc cập nhật dải ký hiệu, mẫu số hóa đơn.
 * Sử dụng StoreProviderID để liên kết chặt chẽ với cấu hình tích hợp.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EinvStoreSerialRequest {

    @JsonProperty("ID")
    private String id; // UUID bản ghi

    @NotBlank(message = "StoreProviderID không được để trống")
    @JsonProperty("StoreProviderID")
    private String storeProviderId; // Tham chiếu đến einv_store_provider.id

    @NotNull(message = "InvoiceTypeID không được để trống")
    @JsonProperty("InvoiceTypeID")
    private Integer invoiceTypeId; // 1: GTGT, 2: Bán hàng...

    @JsonProperty("ProviderSerialID")
    private String providerSerialId; // ID dải số do NCC cấp (nếu có)

    @NotBlank(message = "Mẫu số (InvoiceForm) không được để trống")
    @JsonProperty("InvoiceForm")
    private String invoiceForm; // VD: 1/001

    @NotBlank(message = "Ký hiệu (InvoiceSerial) không được để trống")
    @JsonProperty("InvoiceSerial")
    private String invoiceSerial; // VD: C25TBB

    @JsonProperty("StartDate")
    private LocalDateTime startDate;

    @JsonProperty("Status")
    private Integer status; // 1: Đang sử dụng, 0: Ngừng sử dụng
}