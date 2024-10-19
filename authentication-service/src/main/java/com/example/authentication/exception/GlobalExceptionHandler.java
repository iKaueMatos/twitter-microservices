package com.example.authentication.exception;

import com.example.authentication.dto.response.ErrorResponse;
import io.jsonwebtoken.JwtException;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return new ResponseEntity<>(errors, BAD_REQUEST);
    }

    @ExceptionHandler({
            EntityNotFoundException.class,
            ActivationCodeNotFoundException.class
    })
    public ResponseEntity<ErrorResponse> handleNotFoundException(Exception e) {
        return generateDefaultErrorResponse(NOT_FOUND, e);
    }

    @ExceptionHandler({
            EntityExistsException.class,
            ConstraintViolationException.class,
            InvalidTokenException.class,
            BadCredentialsException.class,
            JwtException.class
    })
    public ResponseEntity<ErrorResponse> handleBadRequestException(Exception e) {
        return generateDefaultErrorResponse(BAD_REQUEST, e);
    }

    @ExceptionHandler(ActivationCodeExpiredException.class)
    public ResponseEntity<ErrorResponse> handleException(ActivationCodeExpiredException e) {
        return generateDefaultErrorResponse(GONE, e);
    }

    @ExceptionHandler({
            AccountNotActivatedException.class,
            InvalidCredentialsException.class
    })
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        return generateDefaultErrorResponse(FORBIDDEN, e);
    }

    private ResponseEntity<ErrorResponse> generateDefaultErrorResponse(HttpStatus status, Exception e) {
        ErrorResponse error = ErrorResponse.builder()
                .code(status.value())
                .message(e.getMessage())
                .timestamp(System.currentTimeMillis())
                .build();

        return new ResponseEntity<>(error, status);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleUserAlreadyExists(UserAlreadyExistsException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", ex.getMessage());
        errorResponse.put("status", HttpStatus.CONFLICT.value()); // 409
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(InvalidActivationCodeException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidActivationCode(InvalidActivationCodeException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", ex.getMessage());
        errorResponse.put("status", HttpStatus.BAD_REQUEST.value()); // 400
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleAllExceptions(Exception ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", "An unexpected error occurred: " + ex.getMessage());
        errorResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
