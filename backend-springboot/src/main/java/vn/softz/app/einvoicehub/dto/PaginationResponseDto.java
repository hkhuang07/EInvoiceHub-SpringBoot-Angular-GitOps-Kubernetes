package vn.softz.app.einvoicehub.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaginationResponseDto<T> {

    @JsonProperty("offset")
    private long offset;

    @JsonProperty("limit")
    private long limit;

    @JsonProperty("total")
    private long total;

    @JsonProperty("count")
    private long count;

    @JsonProperty("items")
    private List<T> items;
}