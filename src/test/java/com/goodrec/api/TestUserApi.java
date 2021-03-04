package com.goodrec.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goodrec.exception.BadRequestException;
import com.goodrec.exception.ResourceNotFoundException;
import com.goodrec.security.TokenProvider;
import com.goodrec.testdata.UserCreator;
import com.goodrec.user.UserController;
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

import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(UserController.class)
class TestUserApi {

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
    @DisplayName("Should return CREATED status with location header when registering user")
    void userRegistration_success() throws Exception {
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
    @DisplayName("Should return BAD REQUEST status when client tried to register with taken email")
    void userRegistration_fail_takenEmail() throws Exception {
//        given
        RegisterRequest request = UserCreator.createRegisterRequest();
        willThrow(new BadRequestException("Email address already in use")).given(userService).create(any());
//        when
        ResultActions registerUser = performRegistrationWith(request);
//        then
        registerUser.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return BAD REQUEST status and error messages when trying to register with wrong data")
    void userRegistration_fail_invalidData() throws Exception {
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

    @Test
    @DisplayName("Should return OK status and body containing user information when getting existing user")
    void getUserByUUID_success() throws Exception {
//        given
        UserResponse response = UserCreator.createResponse();
        UUID userUuid = response.getUuid();
        String jsonResponse = mapper.writeValueAsString(response);
        given(userService.findByUUID(userUuid)).willReturn(response);
//        when
        ResultActions getUser = mockMvc.perform(get("/api/users/" + userUuid.toString()));
//        then
        getUser
                .andExpect(status().isOk())
                .andExpect(content().string(jsonResponse));
    }

    @Test
    @DisplayName("Should return NOT FOUND status when there is no user in system")
    void getUserByUUID_fail_nonExistingUser() throws Exception {
//        given
        UserResponse response = UserCreator.createResponse();
        UUID responseUuid = response.getUuid();
        willThrow(new ResourceNotFoundException("Not found")).given(userService).findByUUID(responseUuid);
//        when
        ResultActions getUser = mockMvc.perform(get("/api/users" + responseUuid.toString()));
//        then
        getUser.andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return OK status and body containing user information when getting existing user")
    void getUserByEmail_success() throws Exception {
//        given
        UserResponse response = UserCreator.createResponse();
        String jsonResponse = mapper.writeValueAsString(response);
        given(userService.findByEmail(response.getEmail())).willReturn(response);
//        when
        ResultActions getUser = mockMvc.perform(get("/api/users?email=" + response.getEmail()));
//        then
        getUser
                .andExpect(status().isOk())
                .andExpect(content().string(jsonResponse));
    }

    @Test
    @DisplayName("Should return NOT FOUND status when there is no user in system")
    void getUserByEmail_fail_nonExistingUser() throws Exception {
//        given
        UserResponse response = UserCreator.createResponse();
        willThrow(new ResourceNotFoundException("Not found")).given(userService).findByEmail(response.getEmail());
//        when
        ResultActions getUser = mockMvc.perform(get("/api/users?email=" + response.getEmail()));
//        then
        getUser.andExpect(status().isNotFound());
    }

    private ResultActions performRegistrationWith(RegisterRequest request) throws Exception {
        return mockMvc.perform(post("/api/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)));
    }
}