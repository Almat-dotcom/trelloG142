package kz.bitlab.trelloG142.exception.core;

import kz.bitlab.trelloG142.exception.TrelloException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.UUID;

@RestControllerAdvice
public class ExceptionHandlerCustom {
    @ExceptionHandler(TrelloException.class)
    public ResponseEntity<ErrorResponse> getRuntime(RuntimeException exception) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorResponse response = ErrorResponse.builder()
                .id(UUID.randomUUID().toString())
                .timestamp(LocalDateTime.now().toString())
                .status(status)
                .details(exception.getMessage())
                .build();
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> getException(Exception exception) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ErrorResponse response = ErrorResponse.builder()
                .id(UUID.randomUUID().toString())
                .timestamp(LocalDateTime.now().toString())
                .status(status)
                .details(exception.getMessage())
                .build();
        return ResponseEntity.status(status).body(response);
    }

}