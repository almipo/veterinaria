package com.veterinaria.duenio.domain.exception;

public class DuplicateCedulaException extends RuntimeException {
    public DuplicateCedulaException(String cedula) {
        super("Ya existe un dueño con la cédula " + cedula);
    }
}
