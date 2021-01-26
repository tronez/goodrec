package com.goodrec.recipe;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;
import java.util.UUID;

@Document
class Category {

    private UUID uuid;
    private String name;

    @DBRef
    private Set<Recipe> recipes;

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public Set<Recipe> getRecipes() {
        return recipes;
    }

}
