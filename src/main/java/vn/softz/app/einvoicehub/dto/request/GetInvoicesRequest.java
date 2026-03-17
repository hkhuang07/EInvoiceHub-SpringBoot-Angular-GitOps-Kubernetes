package vn.softz.app.einvoicehub.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**Request DTO dùng chung cho hai API:
 *  • GetInvoices       — lấy thông tin đầy đủ một hoặc danh sách hóa đơn (CmdType800)
 *  • GetStatusInvoices — tra cứu trạng thái hóa đơn (CmdType850 / CmdType801)*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetInvoicesRequest {

    @NotBlank(message = "Tenant ID is required")
    @Size(max = 36, message = "Tenant ID must not exceed 36 characters")
    @JsonProperty("tenant_id")
    private String tenantId;

    @Size(max = 36, message = "Store ID must not exceed 36 characters")
    @JsonProperty("store_id")
    private String storeId;

    @Size(max = 36, message = "Provider ID must not exceed 36 characters")
    @JsonProperty("provider_id")
    private String providerId;

    @Min(value = 0, message = "ID type must be 0 or 1")
    @Max(value = 1, message = "ID type must be 0 or 1")
    @JsonProperty("id_type")
    @Builder.Default
    private Integer idType = 0;

    @Size(max = 30, message = "Maximum 30 invoices per request")
    @JsonProperty("invoice_ids")
    private List<String> invoiceIds;

    @JsonProperty("invoice_id")
    private String invoiceId;

    /**Bắt buộc đồng bộ trạng thái từ CQT/Provider về HUB.
     *   true  = luôn gọi Provider để cập nhật trạng thái mới nhất.
     *   false = trả kết quả từ DB nội bộ nếu status đã ổn định (∈ {2,6,8,9,10}).*/
    @Builder.Default
    @JsonProperty("force_sync")
    private Boolean forceSync = false;

    @Size(max = 50, message = "Partner invoice ID filter must not exceed 50 characters")
    @JsonProperty("partner_invoice_id")
    private String partnerInvoiceId;

    @Size(max = 50, message = "Invoice number filter must not exceed 50 characters")
    @JsonProperty("invoice_no")
    private String invoiceNo;

    @Size(max = 50, message = "Buyer tax code filter must not exceed 50 characters")
    @JsonProperty("buyer_tax_code")
    private String buyerTaxCode;

    @JsonProperty("status_id")
    private Byte statusId;

    /** 0 = Gốc | 2 = Điều chỉnh | 3 = Thay thế*/
    @Min(value = 0, message = "Reference type ID must be 0, 2, or 3")
    @Max(value = 3, message = "Reference type ID must be 0, 2, or 3")
    @JsonProperty("reference_type_id")
    private Byte referenceTypeId;

    @JsonProperty("from_date")
    private LocalDateTime fromDate;

    @JsonProperty("to_date")
    private LocalDateTime toDate;

    @Min(value = 0, message = "Page number must be >= 0")
    @Builder.Default
    @JsonProperty("page")
    private Integer page = 0;

    @Min(value = 1, message = "Page size must be >= 1")
    @Max(value = 100, message = "Page size must not exceed 100")
    @Builder.Default
    @JsonProperty("size")
    private Integer size = 20;

    @Builder.Default
    @JsonProperty("sort_by")
    private String sortBy = "invoice_date";

    @Builder.Default
    @JsonProperty("sort_direction")
    private String sortDirection = "DESC";

    public boolean isBatchLookup() {
        return invoiceIds != null && !invoiceIds.isEmpty();
    }


}