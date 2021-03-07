package com.goodrec.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(Class<?> clazz, UUID uuid) {
        super(String.format("Resource %s with uuid %s was not found", clazz.getSimpleName(), uuid.toString()));
    }

    public ResourceNotFoundException(Class<?> clazz, String identifier) {
        super(String.format("Resource %s with identifier %s was not found", clazz.getSimpleName(), identifier));
    }
}
