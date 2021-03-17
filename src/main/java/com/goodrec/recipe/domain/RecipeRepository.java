package com.goodrec.recipe.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
interface RecipeRepository extends org.springframework.data.repository.Repository<Recipe, UUID> {

    Recipe save(Recipe recipe);

    Optional<Recipe> findById(UUID uuid);

    Page<Recipe> findAll(Pageable pageable);

    Page<Recipe> findAllByCategoriesContaining(Category category, Pageable pageable);

    void deleteById(UUID uuid);
}
