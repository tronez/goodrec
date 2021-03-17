package com.goodrec.recipe.domain;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface CategoryRepository extends org.springframework.data.repository.Repository<Category, String> {

    Category save(Category recipe);

    boolean existsById(String id);

    List<Category> findAll();
}