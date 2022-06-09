package com.sys.vas.management.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ResponseCodes implements ResponseType {

    USER_NOT_FOUND(1000, "user not found", HttpStatus.BAD_REQUEST),
    INTERNAL_SERVER_ERROR(1001, "internal server error", HttpStatus.INTERNAL_SERVER_ERROR),
    OPERATION_SUCCESS(1002, "success", HttpStatus.OK),
    JWT_ERROR(1003, "access token error", HttpStatus.FORBIDDEN),
    ACCESS_DENIED(1004, "access denied", HttpStatus.UNAUTHORIZED),
    BAD_CREDENTIALS(1005, "username or password does not match", HttpStatus.FORBIDDEN),
    REQUEST_VALIDATION_ERROR(1006, "errors in request body", HttpStatus.BAD_REQUEST),
    INVALID_OPERATION(1007, "invalid operation", HttpStatus.BAD_REQUEST),
    INACTIVE_USER(1008, "user is inactive", HttpStatus.FORBIDDEN),
    USER_ALREADY_FOUND(1009, "user already exists", HttpStatus.BAD_REQUEST),
    USER_ROLE_NOT_FOUND(1010, "user role not found", HttpStatus.BAD_REQUEST),
    SERVICE_NOT_FOUND(1011, "service not found", HttpStatus.BAD_REQUEST),
    ACTION_NOT_FOUND(1012, "action not found", HttpStatus.BAD_REQUEST),
    KEYWORD_NOT_FOUND(1013, "keywords not found", HttpStatus.BAD_REQUEST),
    API_NOT_FOUND(1014, "api not found", HttpStatus.BAD_REQUEST),
    CX_RESPONSE_NOT_FOUND(1015, "cx-response not found", HttpStatus.BAD_REQUEST);

    private int code;
    private String message;
    private HttpStatus httpStatus;

    @Override
    public int getHttpStatus() {
        return httpStatus.value();
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public int getCode() {
        return code;
    }
}
