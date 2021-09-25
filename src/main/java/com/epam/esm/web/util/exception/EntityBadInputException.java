package com.epam.esm.web.util.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EntityBadInputException extends RuntimeException{

    public EntityBadInputException(String message){
        super(message);
    }
}
