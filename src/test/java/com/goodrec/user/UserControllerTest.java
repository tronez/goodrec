package com.goodrec.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goodrec.exception.BadRequestException;
import com.goodrec.security.TokenProvider;
import com.goodrec.user.domain.UserDetailsServiceImpl;
import com.goodrec.user.domain.UserService;
import com.goodrec.user.dto.RegisterRequest;
import com.goodrec.user.dto.UserResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private TokenProvider tokenProvider;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private UserService userService;

    @Test
    @DisplayName("Should successfully create new user and return valid information: status, location")
    void registerCreate() throws Exception {
//        given
        RegisterRequest request = UserCreator.createRegisterRequest();
        UserResponse response = UserCreator.createResponseFrom(request);
        String locationUri = "/api/users/" + response.getUuid().toString();
        given(userService.create(any())).willReturn(response);
//        when
        ResultActions registerUser = performRegistrationWith(request);
//        then
        registerUser
                .andExpect(status().isCreated())
                .andExpect(header().string("location", containsString(locationUri)));
    }

    @Test
    @DisplayName("Should return 400 code when client tried to register with taken email")
    void registerCreate_takenEmail() throws Exception {
//        given
        RegisterRequest request = UserCreator.createRegisterRequest();
        willThrow(new BadRequestException("Email address already in use")).given(userService).create(any());
//        when
        ResultActions registerUser = performRegistrationWith(request);
//        then
        registerUser.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return validation error messages when trying to register with invalid data")
    void registerValidation() throws Exception {
//        given
        RegisterRequest request = UserCreator.createRegisterRequestWithInvalidEmail();
        String expectedResponse =
                "\"password\":[\"Password must be between 4 and 20 characters\"]," +
                        "\"email\":[\"Invalid email " + "format\"";
//        when
        ResultActions registerUser = performRegistrationWith(request);
//        then
        registerUser
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString(expectedResponse)));

    }

    private ResultActions performRegistrationWith(RegisterRequest request) throws Exception {
        return mockMvc.perform(post("/api/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)));
    }
}