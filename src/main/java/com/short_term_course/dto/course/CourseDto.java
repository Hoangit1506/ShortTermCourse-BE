package com.short_term_course.dto.course;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourseDto {
    String id;
    String name;
    String description;
    String suitable;
    Double price;
    String content;
    String thumbnail;
    String promoVideo;   
    String categoryId;
    String categoryName;
}
