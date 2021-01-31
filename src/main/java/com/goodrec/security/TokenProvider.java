package com.goodrec.security;

import org.springframework.security.core.Authentication;

import java.util.UUID;

public interface TokenProvider {

    String createToken(Authentication authentication);

    UUID getUserUUIDFromToken(String token);

    boolean validateToken(String authToken);
}
