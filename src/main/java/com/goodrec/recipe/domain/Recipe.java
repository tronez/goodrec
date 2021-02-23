package com.goodrec.recipe.domain;

import com.goodrec.recipe.dto.CategoryDto;
import com.goodrec.recipe.dto.IngredientDto;
import com.goodrec.recipe.dto.RecipeDto;
import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Document
class Recipe {

    @Id
    private UUID uuid;
    private UUID userUuid;
    private String name;
    private Integer cookingTime;
    private Integer servings;
    private String directions;
    private Difficulty difficulty;
    private List<Ingredient> ingredientList;

    @DBRef
    private List<Category> categories;
    private Binary image;

    public Recipe() {
    }

    public Recipe(UUID uuid, UUID userUuid, String name, Integer cookingTime, Integer servings, String directions,
                  Difficulty difficulty, List<Ingredient> ingredientList, List<Category> categories, Binary image) {
        this.uuid = uuid;
        this.userUuid = userUuid;
        this.name = name;
        this.cookingTime = cookingTime;
        this.servings = servings;
        this.directions = directions;
        this.difficulty = difficulty;
        this.ingredientList = ingredientList;
        this.categories = categories;
        this.image = image;
    }

    public static Recipe createFrom(RecipeDto request) {
        final List<Ingredient> ingredients = request.getIngredientList().stream()
                .map(Ingredient::createFrom)
                .collect(Collectors.toList());

        final List<Category> categories = request.getCategories().stream()
                .map(Category::createFrom)
                .collect(Collectors.toList());

        return new Recipe(
                request.getUuid(),
                request.getUserUuid(),
                request.getName(),
                request.getCookingTime(),
                request.getServings(),
                request.getDirections(),
                request.getDifficulty(),
                ingredients,
                categories,
                request.getImageBase64());


    }

    RecipeDto toDto() {

        final List<IngredientDto> ingredientDtos = getIngredientList().stream()
                .map(Ingredient::toDto)
                .collect(Collectors.toList());

        final List<CategoryDto> categoryDtos = getCategories().stream()
                .map(Category::toDto)
                .collect(Collectors.toList());

        return new RecipeDto(uuid,
                userUuid,
                name,
                cookingTime,
                servings,
                directions,
                difficulty,
                image,
                ingredientDtos,
                categoryDtos);
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

    public Binary getImage() {
        return image;
    }

    public List<Ingredient> getIngredientList() {
        return ingredientList;
    }

    public List<Category> getCategories() {
        return categories;
    }
}
