package vn.softz.app.einvoicehub.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaginationRequestDto {

    @Builder.Default
    @JsonProperty("page")
    private int page = 1;

    @Builder.Default
    @JsonProperty("size")
    private int size = 20;

    @JsonProperty("sort_field")
    private String sortField;

    @Builder.Default
    @JsonProperty("sort_direction")
    private String sortDirection = "DESC";

    @JsonProperty("filters")
    private Map<String, Object> filters;

    public Object getFilter(String key) {
        return filters != null ? filters.get(key) : null;
    }

    public String getFilterAsString(String key) {
        Object value = getFilter(key);
        return value != null ? value.toString() : null;
    }

    public Integer getFilterAsInteger(String key) {
        Object value = getFilter(key);
        if (value == null) return null;
        try {
            return Integer.parseInt(value.toString());
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    public int getZeroBasedPage() {
        return Math.max(0, page - 1);
    }

    public int getSafeSize() {
        return Math.min(Math.max(1, size), 200);
    }
}
