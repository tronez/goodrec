package com.goodrec.user.domain;

import com.goodrec.user.dto.UserResponse;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document
class User {

    @Id
    private UUID id;
    private String email;
    private String password;

    public User() {
    }

    User(UUID id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    UserResponse toUserResponse() {
        return new UserResponse(id, email, password);
    }

    UUID getId() {
        return id;
    }

    String getEmail() {
        return email;
    }

    String getPassword() {
        return password;
    }

    User setId(UUID id) {
        this.id = id;
        return this;
    }

    User setEmail(String email) {
        this.email = email;
        return this;
    }

    User setPassword(String password) {
        this.password = password;
        return this;
    }
}
