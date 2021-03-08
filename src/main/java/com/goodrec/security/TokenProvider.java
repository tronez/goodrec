package com.goodrec.security;

import com.goodrec.user.domain.UserPrincipal;

import java.util.UUID;

public interface TokenProvider {

    String createToken(UserPrincipal authentication);

    String getJwtFromHeader(String header);

    UUID getUserUUIDFromToken(String token);

    boolean validateToken(String authToken);
}
