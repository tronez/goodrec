package com.goodrec.recipe.dto;

import com.goodrec.recipe.domain.Difficulty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

public class NewRecipeRequest {

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
    private List<NewIngredientRequest> ingredientList;
    @NotEmpty
    private List<CategoryDto> categories;

    public NewRecipeRequest() {
    }

    public String getName() {
        return name;
    }

    public NewRecipeRequest setName(String name) {
        this.name = name;
        return this;
    }

    public Integer getCookingTime() {
        return cookingTime;
    }

    public NewRecipeRequest setCookingTime(Integer cookingTime) {
        this.cookingTime = cookingTime;
        return this;
    }

    public Integer getServings() {
        return servings;
    }

    public NewRecipeRequest setServings(Integer servings) {
        this.servings = servings;
        return this;
    }

    public String getDirections() {
        return directions;
    }

    public NewRecipeRequest setDirections(String directions) {
        this.directions = directions;
        return this;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public NewRecipeRequest setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
        return this;
    }

    public List<NewIngredientRequest> getIngredientList() {
        return ingredientList;
    }

    public NewRecipeRequest setIngredientList(List<NewIngredientRequest> ingredientList) {
        this.ingredientList = ingredientList;
        return this;
    }

    public List<CategoryDto> getCategories() {
        return categories;
    }

    public NewRecipeRequest setCategories(List<CategoryDto> categories) {
        this.categories = categories;
        return this;
    }
}
