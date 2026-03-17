package vn.softz.app.einvoicehub.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaginationResponseDto<T> {

    @JsonProperty("content")
    private List<T> content;

    @JsonProperty("page")
    private int page;

    @JsonProperty("size")
    private int size;

    @JsonProperty("total_elements")
    private long totalElements;

    @JsonProperty("total_pages")
    private int totalPages;

    @JsonProperty("has_next")
    private boolean hasNext;

    @JsonProperty("has_previous")
    private boolean hasPrevious;


    public static <T> PaginationResponseDto<T> empty() {
        return PaginationResponseDto.<T>builder()
                .content(List.of())
                .page(1)
                .size(0)
                .totalElements(0L)
                .totalPages(0)
                .hasNext(false)
                .hasPrevious(false)
                .build();
    }

    public static <T> PaginationResponseDto<T> ofList(List<T> content) {
        int size = content == null ? 0 : content.size();
        return PaginationResponseDto.<T>builder()
                .content(content != null ? content : List.of())
                .page(1)
                .size(size)
                .totalElements(size)
                .totalPages(1)
                .hasNext(false)
                .hasPrevious(false)
                .build();
    }
}
