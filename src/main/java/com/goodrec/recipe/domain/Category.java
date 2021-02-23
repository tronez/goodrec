package com.goodrec.recipe.domain;

import com.goodrec.recipe.dto.CategoryDto;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;
import java.util.UUID;

@Document
class Category {

    @Id
    private String name;

    public Category() {
    }

    public Category(String name) {
        this.name = name;
    }

    static Category createFrom(CategoryDto dto) {
        final String lowercaseName = dto.getName().toLowerCase();
        return new Category(lowercaseName);
    }

    CategoryDto toDto() {
        return new CategoryDto(name);
    }

    @DBRef
    private Set<Recipe> recipes;

    public String getName() {
        return name;
    }

    public Set<Recipe> getRecipes() {
        return recipes;
    }

}
