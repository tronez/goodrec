package com.goodrec.recipe.domain;

import com.goodrec.config.logging.Log;
import com.goodrec.exception.ResourceNotFoundException;
import com.goodrec.recipe.dto.CategoryDto;
import com.goodrec.recipe.dto.NewRecipeRequest;
import com.goodrec.recipe.dto.RecipeDto;
import com.goodrec.recipe.dto.UpdateRecipeRequest;
import com.goodrec.security.TokenProvider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
@Log
public class RecipeFacade {

    private final RecipeService recipeService;
    private final CategoryService categoryService;
    private final TokenProvider tokenProvider;

    public RecipeFacade(RecipeService recipeService, CategoryService categoryService, TokenProvider tokenProvider) {
        this.recipeService = recipeService;
        this.categoryService = categoryService;
        this.tokenProvider = tokenProvider;
    }

    public RecipeDto createRecipe(MultipartFile file, NewRecipeRequest newRecipeRequest, String token) {

        final byte[] image = FileManager.getBytes(file);
        final List<CategoryDto> savedCategories = categoryService.saveAll(newRecipeRequest.getCategories());
        newRecipeRequest.setCategories(savedCategories);
        final UUID userUUID = tokenProvider.getUserUUIDFromToken(token);
        final RecipeDto dto = RecipeDto.createFrom(image, newRecipeRequest, userUUID);

        return recipeService.saveRecipe(dto);
    }

    public RecipeDto getRecipeByUUID(UUID recipeUUID) {

        return recipeService.getByUUID(recipeUUID);
    }

    public void deleteRecipe(UUID recipeUUID, String token) {
        final UUID userUUID = tokenProvider.getUserUUIDFromToken(token);
        final RecipeDto recipeToDelete = getRecipeByUUID(recipeUUID);

        if (recipeToDelete.belongsToUser(userUUID)) {
            recipeService.deleteByUUID(recipeUUID);
        }
    }

    public Page<RecipeDto> findAll(Pageable pageable) {
        return recipeService.findAll(pageable);
    }

    public RecipeDto update(UUID recipeUUID, UpdateRecipeRequest updatedRecipe, String token) {
        final UUID userUUID = tokenProvider.getUserUUIDFromToken(token);
        final RecipeDto existingRecipe = getRecipeByUUID(recipeUUID);

        if (existingRecipe.belongsToUser(userUUID)) {
            var recipe = RecipeDto.createFrom(recipeUUID, userUUID, updatedRecipe, existingRecipe.getImageBase64());
            return recipeService.saveRecipe(recipe);
        }

        throw new ResourceNotFoundException(Recipe.class, recipeUUID);
    }

    public RecipeDto updateRecipeImage(UUID recipeUUID, MultipartFile file, String token) {
        final UUID userUUID = tokenProvider.getUserUUIDFromToken(token);
        final RecipeDto recipeDto = getRecipeByUUID(recipeUUID);

        if (recipeDto.belongsToUser(userUUID)) {
            final byte[] imageBytes = FileManager.getBytes(file);
            return recipeService.updateImage(recipeDto, imageBytes);
        }

        throw new ResourceNotFoundException(Recipe.class, recipeUUID);
    }

    public List<CategoryDto> getAllCategories() {
        return categoryService.getAllCategories();
    }

    public Page<RecipeDto> findAllByCategory(String category, Pageable pageable) {
        final CategoryDto dto = CategoryDto.from(category);
        return recipeService.findAllByCategory(dto, pageable);
    }
}
