package com.einvoicehub.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EinvMerchantRequest {

    @NotBlank(message = "Mã số thuế không được để trống")
    @Size(max = 20, message = "Mã số thuế không quá 20 ký tự")
    @JsonProperty("TaxCode")
    private String taxCode;

    @NotBlank(message = "Tên công ty không được để trống")
    @Size(max = 255, message = "Tên công ty không quá 255 ký tự")
    @JsonProperty("CompanyName")
    private String companyName;

    @Size(max = 100)
    @JsonProperty("ShortName")
    private String shortName;

    @JsonProperty("Address")
    private String address;

    @Size(max = 100)
    @JsonProperty("District")
    private String district;

    @Size(max = 100)
    @JsonProperty("City")
    private String city;

    @Email(message = "Email không đúng định dạng")
    @JsonProperty("Email")
    private String email;

    @Size(max = 20)
    @JsonProperty("Phone")
    private String phone;

    @JsonProperty("RepresentativeName")
    private String representativeName;

    @JsonProperty("RepresentativeTitle")
    private String representativeTitle;

    @Size(max = 10)
    @JsonProperty("TaxAuthorityCode")
    private String taxAuthorityCode;
}