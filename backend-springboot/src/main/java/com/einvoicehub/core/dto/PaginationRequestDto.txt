package com.einvoicehub.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaginationRequestDto {

    @Builder.Default
    @JsonProperty("Offset")
    private Integer offset = 0;

    @Builder.Default
    @JsonProperty("Limit")
    private Integer limit = 20;

    @Builder.Default
    @JsonProperty("Filters")
    private Map<String, Object> filters = new HashMap<>();

    @JsonProperty("SortField")
    private String sortField;

    @Builder.Default
    @JsonProperty("SortDirection")
    private String sortDirection = "desc";

    public int getPageNumber() {
        return (limit == null || limit <= 0) ? 0 : (offset / limit);
    }
}