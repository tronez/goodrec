package com.goodrec.recipe.domain;

import com.goodrec.user.domain.UserPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.UUID;

class UserAuthFactory {

    static UserPrincipal createSimplePrincipal() {
        return new UserPrincipal(UUID.randomUUID(),
                "kamil@gmail.com",
                "123456",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
    }

    static UserPrincipal createFrom(String email, String password) {
        return new UserPrincipal(UUID.randomUUID(),
                email,
                password,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
    }
}
