package com.goodrec.user.domain;

import com.goodrec.exception.BadRequestException;
import com.goodrec.exception.ResourceNotFoundException;
import com.goodrec.user.dto.RegisterRequest;
import com.goodrec.user.dto.UserResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository repository;

    UserService(PasswordEncoder passwordEncoder, UserRepository repository) {
        this.passwordEncoder = passwordEncoder;
        this.repository = repository;
    }

    public UserResponse create(RegisterRequest registerRequest) {

        if (repository.existsByEmail(registerRequest.getEmail())) {
            throw new BadRequestException("Email address already in use.");
        }

        final String encryptedPassword = passwordEncoder.encode(registerRequest.getPassword());
        final User user = new User(UUID.randomUUID(), registerRequest.getEmail(), encryptedPassword);

        return repository
                .save(user)
                .toUserResponse();
    }

    public UserResponse findByUUID(UUID uuid) {
        return repository
                .findById(uuid)
                .map(User::toUserResponse)
                .orElseThrow(() -> new ResourceNotFoundException(User.class, uuid));
    }

    public UserResponse findByEmail(String email) {

        return repository
                .findByEmail(email)
                .map(User::toUserResponse)
                .orElseThrow(() -> new ResourceNotFoundException(User.class, email));
    }
}
