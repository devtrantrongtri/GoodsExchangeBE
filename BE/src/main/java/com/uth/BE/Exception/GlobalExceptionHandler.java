package com.uth.BE.Exception;

import com.uth.BE.dto.res.GlobalRes;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.nio.file.NoSuchFileException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GlobalRes<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        StringBuilder errors = new StringBuilder();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.append(fieldName).append(": ").append(errorMessage).append(" x ; ");
        });
        return new GlobalRes<>(HttpStatus.BAD_REQUEST.value(), "Validation failed: " + errors.toString());
    }
    @ExceptionHandler(IOException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public GlobalRes<String> handleIOException(IOException ex) {
        return new GlobalRes<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "File operation failed: " + ex.getMessage());
    }

    @ExceptionHandler(NoSuchFileException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public GlobalRes<String> handleNoSuchFileException(NoSuchFileException ex) {
        return new GlobalRes<>(HttpStatus.NOT_FOUND.value(), "File not found: " + ex.getMessage());
    }
}