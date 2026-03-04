package com.einvoicehub.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysUserDto {

    @JsonProperty("ID")
    private Long id;

    @JsonProperty("Username")
    private String name;

    @JsonProperty("FullName")
    private String fullName;

    @JsonProperty("IsActive")
    private Boolean isActive;
}