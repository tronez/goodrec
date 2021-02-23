package com.goodrec.recipe.domain;

import com.goodrec.exception.ResourceNotFoundException;
import com.goodrec.recipe.dto.RecipeDto;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
class RecipeService {

    private final RecipeRepository recipeRepository;

    RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    RecipeDto saveRecipe(RecipeDto recipeDto) {

        final Recipe recipe = Recipe.createFrom(recipeDto);

        return recipeRepository
                .save(recipe)
                .toDto();
    }

    public RecipeDto getByUUID(UUID recipeUUID) {
        return recipeRepository
                .findById(recipeUUID)
                .map(Recipe::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe was not found"));
    }

    public void deleteByUUID(UUID recipeUUID) {
        recipeRepository.deleteById(recipeUUID);
    }
}
