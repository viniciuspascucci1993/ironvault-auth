package com.ironvault.auth.adapter.in.common;

import com.ironvault.auth.domain.exception.InvalidCredentialsException;
import com.ironvault.auth.domain.exception.TooManyRequestsException;
import com.ironvault.auth.domain.exception.UserAlreadyExistsException;
import com.ironvault.auth.domain.exception.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ApiError> handleUserAlreadyExists(UserAlreadyExistsException ex,
                                                            HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ApiError(HttpStatus.CONFLICT.value(), "Conflict", ex.getMessage(), request.getRequestURI()));
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ApiError> handleInvalidCredentials(InvalidCredentialsException ex,
                                                             HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ApiError(HttpStatus.UNAUTHORIZED.value(), "Unauthorized", ex.getMessage(), request.getRequestURI()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiError> handleUserNotFound(UserNotFoundException ex,
                                                       HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiError(HttpStatus.NOT_FOUND.value(), "Not Found", ex.getMessage(), request.getRequestURI()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex,
                                                     HttpServletRequest request) {
        String message = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiError(HttpStatus.BAD_REQUEST.value(), "Bad Request", message, request.getRequestURI()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneric(Exception ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "Internal Server Error", "An unexpected error occurred", request.getRequestURI()));
    }

    @ExceptionHandler(TooManyRequestsException.class)
    public ResponseEntity<ApiError> handleTooManyRequests(TooManyRequestsException ex, HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .body(new ApiError(HttpStatus.TOO_MANY_REQUESTS.value(), "Too Many Requests",
                        ex.getMessage(), request.getRequestURI()));
    }

    public record ApiError(int status, String title, String message, String path) {}
}
