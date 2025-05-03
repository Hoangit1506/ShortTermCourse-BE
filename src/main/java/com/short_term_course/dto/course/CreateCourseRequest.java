package com.short_term_course.dto.course;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateCourseRequest {
    @NotBlank(message = "Name is required")
    String name;

    String description;
    String suitable;

    @NotNull(message = "Price is required")
    Double price;

    String content;

    @NotBlank(message = "Category ID is required")
    String categoryId;

//    @NotBlank(message = "Thumbnail URL is required")
    String thumbnail;

    String promoVideo;   
}
