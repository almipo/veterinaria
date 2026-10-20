package com.veterinaria.duenio.domain.exception;

public class DuplicateEmailException extends RuntimeException {
    public DuplicateEmailException(String email) {
        super("Ya existe un dueño con el email " + email);
    }
}
