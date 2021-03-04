package com.goodrec.testdata;

import com.goodrec.user.domain.UserPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.UUID;

public class PrincipalCreator {

    public static UserPrincipal createSimplePrincipal() {
        return new UserPrincipal(UUID.randomUUID(),
                "kamil@gmail.com",
                "kamil12345",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
    }

    public static UserPrincipal principalOf(UUID uuid, String mail, String password) {
        return new UserPrincipal(uuid,
                mail,
                password,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
    }

    public static UserPrincipal createFrom(String email, String password) {
        return new UserPrincipal(UUID.randomUUID(),
                email,
                password,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
    }
}
