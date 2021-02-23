package com.goodrec.recipe.domain;

import java.util.concurrent.ConcurrentHashMap;

import static java.util.Objects.requireNonNull;

public class InMemoryCategoryRepository implements CategoryRepository{
    private ConcurrentHashMap<String, Category> map = new ConcurrentHashMap<>();

    @Override
    public Category save(Category recipe) {
        requireNonNull(recipe);
        map.put(recipe.getName(), recipe);
        return recipe;
    }

    @Override
    public boolean existsById(String id) {
        return map.containsKey(id);
    }

}
