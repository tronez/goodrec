package com.goodrec.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RegistrationEmailInUseException extends RuntimeException{

    public RegistrationEmailInUseException(String email) {
        super(String.format("Email address: %s is already in use.", email));
    }
}
