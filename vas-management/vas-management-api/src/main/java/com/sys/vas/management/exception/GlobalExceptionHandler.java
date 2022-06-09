package com.sys.vas.management.exception;

import com.sys.vas.management.dto.Response;
import com.sys.vas.management.dto.ResponseCodes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@Slf4j
@Order(0)
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> errors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach(e -> {
            FieldError fe = (FieldError) e;
            String field = fe.getField();
            String msg = e.getDefaultMessage();
            errors.add(new StringBuilder().append(field).append(":").append(msg).toString());
        });
        log.error("Request validation errors|{}", errors);
        //only sends first error
        Response response = Response.error(errors.get(0)).build(ResponseCodes.REQUEST_VALIDATION_ERROR);
        log.info("Res|{}", response.toString());
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    /**
     * handle ApiException
     * @param ex
     * @return
     */
    @ExceptionHandler(value = ApiException.class)
    public ResponseEntity<?> handleApiException(ApiException ex) {
        log.error("ApiException:{}", ex.getMsg());
        Response response = Response.error(ex.getMsg()).build(ex.getResponseType());
        log.info("Res|{}", response.toString());
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    /**
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(value = BadCredentialsException.class)
    public ResponseEntity<Response> handleBadCredentialsException(BadCredentialsException ex) {
        log.error("BadCredentialsException", ex);
        Response response = Response.error("Username or password does not match").build(ResponseCodes.BAD_CREDENTIALS);
        log.info("Res|{}", response.toString());
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    /**
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException ex) {
        log.error("AccessDeniedException", ex);
        Response response = Response.error(ex.getMessage()).build(ResponseCodes.ACCESS_DENIED);
        log.info("Res|{}", response.toString());
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    /**
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(value = DisabledException.class)
    public ResponseEntity<?> handleDisabledException(DisabledException ex) {
        log.error("DisabledException", ex);
        Response response = Response.error("User is inactive").build(ResponseCodes.INACTIVE_USER);
        log.info("Res|{}", response.toString());
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    /**
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<?> handleException(Exception ex) {
        log.error("InternalError", ex);
        return ResponseEntity.internalServerError().build();
    }
}
