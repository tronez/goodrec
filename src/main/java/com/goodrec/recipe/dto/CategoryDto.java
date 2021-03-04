package com.goodrec.recipe.dto;

import com.goodrec.recipe.domain.CategoryEnum;

public class CategoryDto {

    private CategoryEnum name;

    public CategoryDto() {
    }

    public CategoryDto(CategoryEnum name) {
        this.name = name;
    }

    public String getName() {
        return name.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CategoryDto that = (CategoryDto) o;

        return name == that.name;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
