package com.goodrec.recipe.domain;

import com.goodrec.config.logging.Log;
import com.goodrec.exception.ResourceNotFoundException;
import com.goodrec.recipe.dto.RecipeDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Log
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

    public RecipeDto getByUUID(UUID uuid) {
        return recipeRepository
                .findById(uuid)
                .map(Recipe::toDto)
                .orElseThrow(() -> new ResourceNotFoundException(Recipe.class, uuid));
    }

    public void deleteByUUID(UUID recipeUUID) {
        recipeRepository.deleteById(recipeUUID);
    }

    public Page<RecipeDto> findAll(Pageable pageable) {
        return recipeRepository
                .findAll(pageable)
                .map(Recipe::toDto);
    }
}
