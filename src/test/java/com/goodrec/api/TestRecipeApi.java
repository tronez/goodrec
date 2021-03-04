package com.goodrec.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goodrec.recipe.dto.RecipeDto;
import com.goodrec.security.TokenProvider;
import com.goodrec.testdata.PrincipalCreator;
import com.goodrec.testdata.RecipeCreator;
import com.goodrec.testdata.RecipeTestDataFactory;
import com.goodrec.user.domain.UserPrincipal;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static com.goodrec.security.JwtConstants.HEADER_STRING;
import static com.goodrec.security.JwtConstants.TOKEN_PREFIX;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithUserDetails("kamil@gmail.com")
class TestRecipeApi {

    private MockMvc mockMvc;

    private ObjectMapper mapper;

    private TokenProvider tokenProvider;

    private RecipeTestDataFactory recipeTestDataFactory;

    @Autowired
    public TestRecipeApi(MockMvc mockMvc, ObjectMapper mapper, TokenProvider tokenProvider,
                         RecipeTestDataFactory recipeTestDataFactory) {
        this.mockMvc = mockMvc;
        this.mapper = mapper;
        this.tokenProvider = tokenProvider;
        this.recipeTestDataFactory = recipeTestDataFactory;
    }

    private MockMultipartFile imageMultipart = new MockMultipartFile("image", "base64".getBytes());
    private static UserPrincipal testPrincipal;
    private static String token;

    @BeforeAll
    static void beforeAll() {
        testPrincipal = PrincipalCreator.principalOf(UUID.fromString("fcbaf08f-7c6c-454a-90f1-c204627ac49b"),
                "kamil@gmail.com",
                "kamil12345");
    }

    @BeforeEach
    void setUp() {
        token = tokenProvider.createToken(testPrincipal);
    }

    @Test
    @DisplayName("Should return 201 status with location header when creating a recipe")
    void testCreateSuccess() throws Exception {
        var recipeRequest = RecipeCreator.createNewRequest();
        var requestMultipart = new MockMultipartFile("request", "request", "application/json",
                mapper.writeValueAsBytes(recipeRequest));

        MvcResult result = mockMvc.perform(multipart("/api/recipes")
                .file(imageMultipart)
                .file(requestMultipart)
                .header(HEADER_STRING, TOKEN_PREFIX + token))
                .andExpect(status().isCreated())
                .andExpect(header().string("location", containsString("/api/recipes/")))
                .andReturn();

        RecipeDto response = mapper.readValue(result.getResponse().getContentAsString(), RecipeDto.class);
        assertNotNull(response.getUuid(), "Recipe id can't be null");
        assertEquals(recipeRequest.getName(), response.getName(), "Recipe name update isn't applied");
    }

    @Test
    @DisplayName("Should return 400 status with proper error message when creating recipe with invalid request")
    void testCreateFail() throws Exception {
        var recipeRequest = RecipeCreator.createBadRequest();
        var requestMultipart = new MockMultipartFile("request", "request", "application/json",
                mapper.writeValueAsBytes(recipeRequest));

        mockMvc.perform(multipart("/api/recipes")
                .file(imageMultipart)
                .file(requestMultipart)
                .header(HEADER_STRING, TOKEN_PREFIX + token))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Method argument validation failed")));
    }

    @Test
    @DisplayName("Should return 200 status and matching recipe when fetching recipe")
    void testGetSuccess() throws Exception {
        var expected = recipeTestDataFactory.createSimpleRecipe(token);

        MvcResult result = mockMvc.perform(get("/api/recipes/" + expected.getUuid()))
                .andExpect(status().isOk())
                .andReturn();

        String expectedJson = mapper.writeValueAsString(expected);
        assertEquals(expectedJson, result.getResponse().getContentAsString(), "Recipes should be equal");
    }

    @Test
    @DisplayName("Should return 404 status and proper error message when fetching non existing recipe")
    void testGetNotFound() throws Exception {
        String expectedMessage = "Resource Recipe with uuid 1bb7aa99-d5cf-49b2-b087-2a080ae6171a was not found";
        var uuid = UUID.fromString("1bb7aa99-d5cf-49b2-b087-2a080ae6171a");

        mockMvc.perform(get("/api/recipes/" + uuid))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString(expectedMessage)));
    }

    @Test
    @DisplayName("Should return 200 status when fetching page of recipes")
    void testGetPageSuccess() throws Exception {
        mockMvc.perform(get("/api/recipes"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return 204 status when deleting recipe")
    void testDeleteSuccess() throws Exception {
        var recipe = recipeTestDataFactory.createSimpleRecipe(token);

        mockMvc.perform(delete("/api/recipes/" + recipe.getUuid())
                .header(HEADER_STRING, TOKEN_PREFIX + token))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/recipes/" + recipe.getUuid()))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return 404 status and proper error message when deleting non existing recipe")
    void testDeleteNotFound() throws Exception {
        var uuid = UUID.fromString("1aa7bb99-d5cf-49b2-b087-2a080ea6171a");
        var expectedMessage = "Resource Recipe with uuid 1aa7bb99-d5cf-49b2-b087-2a080ea6171a was not found";

        mockMvc.perform(delete("/api/recipes/" + uuid)
                .header(HEADER_STRING, TOKEN_PREFIX + token))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString(expectedMessage)));
    }
}