package com.einvoicehub.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaginationResponseDto<T> {

    @JsonProperty("Offset")
    private long offset;

    @JsonProperty("Limit")
    private long limit;

    @JsonProperty("Total")
    private long total;

    @JsonProperty("Count")
    private long count;

    @JsonProperty("Items")
    private List<T> items;
}