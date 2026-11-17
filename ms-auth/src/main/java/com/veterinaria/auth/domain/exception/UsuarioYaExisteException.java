package com.veterinaria.auth.domain.exception;

public class UsuarioYaExisteException extends RuntimeException {
    public UsuarioYaExisteException(String mensaje) { super(mensaje); }
}
