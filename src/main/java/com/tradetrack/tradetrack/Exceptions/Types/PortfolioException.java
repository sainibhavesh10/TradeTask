package com.tradetrack.tradetrack.Exceptions.Types;

import org.springframework.http.HttpStatus;

public class PortfolioException extends RuntimeException {

    private final ErrorType errorType;

    public PortfolioException(String message, ErrorType type) {
        super(message);
        this.errorType = type;
    }

    public ErrorType getType() {
        return errorType;
    }

    public HttpStatus getHttpStatus(){
        return switch(errorType){
            case INVALID_QUANTITY -> HttpStatus.BAD_REQUEST; //400
            case INSUFFICIENT_HOLDINGS -> HttpStatus.CONFLICT; //409
        };
    }

    public enum ErrorType {
        INVALID_QUANTITY,
        INSUFFICIENT_HOLDINGS
    }
}