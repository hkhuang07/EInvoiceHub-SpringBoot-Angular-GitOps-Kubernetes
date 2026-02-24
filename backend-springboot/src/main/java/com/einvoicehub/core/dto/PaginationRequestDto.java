package com.einvoicehub.core.dto;

import lombok.*;
import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaginationRequestDto {

    @Builder.Default
    private Integer offset = 0;

    @Builder.Default
    private Integer limit = 20;

    @Builder.Default
    private Map<String, Object> filters = new HashMap<>();

    private String sortField;

    @Builder.Default
    private String sortDirection = "desc";

    public int getPageNumber() {
        return (limit == null || limit <= 0) ? 0 : (offset / limit);
    }
}