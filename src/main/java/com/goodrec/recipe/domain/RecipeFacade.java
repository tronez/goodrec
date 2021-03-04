package com.goodrec.recipe.domain;

import com.goodrec.recipe.dto.CategoryDto;
import com.goodrec.recipe.dto.NewRecipeRequest;
import com.goodrec.recipe.dto.RecipeDto;
import com.goodrec.security.TokenProvider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
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

        if (recipeToDelete.getUserUuid().equals(userUUID)) {
            recipeService.deleteByUUID(recipeUUID);
        }
    }

    public Page<RecipeDto> findAll(Pageable pageable) {
        return recipeService.findAll(pageable);
    }
}
