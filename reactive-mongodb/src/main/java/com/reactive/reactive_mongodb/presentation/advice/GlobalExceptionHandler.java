package com.reactive.reactive_mongodb.presentation.advice;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.reactive.reactive_mongodb.presentation.advice.custom.InfoNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InfoNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleInfoNotFoundException(InfoNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(Map.of("NOT FOUND", e.getMessage()));
    }

    // HttpMessageNotReadableException: To handle cases where the request body is
    // not readable
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return ResponseEntity.badRequest()
            .body(Map.of("BAD REQUEST", "The request body is not readable --- " + e.getMessage()));
    }

    // General Exceptions

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<Map<String, String>> handleNullPointerException(NullPointerException e) {
        return ResponseEntity.badRequest()
            .body(Map.of("BAD REQUEST", e.getMessage()));
    }

    public ResponseEntity<Map<String, String>> handleGlobalException(Exception e) {
        return ResponseEntity.internalServerError()
            .body(Map.of("An unexpected error ocurred", e.getMessage()));
    }

}
