package com.goodrec.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goodrec.recipe.dto.RecipeDto;
import com.goodrec.security.TokenProvider;
import com.goodrec.testdata.MultipartFileCreator;
import com.goodrec.testdata.PrincipalCreator;
import com.goodrec.testdata.RecipeCreator;
import com.goodrec.testdata.RecipeTestDataFactory;
import com.goodrec.user.domain.UserPrincipal;
import com.jayway.jsonpath.JsonPath;
import org.apache.tomcat.util.codec.binary.Base64;
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
import static java.lang.String.format;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithUserDetails("kamil@gmail.com")
class TestRecipeApi {

    private final MockMvc mockMvc;

    private final ObjectMapper mapper;

    private final TokenProvider tokenProvider;

    private final RecipeTestDataFactory recipeTestDataFactory;

    @Autowired
    public TestRecipeApi(MockMvc mockMvc, ObjectMapper mapper, TokenProvider tokenProvider,
                         RecipeTestDataFactory recipeTestDataFactory) {
        this.mockMvc = mockMvc;
        this.mapper = mapper;
        this.tokenProvider = tokenProvider;
        this.recipeTestDataFactory = recipeTestDataFactory;
    }

    private static MockMultipartFile imageMultipart;
    private static UserPrincipal testPrincipal;
    private static String token;

    @BeforeAll
    static void beforeAll() {
        testPrincipal = PrincipalCreator.principalOf(UUID.fromString("fcbaf08f-7c6c-454a-90f1-c204627ac49b"),
                "kamil@gmail.com",
                "kamil12345");

        imageMultipart = MultipartFileCreator.fromFile(
                "image",
                IMAGE_JPEG_VALUE,
                "src/test/resources/images/22996.jpg");
    }

    @BeforeEach
    void setUp() {
        token = tokenProvider.createToken(testPrincipal);
    }

    @Test
    @DisplayName("Should return 201 status with location header when creating a recipe")
    void testCreateSuccess() throws Exception {
        var recipeRequest = RecipeCreator.createNewRequest();
        var requestMultipart = MultipartFileCreator.simpleFile(
                "request",
                APPLICATION_JSON_VALUE,
                mapper.writeValueAsBytes(recipeRequest));

        MvcResult result = mockMvc.perform(multipart("/api/recipes")
                .file(imageMultipart)
                .file(requestMultipart)
                .header(HEADER_STRING, TOKEN_PREFIX + token))
                .andExpect(status().isCreated())
                .andExpect(header().string("location", containsString("/api/recipes/")))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        String imageResponse = JsonPath.read(content, "$.imageBase64");
        RecipeDto response = mapper.readValue(content, RecipeDto.class);
        assertAll(
                () -> assertNotNull(response.getUuid(), "Recipe id can't be null"),
                () -> assertEquals(recipeRequest.getName(), response.getName(), "Recipe name update isn't applied"),
                () -> assertArrayEquals(imageMultipart.getBytes(), response.getImageBase64(), "Malformed image"),
                () -> assertTrue(Base64.isBase64(imageResponse), "Returned image in response should be in base64")
        );
    }

