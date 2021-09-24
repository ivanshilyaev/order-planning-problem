package com.example.orderplanning.service.exception;

public class NoWarehouseWithSuchProductException extends RuntimeException {
    public NoWarehouseWithSuchProductException() {
    }

    public NoWarehouseWithSuchProductException(String message) {
        super(message);
    }

    public NoWarehouseWithSuchProductException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoWarehouseWithSuchProductException(Throwable cause) {
        super(cause);
    }
}
