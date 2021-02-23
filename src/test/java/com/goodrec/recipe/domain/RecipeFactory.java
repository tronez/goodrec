package com.goodrec.recipe.domain;

import com.goodrec.recipe.dto.CategoryDto;
import com.goodrec.recipe.dto.NewIngredientRequest;
import com.goodrec.recipe.dto.NewRecipeRequest;

import java.util.List;

class RecipeFactory {

    public static NewRecipeRequest createNewRequest() {
        var oats = new NewIngredientRequest("oats", 0.5, Unit.CUP);
        var milk = new NewIngredientRequest("milk", 0.5, Unit.CUP);
        var water = new NewIngredientRequest("water", 0.5, Unit.CUP);
        var breakfastCategory = new CategoryDto("breakfast");
        var recipe = new NewRecipeRequest();
        recipe.setName("oatmeal")
                .setCookingTime(5)
                .setServings(1)
                .setDirections("Place all the ingredients into a medium microwave safe bowl and stir together. Heat " +
                        "in the microwave on high for 2 minutes. Then add 15-second increments until the oatmeal is " +
                        "puffed and softened. This is only necessary the first time you make it. Then you can gauge " +
                        "the exact time needed and repeat in the future. Stir before serving")
                .setDifficulty(Difficulty.EASY)
                .setIngredientList(List.of(oats, milk, water))
                .setCategories(List.of(breakfastCategory));
        return recipe;
    }

    public static NewRecipeRequest createWithDoubledCategories() {
        var recipe = createNewRequest();
        List<CategoryDto> categories = List.of(new CategoryDto("breakfast"),
                new CategoryDto("BREAKFAST"),
                new CategoryDto("BreakFast"));

        recipe.setCategories(categories);
        return recipe;
    }
}
