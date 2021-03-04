package com.goodrec.recipe.domain;

import com.goodrec.testdata.RecipeCreator;
import com.goodrec.testdata.TokenCreator;
import com.goodrec.testdata.PrincipalCreator;
import com.goodrec.exception.ResourceNotFoundException;
import com.goodrec.recipe.dto.NewRecipeRequest;
import com.goodrec.recipe.dto.RecipeDto;
import com.goodrec.security.TokenProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RecipeFacadeTest {

    private static final String RESOURCE_NOT_FOUND_MESSAGE = "Recipe was not found";
    private RecipeService recipeService = new RecipeService(new InMemoryRecipeRepository());
    private CategoryService categoryService = new CategoryService(new InMemoryCategoryRepository());
    private TokenProvider tokenProvider = new TokenCreator();
    private RecipeFacade cut = new RecipeFacade(recipeService, categoryService, tokenProvider);

    private MultipartFile image = new MockMultipartFile("Image", "base64".getBytes());

    @BeforeAll
    static void beforeAll() {
        TokenCreator.setTokenSimpleConfig();
    }

    @Test
    @DisplayName("Should add a recipe and return correct DTO")
    void shouldAddRecipe() throws IOException {
//        given
        String token = tokenProvider.createToken(PrincipalCreator.createSimplePrincipal());
        NewRecipeRequest expectedRecipe = RecipeCreator.createNewRequest();
//        when
        RecipeDto actualRecipe = cut.createRecipe(image, expectedRecipe, token);
//        then
        assertEqualRecipe(expectedRecipe, actualRecipe);
    }

    @Test
    @DisplayName("Should add a recipe with doubled categories and save only one")
    void shouldAddRecipeWithDoubledCategories() throws IOException {
//        given
        String token = tokenProvider.createToken(PrincipalCreator.createSimplePrincipal());
        NewRecipeRequest expectedRecipe = RecipeCreator.createWithDoubledCategories();
//        when
        RecipeDto actualRecipe = cut.createRecipe(image, expectedRecipe, token);
//        then
        assertEquals(1, actualRecipe.getCategories().size());
        assertEqualRecipe(expectedRecipe, actualRecipe);
    }

    @Test
    @DisplayName("Should get recipe by uuid and return correct recipe DTO")
    void shouldGetRecipeByUUIDAndReturnCorrectDto() {
//        given
        String token = tokenProvider.createToken(PrincipalCreator.createSimplePrincipal());
        NewRecipeRequest request = RecipeCreator.createNewRequest();
        RecipeDto expectedRecipe = cut.createRecipe(image, request, token);
//        when
        RecipeDto actualRecipe = cut.getRecipeByUUID(expectedRecipe.getUuid());
//        then
        assertEquals(expectedRecipe, actualRecipe);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when fetching non existing recipe")
    void shouldThrowExceptionWhenFetchingNonExistingRecipe() {
//        given
        UUID uuid = UUID.randomUUID();
//        when
        var exception = assertThrows(ResourceNotFoundException.class,
                () -> cut.getRecipeByUUID(uuid));
//        then
        assertEquals(RESOURCE_NOT_FOUND_MESSAGE, exception.getMessage());
    }

    @Test
    @DisplayName("Should delete recipe on correct uuid and if recipe belongs to user")
    void shouldDeleteRecipe() {
//        given
        String token = tokenProvider.createToken(PrincipalCreator.createSimplePrincipal());
        NewRecipeRequest request = RecipeCreator.createNewRequest();
        RecipeDto savedRecipe = cut.createRecipe(image, request, token);
//        when
        cut.deleteRecipe(savedRecipe.getUuid(), token);
//        then
        assertThrows(ResourceNotFoundException.class, () -> cut.getRecipeByUUID(savedRecipe.getUuid()));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when deleting non existing recipe")
    void shouldThrowExceptionWhenDeletingNonExistingRecipe() {
//        given
        UUID uuid = UUID.randomUUID();
//        when
        var resourceNotFoundException = assertThrows(ResourceNotFoundException.class,
                () -> cut.getRecipeByUUID(uuid));
//        then
        assertEquals(RESOURCE_NOT_FOUND_MESSAGE, resourceNotFoundException.getMessage());
    }

    @Test
    @DisplayName("Should not delete other user recipe")
    void shouldNotDeleteRecipeWhenRecipeDontBelongToUser() {
//        given
        String token = tokenProvider.createToken(PrincipalCreator.createSimplePrincipal());
        NewRecipeRequest request = RecipeCreator.createNewRequest();
        RecipeDto expectedRecipe = cut.createRecipe(image, request, token);
        String otherUserToken = tokenProvider.createToken(PrincipalCreator.createFrom("mail@op.pl", "12345"));
//        when
        cut.deleteRecipe(expectedRecipe.getUuid(), otherUserToken);
//        then
        var actualRecipe = assertDoesNotThrow(() -> cut.getRecipeByUUID(expectedRecipe.getUuid()));
        assertEquals(expectedRecipe, actualRecipe);
    }

    void assertEqualRecipe(NewRecipeRequest expected, RecipeDto actual) {
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getDirections(), actual.getDirections());
        assertEquals(expected.getDifficulty(), actual.getDifficulty());
        assertEquals(expected.getCookingTime(), actual.getCookingTime());
        assertEquals(expected.getCategories().size(), actual.getCategories().size());
        assertEquals(expected.getIngredientList().size(), expected.getIngredientList().size());
    }
}