package com.einvoicehub.server.dto.response;

import com.einvoicehub.server.dto.EinvInvoiceDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

/**Response cho API GetInvoices (chế độ danh sách / phân trang)*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ListInvoicesResponse {

    @JsonProperty("invoices")
    private List<EinvInvoiceDto> invoices;

    @JsonProperty("total_elements")
    private Long totalElements;

    @JsonProperty("total_pages")
    private Integer totalPages;

    @JsonProperty("current_page")
    private Integer currentPage;

    @JsonProperty("page_size")
    private Integer pageSize;

    @JsonProperty("has_next")
    private Boolean hasNext;

    @JsonProperty("has_previous")
    private Boolean hasPrevious;

    @JsonProperty("synced_count")
    private Integer syncedCount;

    @JsonProperty("message")
    private String message;

    public static ListInvoicesResponse of(List<EinvInvoiceDto> invoices,
                                          long totalElements, int totalPages,
                                          int currentPage, int pageSize) {
        return ListInvoicesResponse.builder()
                .invoices(invoices)
                .totalElements(totalElements)
                .totalPages(totalPages)
                .currentPage(currentPage)
                .pageSize(pageSize)
                .hasNext(currentPage < totalPages - 1)
                .hasPrevious(currentPage > 0)
                .build();
    }

    public static ListInvoicesResponse empty() {
        return ListInvoicesResponse.builder()
                .invoices(List.of())
                .totalElements(0L)
                .totalPages(0)
                .currentPage(0)
                .pageSize(0)
                .hasNext(false)
                .hasPrevious(false)
                .build();
    }
}