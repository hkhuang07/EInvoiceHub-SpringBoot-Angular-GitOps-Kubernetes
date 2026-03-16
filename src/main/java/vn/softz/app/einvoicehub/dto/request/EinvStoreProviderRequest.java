package vn.softz.app.einvoicehub.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EinvStoreProviderRequest {

    @NotBlank(message = "Provider ID is required")
    @Size(max = 36)
    @JsonProperty("provider_id")
    private String providerId;

    @Min(value = 0, message = "Sign type must be 0 (Token), 1 (HSM)")
    @Max(value = 2, message = "Sign type must be 0 (Token), 1 (HSM)")
    @JsonProperty("sign_type")
    private Byte signType;

    @JsonProperty("is_two_step_signing")
    private Boolean isTwoStepSigning;

    /**BKAV: PartnerGUID cấp khi đăng ký
     → Mapper: bkavPartnerGuid → partner_id
     */
    @Size(max = 200, message = "BKAV Partner GUID must not exceed 200 characters")
    @JsonProperty("bkav_partner_guid")
    private String bkavPartnerGuid;
    /**BKAV: PartnerToken dùng để xác thực API.
     → Mapper: bkavPartnerToken → partner_token
     */
    @Size(max = 200, message = "BKAV Partner Token must not exceed 200 characters")
    @JsonProperty("bkav_partner_token")
    private String bkavPartnerToken;

    /**Mobifone: Username tài khoản dịch vụ.
     → Mapper: mobifoneUsername → partner_usr*/
    @Size(max = 200, message = "Mobifone username must not exceed 200 characters")
    @JsonProperty("mobifone_username")
    private String mobifoneUsername;
    /**Mobifone: Password tài khoản dịch vụ.
     → Mapper: mobifonePassword → partner_pwd*/
    @Size(max = 200, message = "Mobifone password must not exceed 200 characters")
    @JsonProperty("mobifone_password")
    private String mobifonePassword;
    /**Mobifone: Mã số thuế đơn vị (dùng làm account identifier).
     → Mapper: mobifoneTaxCode → tax_code*/
    @Size(max = 200, message = "Tax code must not exceed 200 characters")
    @JsonProperty("mobifone_tax_code")
    private String mobifoneTaxCode;

    /**MISA: app_id cấp khi đăng ký ứng dụng.*/
    @Size(max = 100, message = "App ID must not exceed 100 characters")
    @JsonProperty("app_id")
    private String appId;


    /**VNPT: tiền tố Fkey (FKey prefix dành riêng cho VNPT).*/
    @Size(max = 50, message = "Fkey prefix must not exceed 50 characters")
    @JsonProperty("fkey_prefix")
    private String fkeyPrefix;
    /**VNPT: integration URL riêng (override mặc định của provider*/
    @Size(max = 200, message = "Integration URL must not exceed 200 characters")
    @JsonProperty("integration_url")
    private String integrationUrl;
    /**Username API dịch vụ (dùng cho VNPT / các NCC khác).*/
    @Size(max = 100, message = "Username service must not exceed 100 characters")
    @JsonProperty("username_service")
    private String usernameService;
    /**Password API dịch vụ.*/
    @Size(max = 255, message = "Password service must not exceed 255 characters")
    @JsonProperty("password_service")
    private String passwordService;
}