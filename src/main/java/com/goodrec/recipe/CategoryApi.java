package com.goodrec.recipe;

import com.goodrec.recipe.domain.RecipeFacade;
import com.goodrec.recipe.dto.CategoryDto;
import com.goodrec.recipe.dto.RecipeDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryApi {

    private RecipeFacade facade;

    public CategoryApi(RecipeFacade facade) {
        this.facade = facade;
    }

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        return ResponseEntity.ok(facade.getAllCategories());
    }

    @GetMapping("/{category}/recipes")
    public ResponseEntity<Page<RecipeDto>> getAllRecipesByCategory(@PathVariable String category,
                                                                   Pageable pageable) {

        final Page<RecipeDto> page = facade.findAllByCategory(category, pageable);
        return ResponseEntity.ok(page);
    }
}
