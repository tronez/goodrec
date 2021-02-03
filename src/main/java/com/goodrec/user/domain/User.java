package com.goodrec.user.domain;

import com.goodrec.user.dto.UserResponse;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document
class User {

    @Id
    private UUID uuid;
    private String email;
    private String password;

    User(UUID uuid, String email, String password) {
        this.uuid = uuid;
        this.email = email;
        this.password = password;
    }

    UserResponse toUserResponse() {
        return new UserResponse(uuid, email, password);
    }

    UUID getUuid() {
        return uuid;
    }

    String getEmail() {
        return email;
    }

    String getPassword() {
        return password;
    }
}
