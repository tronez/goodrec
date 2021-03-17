package com.goodrec.recipe.domain;

import com.goodrec.config.logging.Log;
import com.goodrec.recipe.dto.CategoryDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Log
class CategoryService {

    private final CategoryRepository repository;

    CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    CategoryDto saveRecipe(CategoryDto dto) {

        if (repository.existsById(dto.getName())) {
            return dto;
        }

        final Category category = Category.createFrom(dto);
        return repository
                .save(category)
                .toDto();
    }

    List<CategoryDto> saveAll(List<CategoryDto> categoryDtos) {

        return categoryDtos.stream()
                .map(this::saveRecipe)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<CategoryDto> getAllCategories() {
        return repository.findAll().stream()
                .map(Category::toDto)
                .collect(Collectors.toList());
    }
}
