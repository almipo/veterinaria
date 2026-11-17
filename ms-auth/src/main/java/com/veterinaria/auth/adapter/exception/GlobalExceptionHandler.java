package com.veterinaria.auth.adapter.exception;

import com.veterinaria.auth.adapter.in.web.dto.ErrorResponse;
import com.veterinaria.auth.domain.exception.CredencialesInvalidasException;
import com.veterinaria.auth.domain.exception.UsuarioYaExisteException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CredencialesInvalidasException.class)
    public ResponseEntity<ErrorResponse> credentials(CredencialesInvalidasException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(LocalDateTime.now(), 401, "Unauthorized", ex.getMessage()));
    }
    @ExceptionHandler(UsuarioYaExisteException.class)
    public ResponseEntity<ErrorResponse> duplicated(UsuarioYaExisteException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(LocalDateTime.now(), 409, "Conflict", ex.getMessage()));
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> validation(MethodArgumentNotValidException ex) {
        String msg = ex.getBindingResult().getFieldErrors().stream().findFirst().map(f -> f.getDefaultMessage()).orElse("Solicitud inválida");
        return ResponseEntity.badRequest().body(new ErrorResponse(LocalDateTime.now(), 400, "Bad Request", msg));
    }
}
