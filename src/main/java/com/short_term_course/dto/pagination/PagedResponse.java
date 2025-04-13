package com.short_term_course.dto.pagination;

import lombok.*;
import lombok.experimental.FieldDefaults;

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
}
