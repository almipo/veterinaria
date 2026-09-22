package com.vet_system.Exception;

public class StockInsuficienteException extends RuntimeException {

    public StockInsuficienteException(String message) {
        super(message);
    }
}