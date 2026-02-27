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
public class EinvUnitDto {

    @JsonProperty("Code")
    private String code; // Mã đơn vị tính (DVT01, DVT02...)

    @JsonProperty("Name")
    private String name;
}