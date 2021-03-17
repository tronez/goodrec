package com.goodrec.recipe.domain;

import com.google.common.collect.ImmutableList;

import java.util.List;
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

    @Override
    public List<Category> findAll() {
        return ImmutableList.copyOf(map.values());
    }

}
