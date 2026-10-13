package com.vet_system.Exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalException {
@ExceptionHandler(MethodArgumentNotValidException.class)
public ResponseEntity<ErrorResponse> handleValidationException(
        MethodArgumentNotValidException ex,
        HttpServletRequest request) {

    String mensaje = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .collect(Collectors.joining(", "));

    return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse(
                    LocalDateTime.now(),
                    400,
                    "Bad Request",
                    mensaje,
                    request.getRequestURI()
            ));
}

@ExceptionHandler(ResourceNotFoundException.class)
public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
        ResourceNotFoundException rnfe,
        HttpServletRequest request) {

    return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(new ErrorResponse(
                    LocalDateTime.now(),
                    404,
                    "Not Found",
                    rnfe.getMessage(),
                    request.getRequestURI()
            ));
}

@ExceptionHandler(DuplicateResourceException.class)
public ResponseEntity<ErrorResponse> handleDuplicateResourceException(
        DuplicateResourceException dre,
        HttpServletRequest request) {

    return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(new ErrorResponse(
                    LocalDateTime.now(),
                    409,
                    "Conflict",
                    dre.getMessage(),
                    request.getRequestURI()
            ));
}

@ExceptionHandler(TurnoSuperpuestoException.class)
public ResponseEntity<ErrorResponse> handleTurnoSuperpuestoException(
        TurnoSuperpuestoException tse,
        HttpServletRequest request) {

    return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(new ErrorResponse(
                    LocalDateTime.now(),
                    409,
                    "Conflict",
                    tse.getMessage(),
                    request.getRequestURI()
            ));
}

@ExceptionHandler(Exception.class)
public ResponseEntity<ErrorResponse> handleGeneralException(
        Exception ex,
        HttpServletRequest request) {

    return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ErrorResponse(
                    LocalDateTime.now(),
                    500,
                    "Internal Server Error",
                    "Ocurrió un error inesperado",
                    request.getRequestURI()
            ));
}
}
