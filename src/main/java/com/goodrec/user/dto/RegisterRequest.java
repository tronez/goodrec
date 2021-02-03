package com.goodrec.user.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

public class RegisterRequest {

    @Email(message = "Invalid email format")
    private String email;
    @Size(min = 4, max = 30, message = "Password must be between 4 and 20 characters")
    private String password;

    public RegisterRequest() {
    }

    public RegisterRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
