package com.component.checkout.shared.exception;

import com.component.checkout.presentation.dto.ErrorResponse;
import com.component.checkout.presentation.dto.auth.AuthResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

/**
 * A global exception handler that intercepts exceptions thrown by controllers and returns custom responses.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private static final HttpStatus BAD_REQUEST_STATUS = HttpStatus.BAD_REQUEST;

    /**
     * Handles validation exceptions where arguments are not valid as per the defined constraints.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        LOGGER.info("Handling MethodArgumentNotValidException");
        Map<String, String> errors = new HashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return ResponseEntity.status(BAD_REQUEST_STATUS).body(errors);
    }

    /**
     * Handles constraint violation exceptions caused by invalid method parameters.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> handleConstraintViolationException(ConstraintViolationException ex) {
        LOGGER.info("Handling ConstraintViolationException");
        Map<String, String> errors = new HashMap<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            String propertyPath = violation.getPropertyPath().toString();
            String message = violation.getMessage();
            errors.put(propertyPath, message);
        }
        return ResponseEntity.status(BAD_REQUEST_STATUS).body(errors);
    }

    /**
     * Handles type mismatch errors in method arguments.
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, String>> handleTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        LOGGER.info("Handling MethodArgumentTypeMismatchException");
        Map<String, String> errors = new HashMap<>();
        String fieldName = ex.getName();
        String errorMessage = String.format("Invalid value '%s' for parameter '%s'", ex.getValue(), fieldName);
        errors.put(fieldName, errorMessage);
        return ResponseEntity.status(BAD_REQUEST_STATUS).body(errors);
    }

    /**
     * Handles IllegalArgumentException by returning a standardized error response.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        LOGGER.info("Handling IllegalArgumentException: {}", ex.getMessage());
        ErrorResponse response = new ErrorResponse(false, ex.getMessage());
        return ResponseEntity.status(BAD_REQUEST_STATUS).body(response);
    }

    /**
     * Handles BindException, typically thrown when request parameters cannot be bound to method arguments.
     */
    @ExceptionHandler(org.springframework.validation.BindException.class)
    public ResponseEntity<Map<String, String>> handleBindException(org.springframework.validation.BindException ex) {
        LOGGER.info("Handling BindException");
        Map<String, String> errors = new HashMap<>();
        for (FieldError fieldError : ex.getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return ResponseEntity.status(BAD_REQUEST_STATUS).body(errors);
    }

    /**
     * Handles HandlerMethodValidationException, thrown when method-level validation fails.
     */
    @ExceptionHandler(org.springframework.web.method.annotation.HandlerMethodValidationException.class)
    public ResponseEntity<Map<String, String>> handleHandlerMethodValidationException(
            org.springframework.web.method.annotation.HandlerMethodValidationException ex) {

        LOGGER.info("Handling HandlerMethodValidationException");
        Map<String, String> errors = new HashMap<>();
        ex.getAllErrors().forEach(error -> {
            String fieldName = (error instanceof FieldError) ? ((FieldError) error).getField() : "";
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    /**
     * Handles database integrity violations.
     */
    @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(org.springframework.dao.DataIntegrityViolationException ex) {
        LOGGER.info("Data integrity violation: {}", ex.getMostSpecificCause().getMessage());
        String message = "Database integrity error: " + ex.getMostSpecificCause().getMessage();
        return buildErrorResponse(message);
    }

    /**
     * Handles IllegalStateException by returning a standardized error response.
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalStateException(IllegalStateException ex) {
        LOGGER.info("Handling IllegalStateException: {}", ex.getMessage());
        ErrorResponse response = new ErrorResponse(false, ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Handles custom UserAlreadyExistsException.
     */
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        LOGGER.info("UserAlreadyExistsException: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage());
    }

    /**
     * Handles custom UserNotFoundException.
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex) {
        LOGGER.info("UserNotFoundException: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(false, ex.getMessage()));
    }

    /**
     * Handles custom AuthenticationFailedException.
     */
    @ExceptionHandler(AuthenticationFailedException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationFailedException(AuthenticationFailedException ex) {
        LOGGER.info("AuthenticationFailedException: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(false, ex.getMessage()));
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(String message) {
        ErrorResponse response = new ErrorResponse(false, message);
        return ResponseEntity.status(BAD_REQUEST_STATUS).body(response);
    }
}
