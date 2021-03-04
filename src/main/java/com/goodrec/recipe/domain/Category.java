package com.goodrec.recipe.domain;

import com.goodrec.recipe.dto.CategoryDto;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

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
        return new Category(dto.getName());
    }

    CategoryDto toDto() {
        return new CategoryDto(CategoryEnum.valueOf(name));
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
