package com.tradetrack.tradetrack.Exceptions.Types;

import org.springframework.http.HttpStatus;

public class UserException extends RuntimeException {

    private final ErrorType errorType;

    public UserException(String message, ErrorType errorType) {
        super(message);
        this.errorType = errorType;
    }

    public ErrorType getErrorType() {
        return errorType;
    }
    
    public HttpStatus getHttpStatus(){
        return switch(errorType){
            case USER_ALREADY_EXISTS -> HttpStatus.CONFLICT; //409
            case USER_NOT_FOUND -> HttpStatus.NOT_FOUND; //404
            case INVALID_CREDENTIALS -> HttpStatus.UNAUTHORIZED; //401
        };
    }

    public enum ErrorType {
        USER_ALREADY_EXISTS,   // 409 Conflict
        USER_NOT_FOUND,        // 404 Not Found
        INVALID_CREDENTIALS    // 401 Unauthorized
    }
}
