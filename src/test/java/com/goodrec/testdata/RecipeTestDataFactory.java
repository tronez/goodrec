package com.goodrec.testdata;

import com.goodrec.recipe.domain.RecipeFacade;
import com.goodrec.recipe.dto.RecipeDto;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;

@Service
public class RecipeTestDataFactory {

    private RecipeFacade facade;

    public RecipeTestDataFactory(RecipeFacade facade) {
        this.facade = facade;
    }

    public RecipeDto createSimpleRecipe(String token) {
        var recipeRequest = RecipeCreator.createNewRequest();
        var imageMultipart = new MockMultipartFile("image", "base64".getBytes());
        return facade.createRecipe(imageMultipart, recipeRequest, token);
    }
}
