package vn.softz.app.einvoicehub.provider.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceStatusResult {

    private boolean success;

    private String message;

    private Integer bkavStatusId;

    private Integer hubStatusId;

    private String taxAuthorityCode;

    private String invoiceLookupCode;

    public static InvoiceStatusResult error(String message) {
        return InvoiceStatusResult.builder()
                .success(false)
                .message(message)
                .build();
    }
}