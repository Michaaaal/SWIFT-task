package michal.malek.remitlytask.advice;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import michal.malek.remitlytask.exception.SwiftDataAlreadyExistsException;
import michal.malek.remitlytask.exception.SwiftDataNotFoundException;
import michal.malek.remitlytask.exception.SwiftDataNotValidException;
import michal.malek.remitlytask.model.standard.StandardizedExceptionResponse;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * Handles exceptions globally.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SwiftDataNotFoundException.class)
    public ResponseEntity<StandardizedExceptionResponse> handleSwiftDataNotFound(
            final SwiftDataNotFoundException swiftDataNotFound) {

        StandardizedExceptionResponse response =
                new StandardizedExceptionResponse(swiftDataNotFound.getMessage(), HttpStatus.NOT_FOUND);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @ExceptionHandler(SwiftDataAlreadyExistsException.class)
    public ResponseEntity<StandardizedExceptionResponse> handleSwiftDataAlreadyExist(
            final SwiftDataAlreadyExistsException swiftDataAlreadyExists) {

        StandardizedExceptionResponse response =
                new StandardizedExceptionResponse(swiftDataAlreadyExists.getMessage(), HttpStatus.CONFLICT);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @ExceptionHandler(SwiftDataNotValidException.class)
    public ResponseEntity<StandardizedExceptionResponse> handleSwiftDataNotValid(
            final SwiftDataNotValidException swiftDataNotValidException) {

        StandardizedExceptionResponse response =
                new StandardizedExceptionResponse(swiftDataNotValidException.getMessage(), HttpStatus.BAD_REQUEST);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<StandardizedExceptionResponse> handleConstraintViolationException(ConstraintViolationException ex) {
        String message = ex.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));

        StandardizedExceptionResponse response =
                new StandardizedExceptionResponse(message, HttpStatus.BAD_REQUEST);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));

        StandardizedExceptionResponse response =
                new StandardizedExceptionResponse(message, HttpStatus.BAD_REQUEST);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
