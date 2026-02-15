package com.einvoicehub.core.dto;

import lombok.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaginationResponseDto<T> {
    private long offset;
    private long limit;
    private long total;
    private long count;
    private List<T> items;
}