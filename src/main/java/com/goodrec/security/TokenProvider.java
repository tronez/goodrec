package com.goodrec.security;

import com.goodrec.user.domain.UserPrincipal;
import org.springframework.security.core.Authentication;

import java.util.UUID;

public interface TokenProvider {

    String createToken(UserPrincipal authentication);

    String getJwtFromHeader(String header);

    UUID getUserUUIDFromToken(String token);

    boolean validateToken(String authToken);
}
