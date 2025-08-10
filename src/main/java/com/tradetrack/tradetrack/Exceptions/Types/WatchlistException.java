package com.tradetrack.tradetrack.Exceptions.Types;

import org.springframework.http.HttpStatus;

public class WatchlistException extends RuntimeException {

    private final ErrorType errorType;

    public WatchlistException(String message, ErrorType errorType) {
        super(message);
        this.errorType = errorType;
    }

    public ErrorType getErrorType() {
        return errorType;
    }
    
    public HttpStatus getHttpStatus(){
        return switch(errorType){
            case STOCK_ALREADY_IN_WATCHLIST -> HttpStatus.CONFLICT; //409
            case STOCK_NOT_IN_WATCHLIST -> HttpStatus.NOT_FOUND; //404
            case UNSUPPORTED_SORT_FIELD -> HttpStatus.BAD_REQUEST; //400
        };
    }

    public enum ErrorType {
        STOCK_ALREADY_IN_WATCHLIST,
        STOCK_NOT_IN_WATCHLIST,
        UNSUPPORTED_SORT_FIELD
    }
}
