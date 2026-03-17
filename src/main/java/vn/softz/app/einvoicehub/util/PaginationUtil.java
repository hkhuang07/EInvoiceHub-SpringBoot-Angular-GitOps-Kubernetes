package vn.softz.app.einvoicehub.util;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import vn.softz.app.einvoicehub.dto.PaginationRequestDto;
import vn.softz.app.einvoicehub.dto.PaginationResponseDto;

import java.util.List;


public final class PaginationUtil {

    private PaginationUtil() {}

    public static Pageable createPageable(PaginationRequestDto request,
                                          String defaultSortField) {
        int page = Math.max(0, request.getZeroBasedPage());
        int size = request.getSafeSize();

        String sortField = (request.getSortField() != null && !request.getSortField().isBlank())
                ? request.getSortField()
                : defaultSortField;

        Sort.Direction direction = "ASC".equalsIgnoreCase(request.getSortDirection())
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        Sort sort = (sortField != null && !sortField.isBlank())
                ? Sort.by(direction, sortField)
                : Sort.unsorted();

        return PageRequest.of(page, size, sort);
    }

    public static Pageable createPageable(PaginationRequestDto request,
                                          List<Sort.Order> sortOrders) {
        int page = Math.max(0, request.getZeroBasedPage());
        int size = request.getSafeSize();
        Sort sort = sortOrders != null && !sortOrders.isEmpty()
                ? Sort.by(sortOrders)
                : Sort.unsorted();
        return PageRequest.of(page, size, sort);
    }

    public static Pageable createPageable(PaginationRequestDto request) {
        return PageRequest.of(request.getZeroBasedPage(), request.getSafeSize());
    }

    public static <T> PaginationResponseDto<T> buildResponse(PaginationRequestDto request,
                                                              Page<?> page,
                                                              List<T> content) {
        return PaginationResponseDto.<T>builder()
                .content(content)
                .page(request.getPage())
                .size(request.getSafeSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .hasNext(page.hasNext())
                .hasPrevious(page.hasPrevious())
                .build();
    }

    public static <T> PaginationResponseDto<T> buildResponse(PaginationRequestDto request,
                                                              Page<T> page) {
        return buildResponse(request, page, page.getContent());
    }
}
