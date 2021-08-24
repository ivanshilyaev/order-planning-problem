package com.example.orderplanning.service.exception;

public class NoWarehouseWithSuchIdException extends RuntimeException {
    public NoWarehouseWithSuchIdException() {
    }

    public NoWarehouseWithSuchIdException(String message) {
        super(message);
    }

    public NoWarehouseWithSuchIdException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoWarehouseWithSuchIdException(Throwable cause) {
        super(cause);
    }
}
