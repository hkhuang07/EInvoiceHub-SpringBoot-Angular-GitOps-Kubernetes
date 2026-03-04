package vn.softz.app.einvoicehub.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PaginationResponseDto<T> {
    private long offset;
    private long limit;
    private long total;
    private long count;
    private List<T> items;
}

