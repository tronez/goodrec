package com.goodrec.web;

import com.goodrec.user.dto.RegisterRequest;
import com.goodrec.user.dto.UserResponse;

import java.util.UUID;

class UserCreator {

    static RegisterRequest createRegisterRequest() {
        return new RegisterRequest("simplemail@gmail.com", "simplepassword1");
    }

    static RegisterRequest createRegisterRequestWithInvalidEmail() {
        return new RegisterRequest("invalid", "1");
    }

    static UserResponse createResponseFrom(RegisterRequest request) {
        return new UserResponse(UUID.randomUUID(), request.getEmail(), request.getPassword());
    }

    static UserResponse createResponse() {
        return new UserResponse(UUID.randomUUID(), "simplemail@gmail.com", "simplepassword1");
    }
}
