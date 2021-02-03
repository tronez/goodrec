package com.goodrec.user.domain;

import com.goodrec.user.dto.RegisterRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private final PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
    private final UserRepository userRepository = Mockito.mock(UserRepository.class);

    private final UserService cut = new UserService(passwordEncoder, userRepository);


    @Test
    @DisplayName("Should successfully create new user")
    void createUser() {
//        given
        final RegisterRequest registerRequest = new RegisterRequest("simple@gmail.com", "1234");
//        when
//        then
    }
}