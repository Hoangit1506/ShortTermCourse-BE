package com.short_term_course.dto.course;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateCourseRequest {
    String name;
    String description;
    String suitable;
    Double price;
    String content;
    String categoryId;
    String thumbnail;
    String promoVideo;  
}
