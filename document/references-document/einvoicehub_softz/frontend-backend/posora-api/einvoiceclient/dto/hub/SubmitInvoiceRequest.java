package vn.softz.app.einvoiceclient.dto.hub;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubmitInvoiceRequest {

    @JsonProperty("SubmitInvoiceType")
    private Integer submitInvoiceType;

    @JsonProperty("PartnerInvoiceID")
    private String partnerInvoiceId;

    @JsonProperty("InvoiceTypeID")
    private Integer invoiceTypeId;

    @JsonProperty("InvoiceDate")
    private String invoiceDate;

    @JsonProperty("InvoiceForm")
    private String invoiceForm;

    @JsonProperty("InvoiceSeries")
    private String invoiceSeries;

    @JsonProperty("PaymentMethodID")
    private Integer paymentMethodId;

    @JsonProperty("BuyerTaxCode")
    private String buyerTaxCode;

    @JsonProperty("BuyerCompany")
    private String buyerCompany;

    @JsonProperty("BuyerName")
    private String buyerName;

    @JsonProperty("BuyerAddress")
    private String buyerAddress;

    @JsonProperty("BuyerIDNo")
    private String buyerIdNo;

    @JsonProperty("BuyerMobile")
    private String buyerMobile;

    @JsonProperty("BuyerBankAccount")
    private String buyerBankAccount;

    @JsonProperty("BuyerBankName")
    private String buyerBankName;

    @JsonProperty("BuyerBudgetCode")
    private String buyerBudgetCode;

    @JsonProperty("ReceiverTypeID")
    private Integer receiverTypeId;

    @JsonProperty("ReceiverEmail")
    private String receiverEmail;

    @JsonProperty("CurrencyCode")
    private String currencyCode;

    @JsonProperty("ExchangeRate")
    private Double exchangeRate;

    @JsonProperty("Notes")
    private String notes;

    @JsonProperty("Details")
    private List<SubmitInvoiceDetailRequest> details;
}
