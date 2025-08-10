package com.tradetrack.tradetrack.Exceptions.Types;


import org.springframework.http.HttpStatus;

public class OtpException extends RuntimeException{

    private ErrorType errorType;

    public OtpException(String message,ErrorType type){
        super(message);
        this.errorType = type;
    }

    public ErrorType getType() {
        return errorType;
    }

    public HttpStatus getHttpStatus(){
        return switch(errorType){
            case NOT_FOUND -> HttpStatus.NOT_FOUND; //404
            case EXPIRED -> HttpStatus.GONE; //410
            case USED -> HttpStatus.CONFLICT; //409
            case ATTEMPTS_EXCEEDED -> HttpStatus.TOO_MANY_REQUESTS; //429
            case INCORRECT -> HttpStatus.BAD_REQUEST; //400
        };
    }

    public enum ErrorType{
        NOT_FOUND,
        EXPIRED,
        USED,
        ATTEMPTS_EXCEEDED,
        INCORRECT
    }
}
