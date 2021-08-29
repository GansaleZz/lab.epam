package com.epam.esm.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TagBadInputException extends RuntimeException{

    public TagBadInputException(String message){
        super(message);
    }
}