    @Test
    @DisplayName("Should return 400 status with proper error message when creating recipe with invalid request")
    void testCreateFail() throws Exception {
        var recipeRequest = RecipeCreator.createBadRequest();
        var requestMultipart = MultipartFileCreator.simpleFile(
                "request",
                APPLICATION_JSON_VALUE,
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
        RecipeDto expected = recipeTestDataFactory.createSimpleRecipe(token);
        var uuid = expected.getUuid();

        MvcResult result = mockMvc.perform(get("/api/recipes/{uuid}", uuid))
                .andExpect(status().isOk())
                .andReturn();

        String expectedJson = mapper.writeValueAsString(expected);
        assertEquals(expectedJson, result.getResponse().getContentAsString(), "Recipes should be equal");
    }

    @Test
    @DisplayName("Should return 404 status and proper error message when fetching non existing recipe")
    void testGetNotFound() throws Exception {
        var uuid = UUID.fromString("1bb7aa99-d5cf-49b2-b087-2a080ae6171a");

        var expectedMessage = format("Resource Recipe with uuid %s was not found", uuid);
        mockMvc.perform(get("/api/recipes/{uuid}", uuid))
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
        RecipeDto recipe = recipeTestDataFactory.createSimpleRecipe(token);
        var uuid = recipe.getUuid();

        mockMvc.perform(delete("/api/recipes/{uuid}", uuid)
                .header(HEADER_STRING, TOKEN_PREFIX + token))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/recipes/{uuid}", uuid))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return 404 status and proper error message when deleting non existing recipe")
    void testDeleteNotFound() throws Exception {
        var uuid = UUID.fromString("1aa7bb99-d5cf-49b2-b087-2a080ea6171a");

        var expectedMessage = format("Resource Recipe with uuid %s was not found", uuid);
        mockMvc.perform(delete("/api/recipes/{uuid}", uuid)
                .header(HEADER_STRING, TOKEN_PREFIX + token))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString(expectedMessage)));
    }

    @Test
    @DisplayName("Should return 200 status when updating existing recipe")
    void testUpdateSuccess() throws Exception {
        RecipeDto recipe = recipeTestDataFactory.createSimpleRecipe(token);
        var uuid = recipe.getUuid();
        var updatedRecipe = RecipeCreator.createUpdated();

        MvcResult updateResult = mockMvc.perform(patch("/api/recipes/{uuid}", uuid)
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(updatedRecipe))
                .header(HEADER_STRING, TOKEN_PREFIX + token))
                .andExpect(status().isOk())
                .andReturn();

        MvcResult getResult = mockMvc
                .perform(get("/api/recipes/{uuid}", uuid))
                .andReturn();
        var expectedJson = updateResult.getResponse().getContentAsString();
        var actualJson = getResult.getResponse().getContentAsString();
        assertEquals(expectedJson, actualJson, "Update was not applied. Json content should be equal");
    }

    @Test
    @DisplayName("Should return 400 when updating recipe with invalid data")
    void testUpdateBadRequestInvalidResource() throws Exception {
        RecipeDto recipe = recipeTestDataFactory.createSimpleRecipe(token);
        var uuid = recipe.getUuid();
        var invalidRecipe = RecipeCreator.createInvalid();

        mockMvc.perform(patch("/api/recipes/{uuid}", uuid)
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(invalidRecipe))
                .header(HEADER_STRING, TOKEN_PREFIX + token))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Method argument validation failed")));
    }

    @Test
    @DisplayName("Should return 404 when updating non existing recipe")
    void testUpdateNotFoundRecipeNotFound() throws Exception {
        var uuid = UUID.fromString("1bb7aa99-d5cf-49b2-b087-2a080ae6171a");
        var updatedRecipe = RecipeCreator.createUpdated();

        var expectedMessage = format("Resource Recipe with uuid %s was not found", uuid);
        mockMvc.perform(patch("/api/recipes/{uuid}", uuid)
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(updatedRecipe))
                .header(HEADER_STRING, TOKEN_PREFIX + token))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString(expectedMessage)));
    }

    @Test
    @DisplayName("Should return 200 when updating recipe's image")
    void testUpdateImageSuccess() throws Exception {
        RecipeDto recipeInSystem = recipeTestDataFactory.createSimpleRecipe(token);
        var uuid = recipeInSystem.getUuid();
        var updatedImage = MultipartFileCreator.fromFile(
                "image",
                IMAGE_JPEG_VALUE,
                "src/test/resources/images/orange.jpg");

        MvcResult result = mockMvc.perform(multipart("/api/recipes/{uuid}/images", uuid)
                .file(updatedImage)
                .contentType(MULTIPART_FORM_DATA)
                .header(HEADER_STRING, TOKEN_PREFIX + token)
                .with(request -> {
                    request.setMethod("PATCH");
                    return request;
                }))
                .andExpect(status().isOk())
                .andReturn();

        var actualRecipe = mapper.readValue(result.getResponse().getContentAsString(), RecipeDto.class);
        assertArrayEquals(updatedImage.getBytes(), actualRecipe.getImageBase64(),
                "images should be equal. Image update was not applied");
    }

    @Test
    @DisplayName("Should return 404 when updating non existing recipe")
    void testUpdateImageNotFound() throws Exception {
        var uuid = UUID.fromString("1bb7aa99-d5cf-49b2-b087-2a080ae6171a");
        var updatedImage = MultipartFileCreator.fromFile(
                "image",
                IMAGE_JPEG_VALUE,
                "src/test/resources/images/orange.jpg");

        var expectedMessage = format("Resource Recipe with uuid %s was not found", uuid);
        mockMvc.perform(multipart("/api/recipes/{uuid}/images", uuid)
                .file(updatedImage)
                .contentType(MULTIPART_FORM_DATA)
                .header(HEADER_STRING, TOKEN_PREFIX + token)
                .with(request -> {
                    request.setMethod("PATCH");
                    return request;
                }))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString(expectedMessage)));
    }
}