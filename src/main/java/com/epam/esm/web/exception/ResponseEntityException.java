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

    @ExceptionHandler(GiftNotFoundException.class)
    public final ResponseEntity<Object> handleAllExceptions(GiftNotFoundException ex, WebRequest request) throws Exception{
        ExceptionResponse exceptionResponse = new ExceptionResponse(ex.getMessage(), String.format("%s", LocalDateTime.now()));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(GiftBadInputException.class)
    public final ResponseEntity<Object> handleAllExceptions(GiftBadInputException ex, WebRequest request) throws Exception{
        ExceptionResponse exceptionResponse = new ExceptionResponse(ex.getMessage(), String.format("%s", LocalDateTime.now()));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TagNotFoundException.class)
    public final ResponseEntity<Object> handleAllExceptions(TagNotFoundException ex, WebRequest request) throws Exception{
        ExceptionResponse exceptionResponse = new ExceptionResponse(ex.getMessage(), String.format("%s", LocalDateTime.now()));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TagBadInputException.class)
    public final ResponseEntity<Object> handleAllExceptions(TagBadInputException ex, WebRequest request) throws Exception{
        ExceptionResponse exceptionResponse = new ExceptionResponse(ex.getMessage(), String.format("%s", LocalDateTime.now()));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }
}
