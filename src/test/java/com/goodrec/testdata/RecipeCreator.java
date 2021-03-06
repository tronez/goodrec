package com.goodrec.testdata;

import com.goodrec.recipe.domain.CategoryEnum;
import com.goodrec.recipe.domain.Difficulty;
import com.goodrec.recipe.domain.Unit;
import com.goodrec.recipe.dto.CategoryDto;
import com.goodrec.recipe.dto.IngredientDto;
import com.goodrec.recipe.dto.NewIngredientRequest;
import com.goodrec.recipe.dto.NewRecipeRequest;
import com.goodrec.recipe.dto.UpdateRecipeRequest;

import java.util.List;
import java.util.UUID;

public class RecipeCreator {

    public static NewRecipeRequest createNewRequest() {
        var oats = new NewIngredientRequest("oats", 0.5, Unit.CUP);
        var milk = new NewIngredientRequest("milk", 0.5, Unit.CUP);
        var water = new NewIngredientRequest("water", 0.5, Unit.CUP);
        var breakfastCategory = new CategoryDto(CategoryEnum.ENGLISH);
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
        List<CategoryDto> categories = List.of(
                new CategoryDto(CategoryEnum.ENGLISH),
                new CategoryDto(CategoryEnum.ENGLISH));

        recipe.setCategories(categories);
        return recipe;
    }

    public static NewRecipeRequest createBadRequest() {
        return new NewRecipeRequest();
    }

    public static UpdateRecipeRequest createUpdated() {
        var updatedRecipe = new UpdateRecipeRequest();
        var updatedIngredient = new IngredientDto(UUID.randomUUID(), "Updated ingredient", 10.0, Unit.CUP);

        var category = new CategoryDto(CategoryEnum.ENGLISH);

        updatedRecipe
                .setName("updated name")
                .setCookingTime(666)
                .setServings(999)
                .setDirections("Updated directions")
                .setDifficulty(Difficulty.EASY)
                .setIngredientList(List.of(updatedIngredient))
                .setCategories(List.of(category));

        return updatedRecipe;
    }

    public static UpdateRecipeRequest createInvalid() {
        return new UpdateRecipeRequest()
                .setName("")
                .setDirections("");
    }
}
