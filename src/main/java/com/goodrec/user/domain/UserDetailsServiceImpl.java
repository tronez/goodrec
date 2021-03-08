package com.goodrec.user.domain;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserPrincipal loadUserByUsername(String username) {
        return userRepository
                .findByEmail(username)
                .map(UserPrincipal::createFrom)
                .orElseThrow(() -> new UsernameNotFoundException("User was not found: " + username));
    }

    public UserPrincipal loadUserByUUID(UUID uuid) {
        return userRepository
                .findById(uuid)
                .map(UserPrincipal::createFrom)
                .orElseThrow(() -> new UsernameNotFoundException("User was not found: " + uuid.toString()));
    }
}
