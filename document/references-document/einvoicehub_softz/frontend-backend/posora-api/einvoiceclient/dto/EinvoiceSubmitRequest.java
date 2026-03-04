package vn.softz.app.einvoiceclient.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EinvoiceSubmitRequest {

    private String invoiceId;

    private Integer submitType;
}
