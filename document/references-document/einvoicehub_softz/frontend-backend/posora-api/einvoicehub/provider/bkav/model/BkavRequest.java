package vn.softz.app.einvoicehub.provider.bkav.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BkavRequest<T> {

    @JsonProperty("CmdType")
    private int cmdType;

    @JsonProperty("CommandObject")
    private T commandObject;

    public static BkavRequest<java.util.List<BkavInvoice.RequestData>> createInvoice(
            int cmdType, java.util.List<BkavInvoice.RequestData> invoices) {
        return BkavRequest.<java.util.List<BkavInvoice.RequestData>>builder()
                .cmdType(cmdType)
                .commandObject(invoices)
                .build();
    }

    public static BkavRequest<String> byGuid(int cmdType, String invoiceGuid) {
        return BkavRequest.<String>builder()
                .cmdType(cmdType)
                .commandObject(invoiceGuid)
                .build();
    }

    public static BkavRequest<java.util.List<BkavInvoice.RequestData>> getInvoice(
            int cmdType, Long partnerInvoiceId) {
        BkavInvoice.RequestData data = BkavInvoice.RequestData.builder()
                .partnerInvoiceID(partnerInvoiceId)
                .build();
        return BkavRequest.<java.util.List<BkavInvoice.RequestData>>builder()
                .cmdType(cmdType)
                .commandObject(java.util.List.of(data))
                .build();
    }

    public static BkavRequest<String> lookupCompany(int cmdType, String taxCode) {
        return BkavRequest.<String>builder()
                .cmdType(cmdType)
                .commandObject(taxCode)
                .build();
    }
}
