package com.goodrec.config;

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

    @ExceptionHandler(ResourceNotFoundException.class)
    ResponseEntity<ApiError<String>> handleResourceNotFoundException(ResourceNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ApiError<>("Resource was not found", List.of(e.getMessage())));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ApiError<Map<String, Set<String>>>> handleValidationException(MethodArgumentNotValidException exception) {
        final List<FieldError> errorList = exception.getBindingResult().getFieldErrors();

        final Map<String, Set<String>> errorMap = errorList.stream()
                .collect(Collectors.groupingBy(FieldError::getField,
                        Collectors.mapping(FieldError::getDefaultMessage, Collectors.toSet())));

        return ResponseEntity
                .badRequest()
                .body(new ApiError<>("Method argument validation failed", List.of(errorMap)));
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
