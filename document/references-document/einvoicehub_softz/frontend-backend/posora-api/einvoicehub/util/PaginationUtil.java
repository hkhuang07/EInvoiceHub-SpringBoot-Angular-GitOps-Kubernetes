package vn.softz.app.einvoicehub.util;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import vn.softz.app.einvoicehub.dto.PaginationRequestDto;
import vn.softz.app.einvoicehub.dto.PaginationResponseDto;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class PaginationUtil {
    
    public static Pageable createPageable(PaginationRequestDto request, String... defaultSortFields) {
        int size = Math.max(1, request.getLimit());
        int page = Math.max(0, request.getOffset() / size);

        String[] sortFields = Optional.ofNullable(request.getSortField())
                .map(s -> Arrays.stream(s.split(","))
                        .map(String::trim)
                        .filter(t -> !t.isEmpty())
                        .toArray(String[]::new))
                .filter(arr -> arr.length > 0)
                .orElse(defaultSortFields);

        if (sortFields == null || sortFields.length == 0) {
            return PageRequest.of(page, size);
        }

        Sort.Direction direction = "asc".equalsIgnoreCase(request.getSortDirection()) 
            ? Sort.Direction.ASC 
            : Sort.Direction.DESC;
        return PageRequest.of(page, size, Sort.by(direction, sortFields));
    }
    
    public static <T, R> PaginationResponseDto<R> buildResponse(
            PaginationRequestDto request, 
            Page<T> page, 
            List<R> items) {
        return PaginationResponseDto.<R>builder()
                .offset(request.getOffset())
                .limit(request.getLimit())
                .total(page.getTotalElements())
                .items(items)
                .count(items != null ? items.size() : 0)
                .build();
    }
    
    public static <T, R> PaginationResponseDto<R> buildResponse(
            PaginationRequestDto request, 
            Page<T> page, 
            Function<T, R> mapper) {
        List<R> items = page.getContent().stream()
                .map(mapper)
                .toList();
        
        return buildResponse(request, page, items);
    }
}
