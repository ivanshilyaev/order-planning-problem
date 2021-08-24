package com.example.orderplanning.service.exception;

public class NoCustomerWithSuchIdException extends RuntimeException {
    public NoCustomerWithSuchIdException() {
    }

    public NoCustomerWithSuchIdException(String message) {
        super(message);
    }

    public NoCustomerWithSuchIdException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoCustomerWithSuchIdException(Throwable cause) {
        super(cause);
    }
}
