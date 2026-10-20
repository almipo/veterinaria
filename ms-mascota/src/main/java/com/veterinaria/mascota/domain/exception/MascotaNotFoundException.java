package com.veterinaria.mascota.domain.exception;

public class MascotaNotFoundException extends RuntimeException {
    public MascotaNotFoundException(Long id) {
        super("No se encontró la mascota con id " + id);
    }
}
