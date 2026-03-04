package vn.softz.app.einvoicehub.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EinvStoreSerialDto {
    private String id;
    private String tenantId;
    private String storeId;
    private String providerId;
    private Integer invoiceTypeId;
    private String invoiceTypeName;
    private String invoiceForm;
    private String invoiceSerial;
    private Instant startDate;
    private Integer status;
    private String providerSerialId;
    private Instant createdDate;
    private Instant updatedDate;
}
