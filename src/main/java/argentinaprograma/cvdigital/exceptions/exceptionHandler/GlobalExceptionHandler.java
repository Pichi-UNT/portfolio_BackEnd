package argentinaprograma.cvdigital.exceptions.exceptionHandler;

import argentinaprograma.cvdigital.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            AccessDeniedException.class,
            ForbiddenException.class
    })
    public ResponseEntity<ErrorMessage> unauthorizedRequest(Exception exception) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorMessage(exception, HttpStatus.FORBIDDEN.value()));
    }


    @ExceptionHandler({
            NotFoundException.class
    })
    public ResponseEntity<ErrorMessage> notFoundRequest(Exception exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessage(exception, HttpStatus.NOT_FOUND.value()));
    }

    @ExceptionHandler({
            BadRequestException.class,

    })
    public ResponseEntity<ErrorMessage> badRequest(Exception exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage(exception, HttpStatus.BAD_REQUEST.value()));
    }

    @ExceptionHandler({
            ConflictException.class
    })
    public ResponseEntity<ErrorMessage> conflict(Exception exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorMessage(exception, HttpStatus.CONFLICT.value()));
    }


    @ExceptionHandler({
            BadGatewayException.class
    })
    public ResponseEntity<ErrorMessage> badGateway(Exception exception) {
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(new ErrorMessage(exception, HttpStatus.BAD_GATEWAY.value()));
    }


    @ExceptionHandler({
            Exception.class
    })
    public ResponseEntity<ErrorMessage> exception(Exception exception) {
        // The error must be corrected
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorMessage(exception, HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }
}
