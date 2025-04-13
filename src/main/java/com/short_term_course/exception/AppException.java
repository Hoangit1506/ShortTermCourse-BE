package com.short_term_course.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AppException extends RuntimeException {
    private final HttpStatus status;
    private final String code;

    public AppException(HttpStatus status, String message, String code) {
        super(message);
        this.status = status;
        this.code = code;
    }

    public AppException(HttpStatus status, String message) {
        super(message);
        this.status = status;
        this.code = null;
    }

    public AppException(String message) {
        super(message);
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
        this.code = null;
    }
}



//package com.short_term_course.exception;
//
//import lombok.Getter;
//
//@Getter
//public class AppException extends RuntimeException {
//    private final ErrorCode errorCode;
//
//    // Dùng message mặc định từ ErrorCode
//    public AppException(ErrorCode errorCode) {
//        super(errorCode.getMessage());
//        this.errorCode = errorCode;
//    }
//
//    // Cho phép custom message, vẫn giữ ErrorCode
//    public AppException(ErrorCode errorCode, String customMessage) {
//        super(customMessage);
//        this.errorCode = errorCode;
//    }
//}
