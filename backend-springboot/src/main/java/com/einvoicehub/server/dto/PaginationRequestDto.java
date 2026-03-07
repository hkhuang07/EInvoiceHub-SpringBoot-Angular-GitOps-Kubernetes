package com.einvoicehub.server.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaginationRequestDto {

    @Builder.Default
    @Min(value = 0, message = "Offset không được âm")
    @JsonProperty("offset")
    private Integer offset = 0;

    @Builder.Default
    @Min(value = 1, message = "Limit tối thiểu là 1")
    @Max(value = 500, message = "Limit tối đa là 500 để đảm bảo hiệu năng")
    @JsonProperty("limit")
    private Integer limit = 20;

    @Builder.Default
    @JsonProperty("filters")
    private Map<String, Object> filters = new HashMap<>();

    @JsonProperty("sort_field")
    private String sortField;

    @Builder.Default
    @JsonProperty("sort_direction")
    private String sortDirection = "desc";

    public int getPageNumber() {
        return (limit == null || limit <= 0) ? 0 : (offset / limit);
    }
}