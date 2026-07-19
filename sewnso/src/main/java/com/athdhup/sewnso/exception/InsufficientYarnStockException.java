package com.athdhup.sewnso.exception;

public class InsufficientYarnStockException extends RuntimeException {
    public InsufficientYarnStockException(String message) {
        super(message);
    }
}