package com.short_term_course.dto.email;

import lombok.*;
import lombok.experimental.FieldDefaults;

//@Getter
//@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SendEmailDto {
    String to;
    String subject;
    String text;
}
