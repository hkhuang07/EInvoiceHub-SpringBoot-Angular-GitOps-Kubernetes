package vn.softz.app.einvoicehub.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;


//status: 0=Chờ duyệt, 1=Đã duyệt, 8=Ngưng sử dụng*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EinvStoreSerialRequest {

    @NotBlank(message = "Provider ID is required")
    @Size(max = 36)
    @JsonProperty("provider_id")
    private String providerId;

    // Loại hóa đơn áp dụng dải này: 1=GTGT, 2=Bán hàng...
    @JsonProperty("invoice_type_id")
    private Byte invoiceTypeId;

    @Size(max = 50, message = "Provider serial ID must not exceed 50 characters")
    @JsonProperty("provider_serial_id")
    private String providerSerialId;

    @Size(max = 20, message = "Invoice form must not exceed 20 characters")
    @JsonProperty("invoice_form")
    private String invoiceForm;

    @Size(max = 20, message = "Invoice serial must not exceed 20 characters")
    @JsonProperty("invoice_serial")
    private String invoiceSerial;

    @JsonProperty("start_date")
    private LocalDateTime startDate;

    @Min(value = 0, message = "Status must be 0 (Pending), 1 (Approved), or 8 (Inactive)")
    @Max(value = 8, message = "Status must be 0 (Pending), 1 (Approved), or 8 (Inactive)")
    @JsonProperty("status")
    private Byte status;
}