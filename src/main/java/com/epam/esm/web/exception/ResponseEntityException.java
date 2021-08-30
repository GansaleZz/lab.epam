package com.epam.esm.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
@RestController
public class ResponseEntityException extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) throws Exception{
        ExceptionResponse exceptionResponse = new ExceptionResponse(ex.getMessage(), String.format("%s", LocalDateTime.now()));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public final ResponseEntity<Object> handleAllExceptions(EntityNotFoundException ex, WebRequest request) throws Exception{
        ExceptionResponse exceptionResponse = new ExceptionResponse(ex.getMessage(), String.format("%s", LocalDateTime.now()));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EntityBadInputException.class)
    public final ResponseEntity<Object> handleAllExceptions(EntityBadInputException ex, WebRequest request) throws Exception{
        ExceptionResponse exceptionResponse = new ExceptionResponse(ex.getMessage(), String.format("%s", LocalDateTime.now()));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }
}
