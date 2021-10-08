package kz.toko.app.controller.errors;

import kz.toko.api.model.ErrorResponse;
import kz.toko.app.exception.EntityDeletedException;
import kz.toko.app.exception.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException entityNotFoundException) {
        return buildErrorResponse(entityNotFoundException, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EntityDeletedException.class)
    public ResponseEntity<ErrorResponse> handleEntityDeletedException(EntityDeletedException entityDeletedException) {
        return buildErrorResponse(entityDeletedException, HttpStatus.GONE);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleAllUncaughtException(RuntimeException exception) {
        log.error("Caught an exception: {}", exception.getMessage());
        return buildErrorResponse(exception, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(Exception exception, HttpStatus httpStatus) {
        ErrorResponse response = new ErrorResponse();
        response.setMessage(exception.getMessage());
        response.setExceptionClass(exception.getClass().getName());
        return ResponseEntity
                .status(httpStatus)
                .contentType(APPLICATION_PROBLEM_JSON)
                .body(response);
    }
}
