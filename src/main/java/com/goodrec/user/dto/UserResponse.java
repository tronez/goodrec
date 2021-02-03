package com.goodrec.user.dto;

import java.util.UUID;

public class UserResponse {

    private UUID uuid;
    private String email;
    private String password;

    public UserResponse() {

    }

    public UserResponse(UUID uuid, String email, String password) {
        this.uuid = uuid;
        this.email = email;
        this.password = password;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

}
