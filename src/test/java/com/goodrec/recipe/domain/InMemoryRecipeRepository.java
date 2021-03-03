package com.goodrec.recipe.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Objects.requireNonNull;

public class InMemoryRecipeRepository implements RecipeRepository {
    private ConcurrentHashMap<UUID, Recipe> map = new ConcurrentHashMap<>();

    @Override
    public Recipe save(Recipe recipe) {
        requireNonNull(recipe);
        map.put(recipe.getId(), recipe);
        return recipe;
    }

    @Override
    public Optional<Recipe> findById(UUID uuid) {

        final Recipe recipe = map.get(uuid);
        return Optional.ofNullable(recipe);
    }

    @Override
    public Page<Recipe> findAll(Pageable pageable) {
        return new PageImpl<>(new ArrayList<>(map.values()), pageable, map.size());
    }

    @Override
    public void deleteById(UUID uuid) {
        map.remove(uuid);
    }
}
