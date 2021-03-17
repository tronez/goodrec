package com.goodrec.config;

import com.goodrec.exception.ImageExtensionNotSupportedException;
import com.goodrec.exception.RegistrationEmailInUseException;
import com.goodrec.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@ControllerAdvice
class ExceptionResponseAdvice {

    @ExceptionHandler(RuntimeException.class)
    ResponseEntity<ApiError<String>> handle(RuntimeException e) {
        final var exceptionMessage = new ApiError<>("Something went wrong", List.of(e.getMessage()));

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(exceptionMessage);
    }

    @ExceptionHandler(RegistrationEmailInUseException.class)
    ResponseEntity<ApiError<String>> handle(RegistrationEmailInUseException e) {
        final var exceptionMessage = new ApiError<>("Could not register", List.of(e.getMessage()));

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(exceptionMessage);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    ResponseEntity<ApiError<String>> handleResourceNotFoundException(ResourceNotFoundException e) {
        final var exceptionMessage = new ApiError<>("Resource was not found", List.of(e.getMessage()));

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(exceptionMessage);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ApiError<Map<String, Set<String>>>> handleValidationException(MethodArgumentNotValidException e) {
        final List<FieldError> errorList = e.getBindingResult().getFieldErrors();

        final Map<String, Set<String>> errorMap = errorList.stream()
                .collect(Collectors.groupingBy(FieldError::getField,
                        Collectors.mapping(FieldError::getDefaultMessage, Collectors.toSet())));

        final var exceptionMessage = new ApiError<>("Method argument validation failed", List.of(errorMap));
        return ResponseEntity
                .badRequest()
                .body(exceptionMessage);
    }

    @ExceptionHandler(ImageExtensionNotSupportedException.class)
    ResponseEntity<ApiError<String>> handleImageExtensionNotSupported(ImageExtensionNotSupportedException e) {
        final var exceptionMessage = new ApiError<>("Image extension not supported", List.of(e.getMessage()));

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(exceptionMessage);
    }

    static class ApiError<T> {
        private String message;
        private List<T> details;

        public ApiError(String message, List<T> details) {
            this.message = message;
            this.details = details;
        }

        public String getMessage() {
            return message;
        }

        public List<T> getDetails() {
            return details;
        }
    }
}
