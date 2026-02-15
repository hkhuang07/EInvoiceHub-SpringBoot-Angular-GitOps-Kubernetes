package com.einvoicehub.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EinvMerchantResponse {

    @JsonProperty("ID")
    private Long id;

    @JsonProperty("TaxCode")
    private String taxCode;

    @JsonProperty("CompanyName")
    private String companyName;

    @JsonProperty("ShortName")
    private String shortName;

    @JsonProperty("Address")
    private String address;

    @JsonProperty("City")
    private String city;

    @JsonProperty("Email")
    private String email;

    @JsonProperty("Phone")
    private String phone;

    @JsonProperty("RepresentativeName")
    private String representativeName;

    // --- Enterprise Shell Fields (SaaS Info) ---
    @JsonProperty("SubscriptionPlan")
    private String subscriptionPlan;

    @JsonProperty("InvoiceQuota")
    private Integer invoiceQuota;

    @JsonProperty("InvoiceUsed")
    private Integer invoiceUsed;

    @JsonProperty("IsUsingHsm")
    private Boolean isUsingHsm;

    @JsonProperty("CreatedAt")
    private String createdAt;
}