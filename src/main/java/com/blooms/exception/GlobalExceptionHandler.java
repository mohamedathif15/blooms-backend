package com.blooms.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication
        .BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> badCreds(
            BadCredentialsException e) {
        return ResponseEntity.status(401)
                .body(Map.of("error",
                        "Invalid email or password"));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> runtime(RuntimeException e) {
        return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> validation(
            MethodArgumentNotValidException e) {
        String msg = e.getBindingResult()
                .getFieldErrors().stream()
                .map(f -> f.getField()
                        + ": " + f.getDefaultMessage())
                .findFirst().orElse("Validation error");
        return ResponseEntity.badRequest()
                .body(Map.of("error", msg));
    }
}