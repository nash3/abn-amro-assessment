package com.nhira.abnrecipeapp.integrationtests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhira.abnrecipeapp.dto.RecipeDto;
import com.nhira.abnrecipeapp.dto.RecipeFilterDto;
import com.nhira.abnrecipeapp.mapper.DtoMapper;
import com.nhira.abnrecipeapp.model.Recipe;
import com.nhira.abnrecipeapp.repository.RecipeRepository;
import com.nhira.abnrecipeapp.utils.enums.ResponseCode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.nhira.abnrecipeapp.utils.RecipeTestDataUtil.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@AutoConfigureMockMvc
public class RecipeControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private ObjectMapper mapper;

    @AfterEach
    void tearDown() {
        recipeRepository.deleteAll();
    }

    @Test
    void givenValidRecipeDto_whenCreateRecipe_shouldReturnSuccessfulCreationResponse() throws Exception {
        RecipeDto recipeDto = getNonVeganRecipeDto();

        ResultActions response = mockMvc.perform(post("/recipes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(recipeDto)));

        response.andExpect(status().isCreated())
                .andDo(print())
                .andExpect(jsonPath("$.responseCode", is(ResponseCode.SUCCESS.name())))
                .andExpect(jsonPath("$.body.name", is(recipeDto.getName())))
                .andExpect(jsonPath("$.body.instructions", is(recipeDto.getInstructions())))
                .andExpect(jsonPath("$.body.numberOfServings", is((int) recipeDto.getNumberOfServings())));

    }

    @Test
    void givenInvalidRecipeDto_whenCreateRecipe_shouldReturnBadRequestResponse() throws Exception {

        RecipeDto invalidRecipeDto = new RecipeDto();

        mockMvc.perform(post("/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(invalidRecipeDto)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void whenGetAllRecipes_shouldReturnSuccessfulResponseWithListOfAvailableRecipes() throws Exception {

        List<Recipe> recipes = new ArrayList<>();
        recipes.add(DtoMapper.MAPPER.toRecipeEntity(getVeganRecipeDto()));
        recipes.add(DtoMapper.MAPPER.toRecipeEntity(getNonVeganRecipeDto()));
        recipeRepository.saveAll(recipes);

        ResultActions response = mockMvc.perform(get("/recipes/find-all")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.content", hasSize(recipes.size())));
    }

    @Test
    void givenRecipeFilter_whenGetRecipes_shouldReturnSuccessfulResponseWithListOfRecipesThatMatchFilter() throws Exception {
        Recipe recipe = recipeRepository.save(DtoMapper.MAPPER.toRecipeEntity(getNonVeganRecipeDto()));

        RecipeFilterDto recipeFilter = getFilterDto();
        ResultActions response = mockMvc.perform(get("/recipes/find")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(recipeFilter)));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].name", is(recipe.getName())))
                .andExpect(jsonPath("$.content[0].instructions", is(recipe.getInstructions())))
                .andExpect(jsonPath("$.content[0].numberOfServings", is((int)recipe.getNumberOfServings())));
    }

    @Test
    void givenExistingRecipeID_whenGetRecipe_shouldReturnSuccessfulResponseRecipeWithIDSpecified() throws Exception {

        Recipe recipe = recipeRepository.save(DtoMapper.MAPPER.toRecipeEntity(getVeganRecipeDto()));
        ResultActions response = mockMvc.perform(get("/recipes/{id}", recipe.getId())
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.responseCode", is(ResponseCode.SUCCESS.name())))
                .andExpect(jsonPath("$.body.name", is(recipe.getName())))
                .andExpect(jsonPath("$.body.instructions", is(recipe.getInstructions())))
                .andExpect(jsonPath("$.body.numberOfServings", is((int) recipe.getNumberOfServings())));
    }

    @Test
    void givenNonExistentRecipeID_whenGetRecipe_shouldReturnNotFoundResponse() throws Exception {

        mockMvc.perform(get("/recipes/{id}", UUID.randomUUID().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void givenExistingRecipeID_whenDeleteRecipe_shouldReturnSuccessfulResponseRecipeDeleted() throws Exception {

        Recipe testRecipe = recipeRepository.save(DtoMapper.MAPPER.toRecipeEntity(getVeganRecipeDto()));
        ResultActions response = mockMvc.perform(delete("/recipes/{id}", testRecipe.getId())
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.responseCode", is(ResponseCode.SUCCESS.name())))
                .andExpect(jsonPath("$.body.name", is(testRecipe.getName())))
                .andExpect(jsonPath("$.body.instructions", is(testRecipe.getInstructions())))
                .andExpect(jsonPath("$.body.numberOfServings", is((int) testRecipe.getNumberOfServings())));
    }

    @Test
    void givenNonExistentRecipeID_whenDeleteRecipe_shouldReturnNotFoundResponse() throws Exception {
        mockMvc.perform(delete("/recipes/{id}", UUID.randomUUID().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }


    @Test
    void givenRecipeDtoWithID_whenUpdateRecipe_shouldReturnSuccessAndUpdatedRecipe() throws Exception {
        Recipe testRecipe = recipeRepository.save(DtoMapper.MAPPER.toRecipeEntity(getVeganRecipeDto()));

        RecipeDto updated = DtoMapper.MAPPER.toRecipeDto(testRecipe);
        updated.setName("Updated recipe");
        ResultActions response = mockMvc.perform(put("/recipes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updated)));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.responseCode", is(ResponseCode.SUCCESS.name())))
                .andExpect(jsonPath("$.body.name", is(updated.getName())))
                .andExpect(jsonPath("$.body.instructions", is(updated.getInstructions())))
                .andExpect(jsonPath("$.body.numberOfServings", is((int) updated.getNumberOfServings())));
    }

    @Test
    void givenInvalidRecipeDto_whenUpdateRecipe_shouldReturnBadRequestResponse() throws Exception {

        RecipeDto invalidRecipeDto = new RecipeDto();

        mockMvc.perform(put("/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(invalidRecipeDto)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void givenRecipeDtoWithNonExistentID_whenUpdateRecipe_shouldReturnNotFoundResponse() throws Exception {
        RecipeDto recipeDto = getNonVeganRecipeDto();
        mockMvc.perform(put("/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(recipeDto)))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

}
