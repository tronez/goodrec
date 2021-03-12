package com.goodrec.recipe.dto;

import com.goodrec.recipe.domain.Difficulty;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public class RecipeDto {

    private UUID uuid;
    private UUID userUuid;
    private String name;
    private Integer cookingTime;
    private Integer servings;
    private String directions;
    private Difficulty difficulty;
    private byte[] imageBase64;
    private List<IngredientDto> ingredientList;
    private List<CategoryDto> categories;

    public RecipeDto() {
    }

    public RecipeDto(UUID uuid, UUID userUuid, String name, Integer cookingTime, Integer servings, String directions,
                     Difficulty difficulty, byte[] imageBase64, List<IngredientDto> ingredientList,
                     List<CategoryDto> categories) {
        this.uuid = uuid;
        this.userUuid = userUuid;
        this.name = name;
        this.cookingTime = cookingTime;
        this.servings = servings;
        this.directions = directions;
        this.difficulty = difficulty;
        this.imageBase64 = imageBase64;
        this.ingredientList = ingredientList;
        this.categories = categories;
    }

    public static RecipeDto createFrom(byte[] image, NewRecipeRequest newRecipe, UUID userUuid) {
        final List<IngredientDto> ingredients = newRecipe.getIngredientList().stream()
                .map(IngredientDto::createFrom)
                .collect(Collectors.toList());

        return new RecipeDto(UUID.randomUUID(),
                userUuid,
                newRecipe.getName(),
                newRecipe.getCookingTime(),
                newRecipe.getServings(),
                newRecipe.getDirections(),
                newRecipe.getDifficulty(),
                image,
                ingredients,
                newRecipe.getCategories()
        );
    }

    public static RecipeDto createFrom(UUID recipeUUID, UUID userUUID, UpdateRecipeRequest updatedRecipe,
                                       byte[] imageBase64) {

        return new RecipeDto(recipeUUID,
                userUUID,
                updatedRecipe.getName(),
                updatedRecipe.getCookingTime(),
                updatedRecipe.getServings(),
                updatedRecipe.getDirections(),
                updatedRecipe.getDifficulty(),
                imageBase64,
                updatedRecipe.getIngredientList(),
                updatedRecipe.getCategories());
    }

    public boolean belongsToUser(UUID userUuid) {
        return this.userUuid.equals(userUuid);
    }

    public UUID getUuid() {
        return uuid;
    }

    public UUID getUserUuid() {
        return userUuid;
    }

    public String getName() {
        return name;
    }

    public Integer getCookingTime() {
        return cookingTime;
    }

    public Integer getServings() {
        return servings;
    }

    public String getDirections() {
        return directions;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public byte[] getImageBase64() {
        return imageBase64;
    }

    public List<IngredientDto> getIngredientList() {
        return ingredientList;
    }

    public List<CategoryDto> getCategories() {
        return categories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RecipeDto recipeDto = (RecipeDto) o;

        if (!Objects.equals(uuid, recipeDto.uuid)) return false;
        return Objects.equals(userUuid, recipeDto.userUuid);
    }

    @Override
    public int hashCode() {
        int result = uuid != null ? uuid.hashCode() : 0;
        result = 31 * result + (userUuid != null ? userUuid.hashCode() : 0);
        return result;
    }
}
