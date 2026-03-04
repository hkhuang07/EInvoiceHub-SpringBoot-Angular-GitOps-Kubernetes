package vn.softz.app.einvoicehub.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@SuperBuilder
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
        return limit == 0 ? 0 : (offset / limit);
    }
}

