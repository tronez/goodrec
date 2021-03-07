package com.goodrec.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goodrec.security.TokenProvider;
import com.goodrec.testdata.UserCreator;
import com.goodrec.user.dto.RegisterRequest;
import com.goodrec.user.dto.UserResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class TestUserApi {

    private MockMvc mockMvc;

    private ObjectMapper mapper;

    private TokenProvider tokenProvider;

    @Autowired
    public TestUserApi(MockMvc mockMvc, ObjectMapper mapper, TokenProvider tokenProvider) {
        this.mockMvc = mockMvc;
        this.mapper = mapper;
        this.tokenProvider = tokenProvider;
    }

    @Test
    @DisplayName("Should return 201 status with location header when registering user")
    void testRegisterSuccess() throws Exception {
        RegisterRequest request = UserCreator.create("simple@gmail.com", "pass1234");

        MvcResult result = mockMvc
                .perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("location", containsString("/api/users/")))
                .andReturn();

        var response = mapper.readValue(result.getResponse().getContentAsString(), UserResponse.class);
        assertEquals(request.getEmail(), response.getEmail(), "User email update isn't applied");
    }

    @Test
    @DisplayName("Should return 400 status when client tried to register with taken email")
    void testRegisterBadRequestTakenMail() throws Exception {
        String takenEmail = "kamil@gmail.com";
        RegisterRequest request = UserCreator.create(takenEmail, "pass12345");

        mockMvc.perform(post("/api/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 400 status and error messages when trying to register with wrong data")
    void testRegisterBadRequestInvalidRequest() throws Exception {
        RegisterRequest request = UserCreator.createRegisterRequestWithInvalidEmail();
        String expectedResponse =
                "\"password\":[\"Password must be between 4 and 20 characters\"]," +
                        "\"email\":[\"Invalid email " + "format\"";

        mockMvc.perform(post("/api/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString(expectedResponse)));
    }

    @Test
    @DisplayName("Should return 200 status and body containing user information when getting existing user")
    void testGetSuccess() throws Exception {
        var existingUserUuid = UUID.fromString("fcbaf08f-7c6c-454a-90f1-c204627ac49b");

        MvcResult result = mockMvc.perform(get("/api/users/{uuid}", existingUserUuid))
                .andExpect(status().isOk())
                .andReturn();

        var response = mapper.readValue(result.getResponse().getContentAsString(), UserResponse.class);
        assertEquals(existingUserUuid, response.getUuid(), "UUIDs should match. Wrong user was fetched");
    }

    @Test
    @DisplayName("Should return 404 status when there is no user identified by given uuid in system")
    void testGetNotFound() throws Exception {
        var uuid = UUID.fromString("aabaf00f-7c6c-454a-90f1-c204627ac49b");

        var expectedMessage = String.format("Resource User with uuid %s was not found", uuid);
        mockMvc.perform(get("/api/users/{uuid}", uuid))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString(expectedMessage)));
    }

    @Test
    @DisplayName("Should return 200 status and body containing user information when getting existing user by email")
    void testGetByEmailSuccess() throws Exception {
        var existingUserMail = "kamil@gmail.com";

        MvcResult result = mockMvc.perform(get("/api/users?email={email}", existingUserMail))
                .andExpect(status().isOk())
                .andReturn();

        var response = mapper.readValue(result.getResponse().getContentAsString(), UserResponse.class);
        assertEquals(existingUserMail, response.getEmail(), "Emails should match. Wrong user was fetched");
    }

    @Test
    @DisplayName("Should return 404 status when there is no user identified by given email in system")
    void TestGetByEmailNotFound() throws Exception {
        var existingUserMail = "emailOfNonExistingUser@gmail.com";

        mockMvc.perform(get("/api/users?email={email}", existingUserMail))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Resource User with identifier " +
                        "emailOfNonExistingUser@gmail.com was not found")));
    }
}