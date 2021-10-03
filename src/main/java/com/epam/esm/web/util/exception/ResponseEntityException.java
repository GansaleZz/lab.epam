package com.epam.esm.web.util.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Locale;

@ControllerAdvice
@RestController
public class ResponseEntityException extends ResponseEntityExceptionHandler {
    private static final String BAD_INPUT = "bad.input.exception";
    private static final String NOT_FOUND = "not.found.exception";
    private static final String EXCEPTION_ON_SERVER_SIDE = "on.server.exception";
    private static final String FOR_FORMAT = "%s";
    private final Logger logger = LoggerFactory.getLogger(ResponseEntityException.class);

    private final MessageSource messageSource;
    private final Clock clock;

    @Autowired
    public ResponseEntityException(MessageSource messageSource, Clock clock) {
        this.messageSource = messageSource;
        this.clock = clock;
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllExceptions(Exception e, Locale locale) {
        String message = messageSource.getMessage(EXCEPTION_ON_SERVER_SIDE, null, locale);
        logger.error(e.getMessage());

        ExceptionResponse exceptionResponse = new ExceptionResponse(message,
                String.format(FOR_FORMAT, LocalDateTime.now(clock)));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public final ResponseEntity<Object> handleAllExceptions(EntityNotFoundException ex,
                                                            Locale locale) {
        String message = messageSource.getMessage(NOT_FOUND, null, locale);

        ExceptionResponse exceptionResponse = new ExceptionResponse(String.format(message, ex.getMessage()),
                String.format(FOR_FORMAT, LocalDateTime.now(clock)));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EntityBadInputException.class)
    public final ResponseEntity<Object> handleAllExceptions(EntityBadInputException ex,
                                                            Locale locale) {
        String message = messageSource.getMessage(BAD_INPUT, null, locale);

        ExceptionResponse exceptionResponse = new ExceptionResponse(message + ex.getMessage(),
                String.format(FOR_FORMAT, LocalDateTime.now(clock)));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }
}