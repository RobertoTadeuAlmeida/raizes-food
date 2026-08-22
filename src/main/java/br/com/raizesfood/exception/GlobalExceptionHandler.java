package br.com.raizesfood.exception;

import br.com.raizesfood.dto.ErroResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErroResponse> tratarIllegalArgument(
            IllegalArgumentException exception
    ) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        ErroResponse erro = new ErroResponse(
                status.value(),
                status.getReasonPhrase(),
                exception.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(status)
                .body(erro);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErroResponse> tratarIllegalState(
            IllegalStateException exception
    ) {
        HttpStatus status = HttpStatus.CONFLICT;

        ErroResponse erro = new ErroResponse(
                status.value(),
                status.getReasonPhrase(),
                exception.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(status)
                .body(erro);
    }
}