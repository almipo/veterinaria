package com.veterinaria.duenio.domain.exception;

public class DuenioNotFoundException extends RuntimeException {
    public DuenioNotFoundException(Long id) {
        super("No se encontró el dueño con id " + id);
    }

    public DuenioNotFoundException(String message) {
        super(message);
    }
}
