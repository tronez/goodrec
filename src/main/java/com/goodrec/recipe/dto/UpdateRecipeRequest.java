package com.goodrec.recipe.dto;

import com.goodrec.recipe.domain.Difficulty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

public class UpdateRecipeRequest {

    @NotBlank
    private String name;
    @NotNull
    private Integer cookingTime;
    @NotNull
    private Integer servings;
    @NotBlank
    private String directions;
    @NotNull
    private Difficulty difficulty;
    @NotEmpty
    private List<IngredientDto> ingredientList;
    @NotEmpty
    private List<CategoryDto> categories;

    public UpdateRecipeRequest() {
    }

    public String getName() {
        return name;
    }

    public UpdateRecipeRequest setName(String name) {
        this.name = name;
        return this;
    }

    public Integer getCookingTime() {
        return cookingTime;
    }

    public UpdateRecipeRequest setCookingTime(Integer cookingTime) {
        this.cookingTime = cookingTime;
        return this;
    }

    public Integer getServings() {
        return servings;
    }

    public UpdateRecipeRequest setServings(Integer servings) {
        this.servings = servings;
        return this;
    }

    public String getDirections() {
        return directions;
    }

    public UpdateRecipeRequest setDirections(String directions) {
        this.directions = directions;
        return this;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public UpdateRecipeRequest setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
        return this;
    }

    public List<IngredientDto> getIngredientList() {
        return ingredientList;
    }

    public UpdateRecipeRequest setIngredientList(List<IngredientDto> ingredientList) {
        this.ingredientList = ingredientList;
        return this;
    }

    public List<CategoryDto> getCategories() {
        return categories;
    }

    public UpdateRecipeRequest setCategories(List<CategoryDto> categories) {
        this.categories = categories;
        return this;
    }
}
