package com.epam.esm.web.util.exception;

import lombok.Getter;

@Getter
public class ExceptionResponse {
    private final String message;
    private final String timestamp;

    public ExceptionResponse(String message, String timestamp){
        this.message = message;
        this.timestamp = timestamp;
    }
}
