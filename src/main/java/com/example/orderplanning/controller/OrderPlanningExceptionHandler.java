package com.example.orderplanning.controller;

import com.example.orderplanning.service.exception.NoCustomerWithSuchIdException;
import com.example.orderplanning.service.exception.NoWarehouseWithSuchIdException;
import com.example.orderplanning.service.exception.NoWarehouseWithSuchProductException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class OrderPlanningExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException exception) {
        log.error("400 detected", exception);
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({NoHandlerFoundException.class, NoCustomerWithSuchIdException.class,
            NoWarehouseWithSuchIdException.class, NoWarehouseWithSuchProductException.class})
    public String handleNoHandlerFoundException(Exception exception) {
        log.error("404 detected", exception);
        return exception.getMessage();
    }
}
