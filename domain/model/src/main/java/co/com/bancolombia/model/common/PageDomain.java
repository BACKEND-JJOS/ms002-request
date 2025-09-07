package co.com.bancolombia.model.common;

import lombok.*;

import java.util.List;

@Getter
@Builder
public class PageDomain<T> {
    private final List<T> content;
    private final int page;
    private final int size;
    private final long totalElements;
    private final int totalPages;
}
