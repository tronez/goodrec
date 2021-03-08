package com.goodrec.recipe.domain;

import com.goodrec.exception.ResourceNotFoundException;
import com.goodrec.recipe.dto.NewRecipeRequest;
import com.goodrec.recipe.dto.RecipeDto;
import com.goodrec.security.TokenProvider;
import com.goodrec.testdata.PrincipalCreator;
import com.goodrec.testdata.RecipeCreator;
import com.goodrec.testdata.TokenCreator;
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
        String token = tokenProvider.createToken(PrincipalCreator.createSimplePrincipal());
        NewRecipeRequest expectedRecipe = RecipeCreator.createNewRequest();

        RecipeDto actualRecipe = cut.createRecipe(image, expectedRecipe, token);

        assertEqualRecipe(expectedRecipe, actualRecipe);
    }

    @Test
    @DisplayName("Should add a recipe with doubled categories and save only one")
    void shouldAddRecipeWithDoubledCategories() throws IOException {
        String token = tokenProvider.createToken(PrincipalCreator.createSimplePrincipal());
        NewRecipeRequest expectedRecipe = RecipeCreator.createWithDoubledCategories();

        RecipeDto actualRecipe = cut.createRecipe(image, expectedRecipe, token);

        assertEquals(1, actualRecipe.getCategories().size());
        assertEqualRecipe(expectedRecipe, actualRecipe);
    }

    @Test
    @DisplayName("Should get recipe by uuid and return correct recipe DTO")
    void shouldGetRecipeByUUIDAndReturnCorrectDto() {
        String token = tokenProvider.createToken(PrincipalCreator.createSimplePrincipal());
        NewRecipeRequest request = RecipeCreator.createNewRequest();
        RecipeDto expectedRecipe = cut.createRecipe(image, request, token);

        RecipeDto actualRecipe = cut.getRecipeByUUID(expectedRecipe.getUuid());

        assertEquals(expectedRecipe, actualRecipe);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when fetching non existing recipe")
    void shouldThrowExceptionWhenFetchingNonExistingRecipe() {
        var uuid = UUID.randomUUID();

        var exception = assertThrows(ResourceNotFoundException.class,
                () -> cut.getRecipeByUUID(uuid));

        var expectedMessage = String.format("Resource Recipe with uuid %s was not found", uuid);
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    @DisplayName("Should delete recipe on correct uuid and if recipe belongs to user")
    void shouldDeleteRecipe() {
        var request = RecipeCreator.createNewRequest();
        String token = tokenProvider.createToken(PrincipalCreator.createSimplePrincipal());
        RecipeDto savedRecipe = cut.createRecipe(image, request, token);

        cut.deleteRecipe(savedRecipe.getUuid(), token);

        var expectedUuid = savedRecipe.getUuid();
        assertThrows(ResourceNotFoundException.class, () -> cut.getRecipeByUUID(expectedUuid));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when deleting non existing recipe")
    void shouldThrowExceptionWhenDeletingNonExistingRecipe() {
        var uuid = UUID.randomUUID();

        var resourceNotFoundException = assertThrows(ResourceNotFoundException.class,
                () -> cut.getRecipeByUUID(uuid));

        var expectedMessage = String.format("Resource Recipe with uuid %s was not found", uuid);
        assertEquals(expectedMessage, resourceNotFoundException.getMessage());
    }

    @Test
    @DisplayName("Should not delete other user recipe")
    void shouldNotDeleteRecipeWhenRecipeDontBelongToUser() {
        var request = RecipeCreator.createNewRequest();
        String token = tokenProvider.createToken(PrincipalCreator.createSimplePrincipal());
        RecipeDto expectedRecipe = cut.createRecipe(image, request, token);
        String otherUserToken = tokenProvider.createToken(PrincipalCreator.createFrom("mail@op.pl", "12345"));

        cut.deleteRecipe(expectedRecipe.getUuid(), otherUserToken);

        var actualRecipe = assertDoesNotThrow(() -> cut.getRecipeByUUID(expectedRecipe.getUuid()));
        assertEquals(expectedRecipe, actualRecipe);
    }

    @Test
    @DisplayName("Should return updated recipe dto when updating recipe")
    void shouldUpdateRecipe() {
        String token = tokenProvider.createToken(PrincipalCreator.createSimplePrincipal());
        RecipeDto savedRecipe = cut.createRecipe(image, RecipeCreator.createNewRequest(), token);
        var recipeUUID = savedRecipe.getUuid();
        var recipeToUpdate = RecipeCreator.createUpdated();

        RecipeDto updatedRecipe = cut.update(recipeUUID, recipeToUpdate, token);

        assertEquals(recipeToUpdate.getName(), updatedRecipe.getName(),
                "Names should be equal. Update was not applied");
    }

    @Test
    @DisplayName("Should throw ResourceNotFound when updating non existing recipe")
    void shouldThrowWhenUpdatingNonExistingRecipe() {
        String token = tokenProvider.createToken(PrincipalCreator.createSimplePrincipal());
        var recipeToUpdate = RecipeCreator.createUpdated();
        var recipeUUID = UUID.fromString("1bb7aa99-d5cf-49b2-b087-2a080ae6171a");

        var resourceNotFoundException = assertThrows(ResourceNotFoundException.class,
                () -> cut.update(recipeUUID, recipeToUpdate, token));

        var expectedMessage = String.format("Resource Recipe with uuid %s was not found", recipeUUID);
        assertEquals(expectedMessage, resourceNotFoundException.getMessage(), "Wrong exception was thrown");
    }

    @Test
    @DisplayName("Should throw ResourceNotFound when updating other user's recipe")
    void shouldThrowWhenUpdatingOtherUserRecipe() {
        String otherUserToken = tokenProvider.createToken(PrincipalCreator.createSimplePrincipal());
        RecipeDto savedRecipe = cut.createRecipe(image, RecipeCreator.createNewRequest(), otherUserToken);
        var recipeUUID = savedRecipe.getUuid();
        var recipeToUpdate = RecipeCreator.createUpdated();
        String token = tokenProvider.createToken(PrincipalCreator.createFrom("user@gmail.com", "1234"));

        var resourceNotFoundException = assertThrows(ResourceNotFoundException.class,
                () -> cut.update(recipeUUID, recipeToUpdate, token));

        var expectedMessage = String.format("Resource Recipe with uuid %s was not found", recipeUUID);
        assertEquals(expectedMessage, resourceNotFoundException.getMessage(), "Wrong exception was thrown");
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