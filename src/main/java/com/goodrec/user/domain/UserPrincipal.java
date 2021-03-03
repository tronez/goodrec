package com.goodrec.user.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

public class UserPrincipal implements UserDetails {

    private final UUID uuid;
    private final String email;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;

    public UserPrincipal(UUID uuid,
                         String email,
                         String password,
                         Collection<? extends GrantedAuthority> authorities) {
        this.uuid = uuid;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    public static UserPrincipal createFrom(User user) {
        var authoritiesList = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));

        return new UserPrincipal(user.getId(),
                user.getEmail(),
                user.getPassword(),
                authoritiesList);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public UUID getUuid() {
        return uuid;
    }
}
