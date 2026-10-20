package com.veterinaria.veterinario.domain.exception;

public class DuplicateMatriculaException extends RuntimeException {
    public DuplicateMatriculaException(String matricula) {
        super("Matricula ya registrada: " + matricula);
    }
}
