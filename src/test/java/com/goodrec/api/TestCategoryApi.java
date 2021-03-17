package com.goodrec.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goodrec.recipe.domain.CategoryEnum;
import com.goodrec.recipe.dto.CategoryDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TestCategoryApi {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    @DisplayName("Should return 200 when fetching category page")
    void testGetAllCategoriesSuccess() throws Exception {
        int availableEnumLength = CategoryEnum.values().length;

        MvcResult result = mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        var responseArray = mapper.readValue(response, CategoryDto[].class);
        assertEquals(availableEnumLength, responseArray.length, "Number of categories are not equal");
    }

    @Test
    @DisplayName("Should return 200 when fetching all recipes")
    void testGetAllRecipesByCategorySuccess() throws Exception {
        String categoryName = "english";

        mockMvc.perform(get("/api/categories/{categoryName}/recipes", categoryName))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return 400 when fetching recipes by category with invalid category")
    void testGetAllRecipesByCategoryFailBadRequest() throws Exception {
        String categoryName = "invalidNAme";

        var exceptionMessage = "No enum constant";
        mockMvc.perform(get("/api/categories/{categoryName}/recipes", categoryName))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString(exceptionMessage)));
    }
}