package com.einvoicehub.server.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MerchantDto {

    @JsonProperty("id")
    private Long id;

    @NotBlank(message = "Mã định danh Tenant (tenant_id) là bắt buộc")
    @Size(max = 36, message = "tenant_id không được vượt quá 36 ký tự")
    @JsonProperty("tenant_id")
    private String tenantId;

    @NotBlank(message = "Tên doanh nghiệp không được để trống")
    @Size(max = 255, message = "Tên doanh nghiệp quá dài")
    @JsonProperty("company_name")
    private String companyName;

    @NotBlank(message = "Mã số thuế là bắt buộc")
    @Size(max = 20, message = "Mã số thuế không hợp lệ")
    @JsonProperty("tax_code")
    private String taxCode;

    @Builder.Default
    @JsonProperty("is_active")
    private Boolean isActive = true;

    @JsonProperty("created_date")
    private LocalDateTime createdDate;

    @JsonProperty("updated_date")
    private LocalDateTime updatedDate;

    @JsonProperty("created_by")
    private String createdBy;

    @JsonProperty("created_by")
    private String updateBy;
}