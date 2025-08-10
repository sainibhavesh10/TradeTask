package com.tradetrack.tradetrack.Exceptions.Handling;

import com.tradetrack.tradetrack.Exceptions.Types.*;
import com.tradetrack.tradetrack.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(OtpException.class)
    public ResponseEntity<ErrorResponse> handleWatchlistException(OtpException ex) {
        HttpStatus httpStatus = ex.getHttpStatus();
        ErrorResponse errorResponse = new ErrorResponse(httpStatus.value(), ex.getMessage());
        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    @ExceptionHandler(PortfolioException.class)
    public ResponseEntity<ErrorResponse> handleWatchlistException(PortfolioException ex) {
        HttpStatus httpStatus = ex.getHttpStatus();
        ErrorResponse errorResponse = new ErrorResponse(httpStatus.value(), ex.getMessage());
        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    @ExceptionHandler(StockException.class)
    public ResponseEntity<ErrorResponse> handleWatchlistException(StockException ex) {
        HttpStatus httpStatus = ex.getHttpStatus();
        ErrorResponse errorResponse = new ErrorResponse(httpStatus.value(), ex.getMessage());
        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity<ErrorResponse> handleWatchlistException(UserException ex) {
        System.out.println("Handling UserException with status: " + ex.getHttpStatus());
        HttpStatus httpStatus = ex.getHttpStatus();
        ErrorResponse errorResponse = new ErrorResponse(httpStatus.value(), ex.getMessage());
        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    @ExceptionHandler(WatchlistException.class)
    public ResponseEntity<ErrorResponse> handleWatchlistException(WatchlistException ex) {
        HttpStatus httpStatus = ex.getHttpStatus();
        ErrorResponse errorResponse = new ErrorResponse(httpStatus.value(), ex.getMessage());
        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        ErrorResponse errorResponse = new ErrorResponse(httpStatus.value(), ex.getMessage());
        return ResponseEntity.status(httpStatus).body(errorResponse);
    }
}
