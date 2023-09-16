package kz.toko.app.controller.errors;

import kz.toko.api.model.ErrorDetails;
import kz.toko.app.exception.EntityDeletedException;
import kz.toko.app.exception.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleEntityNotFoundException(EntityNotFoundException e) {
        return buildErrorResponse(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EntityDeletedException.class)
    public ResponseEntity<ErrorDetails> handleEntityDeletedException(EntityDeletedException e) {
        return buildErrorResponse(e, HttpStatus.GONE);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorDetails> handleAllUncaughtException(RuntimeException exception) {
        log.error("Caught an exception: {}", exception.getMessage());
        return buildErrorResponse(exception, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        final var errors = ex
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(FieldError::getField,
                        fe -> format("%s %s", fe.getField(), fe.getDefaultMessage())));

        final var errorDetails = buildErrorDetails(ex, errors);

        return handleExceptionInternal(ex, errorDetails, headers, status, request);
    }

    private ErrorDetails buildErrorDetails(Exception exception, Map<String, String> params) {
        return new ErrorDetails()
                .message(exception.getMessage())
                .exceptionClass(exception.getClass().getName())
                .params(params);
    }

    private ResponseEntity<ErrorDetails> buildErrorResponse(Exception exception, HttpStatus httpStatus) {
        return ResponseEntity
                .status(httpStatus)
                .contentType(APPLICATION_PROBLEM_JSON)
                .body(buildErrorDetails(exception, null));
    }
}
