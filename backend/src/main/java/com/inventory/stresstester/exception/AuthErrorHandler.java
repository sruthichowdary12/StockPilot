package com.inventory.stresstester.exception;
import org.springframework.http.*;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
@RestControllerAdvice
public class AuthErrorHandler {
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> invalidCredentials(BadCredentialsException e) {
        return ResponseEntity.status(401).body(Map.of("message","Invalid email or password"));
    }
}
