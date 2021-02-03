package com.goodrec.config;

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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<Map<String, Set<String>>> handleValidationException(MethodArgumentNotValidException exception) {
        final List<FieldError> errorList = exception.getBindingResult().getFieldErrors();

        final Map<String, Set<String>> errorMap = errorList.stream()
                .collect(Collectors.groupingBy(FieldError::getField,
                        Collectors.mapping(FieldError::getDefaultMessage, Collectors.toSet())));

        return ResponseEntity
                .badRequest()
                .body(errorMap);
    }
}
