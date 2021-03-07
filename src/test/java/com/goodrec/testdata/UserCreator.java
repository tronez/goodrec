package com.goodrec.testdata;

import com.goodrec.user.dto.RegisterRequest;
import com.goodrec.user.dto.UserResponse;

import java.util.UUID;

public class UserCreator {

    public static RegisterRequest createRegisterRequest() {
        return new RegisterRequest("simplemail@gmail.com", "simplepassword1");
    }

    public static RegisterRequest createRegisterRequestWithInvalidEmail() {
        return new RegisterRequest("invalid", "1");
    }

    public static RegisterRequest create(String email, String password) {
        return new RegisterRequest(email, password);
    }

    public static UserResponse createResponseFrom(RegisterRequest request) {
        return new UserResponse(UUID.randomUUID(), request.getEmail(), request.getPassword());
    }

    public static UserResponse createResponse() {
        return new UserResponse(UUID.randomUUID(), "simplemail@gmail.com", "simplepassword1");
    }
}
