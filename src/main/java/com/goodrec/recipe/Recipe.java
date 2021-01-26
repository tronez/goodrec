package com.goodrec.recipe;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;
import java.util.UUID;

@Document
class Recipe {

    @Id
    private UUID uuid;
    private String name;
    private Integer cookingTime;
    private Integer servings;
    private String directions;
    private Difficulty difficulty;
    private Byte[] image;
    private Set<Ingredient> ingredientList;

    @DBRef
    private Set<Category> categories;

    public UUID getUuid() {
        return uuid;
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

    public Byte[] getImage() {
        return image;
    }

    public Set<Ingredient> getIngredientList() {
        return ingredientList;
    }

    public Set<Category> getCategories() {
        return categories;
    }
}
