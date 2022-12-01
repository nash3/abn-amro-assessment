package com.nhira.abnrecipeapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhira.abnrecipeapp.dto.RecipeDto;
import com.nhira.abnrecipeapp.dto.RecipeFilterDto;
import com.nhira.abnrecipeapp.exceptions.RecipeNotFoundException;
import com.nhira.abnrecipeapp.service.api.RecipeService;
import com.nhira.abnrecipeapp.utils.enums.ResponseCode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Collections;
import java.util.UUID;

import static com.nhira.abnrecipeapp.utils.RecipeTestDataUtil.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = RecipeController.class)
class RecipeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecipeService recipeService;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void givenValidRecipeDto_whenCreateRecipe_shouldReturnSuccessfulCreationResponse() throws Exception {
        RecipeDto recipeDto = getNonVeganRecipeDto();

        when(recipeService.createRecipe(any(RecipeDto.class))).thenReturn(getRecipeSuccessfulApiResponse(recipeDto));

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
        RecipeDto testRecipe = getVeganRecipeDto();
        final Page<RecipeDto> recipes = new PageImpl<>(Collections.singletonList(testRecipe));

        when(recipeService.getAllRecipes(anyInt(), anyInt())).thenReturn(recipes);

        ResultActions response = mockMvc.perform(get("/recipes/find-all")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.content", hasSize(1)));
    }

    @Test
    void givenRecipeFilter_whenGetRecipes_shouldReturnSuccessfulResponseWithListOfRecipesThatMatchFilter() throws Exception {
        RecipeDto testRecipe = getVeganRecipeDto();
        final Page<RecipeDto> recipes = new PageImpl<>(Collections.singletonList(testRecipe));

        when(recipeService.filterRecipes(any(RecipeFilterDto.class),anyInt(), anyInt())).thenReturn(recipes);

        RecipeFilterDto recipeFilter = getFilterDto();
        ResultActions response = mockMvc.perform(get("/recipes/find")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(recipeFilter)));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].name", is(testRecipe.getName())))
                .andExpect(jsonPath("$.content[0].instructions", is(testRecipe.getInstructions())))
                .andExpect(jsonPath("$.content[0].numberOfServings", is((int)testRecipe.getNumberOfServings())));
    }

    @Test
    void givenExistingRecipeID_whenGetRecipe_shouldReturnSuccessfulResponseRecipeWithIDSpecified() throws Exception {

        RecipeDto testRecipe = getVeganRecipeDto();
        when(recipeService.getRecipe(anyString())).thenReturn(getRecipeSuccessfulApiResponse(testRecipe));

        ResultActions response = mockMvc.perform(get("/recipes/{id}", testRecipe.getId())
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.responseCode", is(ResponseCode.SUCCESS.name())))
                .andExpect(jsonPath("$.body.name", is(testRecipe.getName())))
                .andExpect(jsonPath("$.body.instructions", is(testRecipe.getInstructions())))
                .andExpect(jsonPath("$.body.numberOfServings", is((int) testRecipe.getNumberOfServings())));
    }

    @Test
    void givenNonExistentRecipeID_whenGetRecipe_shouldReturnNotFoundResponse() throws Exception {
        when(recipeService.getRecipe(anyString())).thenThrow(
                new RecipeNotFoundException("Recipe with id " + UUID.randomUUID() + " was not found "));

        mockMvc.perform(get("/recipes/{id}", UUID.randomUUID().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void givenExistingRecipeID_whenDeleteRecipe_shouldReturnSuccessfulResponseRecipeDeleted() throws Exception {
        RecipeDto testRecipe = getVeganRecipeDto();
        when(recipeService.deleteRecipe(anyString())).thenReturn(getRecipeSuccessfulApiResponse(testRecipe));

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
        when(recipeService.deleteRecipe(anyString())).thenThrow(
                new RecipeNotFoundException("Recipe with id " + UUID.randomUUID() + " was not found "));

        mockMvc.perform(delete("/recipes/{id}", UUID.randomUUID().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }


    @Test
    void givenRecipeDtoWithID_whenUpdateRecipe_shouldReturnSuccessAndUpdatedRecipe() throws Exception {
        RecipeDto recipeDto = getNonVeganRecipeDto();

        when(recipeService.updateRecipe(any(RecipeDto.class))).thenReturn(getRecipeSuccessfulApiResponse(recipeDto));

        ResultActions response = mockMvc.perform(put("/recipes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(recipeDto)));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.responseCode", is(ResponseCode.SUCCESS.name())))
                .andExpect(jsonPath("$.body.name", is(recipeDto.getName())))
                .andExpect(jsonPath("$.body.instructions", is(recipeDto.getInstructions())))
                .andExpect(jsonPath("$.body.numberOfServings", is((int) recipeDto.getNumberOfServings())));;
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
        when(recipeService.updateRecipe(any(RecipeDto.class))).thenThrow(
                new RecipeNotFoundException("Recipe with id " + recipeDto.getId() + " was not found "));

        mockMvc.perform(put("/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(recipeDto)))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
}