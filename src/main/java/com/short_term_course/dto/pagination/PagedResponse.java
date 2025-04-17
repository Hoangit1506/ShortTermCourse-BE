package com.short_term_course.dto.pagination;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PagedResponse<T> {
    List<T> content;       // Danh sách item
    int pageNumber;              // Trang hiện tại (zero-based)
    int pageSize;              // Kích thước trang
    long totalElements;    // Tổng số phần tử
    int totalPages;        // Tổng số trang
    boolean last;          // Có phải trang cuối cùng không

    public PagedResponse(List<T> content, Pageable pageable, long total) {
        this.content = content;
        this.pageNumber = pageable.getPageNumber();
        this.pageSize = pageable.getPageSize();
        this.totalElements = total;
        this.totalPages = (int) Math.ceil((double) total / pageable.getPageSize());
        this.last = (pageNumber + 1) >= totalPages;
    }
}
