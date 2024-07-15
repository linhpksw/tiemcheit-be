package com.tiemcheit.tiemcheitbe.repository.exception;

import com.tiemcheit.tiemcheitbe.dto.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    // Catches all exceptions that are not explicitly handled by other exception handlers in this advice
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ApiResponse<String>> handleGenericException(Exception ex) {
        log.error("Unhandled Exception: ", ex);
        ApiResponse<String> response = new ApiResponse<>();
        response.setMessage("Internal server error: " + ex.getMessage());
        response.setError(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Handles custom AppExceptions that carry their own messages and HTTP statuses
    @ExceptionHandler(value = AppException.class)
    public ResponseEntity<ApiResponse<String>> handleAppException(AppException ex) {
        log.error("Application Exception: ", ex);
        ApiResponse<String> response = new ApiResponse<>();
        response.setMessage(ex.getMessage());
        response.setError(ex.getStatus().value());
        return new ResponseEntity<>(response, ex.getStatus());
    }

    // Handles AccessDeniedExceptions thrown by Spring Security
    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity<ApiResponse<String>> handleAccessDeniedException(AccessDeniedException ex) {
        log.error("Access Denied: ", ex);
        ApiResponse<String> response = new ApiResponse<>();
        response.setMessage("Access is denied");
        response.setError(HttpStatus.FORBIDDEN.value());
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    // Handles validation errors thrown by @Valid annotated objects in controller methods
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<String>> handleValidationException(MethodArgumentNotValidException ex) {
        log.error("Validation error: ", ex);
        String errors = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        ApiResponse<String> response = new ApiResponse<>();
        response.setMessage("Validation failed: " + errors);
        response.setError(HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
