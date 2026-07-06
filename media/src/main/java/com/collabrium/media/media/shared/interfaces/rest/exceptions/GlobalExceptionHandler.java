package com.collabrium.media.media.shared.interfaces.rest.exceptions;

import com.collabrium.media.media.shared.interfaces.rest.resources.ErrorMessageResource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorMessageResource> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException e,
      HttpServletRequest request) {

    ErrorMessageResource error = new ErrorMessageResource(
        LocalDateTime.now().toString(),
        HttpStatus.BAD_REQUEST.value(),
        HttpStatus.BAD_REQUEST.getReasonPhrase(),
        "VALIDATION_ERROR",
        "Validation failed",
        request.getRequestURI()
    );

    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(error);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorMessageResource> handleConstraintViolationException(
      ConstraintViolationException e,
      HttpServletRequest request) {

    String errorMessage = e.getConstraintViolations().stream()
        .map(violation ->
            violation.getPropertyPath() + ": " + violation.getMessage())
        .collect(Collectors.joining(", "));

    ErrorMessageResource error = new ErrorMessageResource(
        LocalDateTime.now().toString(),
        HttpStatus.BAD_REQUEST.value(),
        HttpStatus.BAD_REQUEST.getReasonPhrase(),
        "CONSTRAINT_VIOLATION",
        errorMessage,
        request.getRequestURI()
    );

    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(error);
  }
}