//package com.short_term_course.exception;
//
//import org.springframework.http.HttpStatus;
//
//public enum ErrorCode {
//    // Category
//    CAT_NOT_FOUND("CAT-404", "Category not found", HttpStatus.NOT_FOUND),
//    CAT_DUPLICATE_NAME("CAT-400", "Category name already exists", HttpStatus.BAD_REQUEST),
//
//    // Validation
//    VALIDATION_ERROR("VAL-400", "Validation failed", HttpStatus.BAD_REQUEST),
//
//    // Auth
//    AUTH_UNAUTHORIZED("AUTH-401", "Authentication failed", HttpStatus.UNAUTHORIZED),
//    AUTH_FORBIDDEN("AUTH-403", "Access denied", HttpStatus.FORBIDDEN),
//
//    // Global
//    SYS_INTERNAL_ERROR("SYS-500", "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
//
//    private final String code;
//    private final String message;
//    private final HttpStatus status;
//
//    ErrorCode(String code, String message, HttpStatus status) {
//        this.code = code;
//        this.message = message;
//        this.status = status;
//    }
//
//    public String getCode() { return code; }
//    public String getMessage() { return message; }
//    public HttpStatus getStatus() { return status; }
//}
