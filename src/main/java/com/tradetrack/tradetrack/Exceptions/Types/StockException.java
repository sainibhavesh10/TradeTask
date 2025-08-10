package com.tradetrack.tradetrack.Exceptions.Types;

import org.springframework.http.HttpStatus;

public class StockException extends RuntimeException{

    private final ErrorType errorType;

    public StockException(String message, ErrorType errorType) {
        super(message);
        this.errorType = errorType;
    }

    public ErrorType getErrorType() {
        return errorType;
    }
    
    public HttpStatus getHttpStatus(){
        return switch (errorType){
            case INVALID_REQUEST -> HttpStatus.BAD_REQUEST; //400
            case NOT_FOUND -> HttpStatus.NOT_FOUND; //404
            case API_FAILURE -> HttpStatus.SERVICE_UNAVAILABLE; //503
            case API_NO_DATA -> HttpStatus.BAD_GATEWAY; //502
            case NO_DATA_TO_PROCESS -> HttpStatus.UNPROCESSABLE_ENTITY; //422
        };
    }

    public enum ErrorType {
        INVALID_REQUEST,     // Bad input (null/empty symbol, wrong params)
        NOT_FOUND,           // Stock not found
        API_FAILURE,         // API call failed
        API_NO_DATA,         // API returned no data
        NO_DATA_TO_PROCESS   // No records in DB or API to process
    }
}
