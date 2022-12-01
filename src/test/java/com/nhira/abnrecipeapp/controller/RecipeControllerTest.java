package com.nhira.abnrecipeapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhira.abnrecipeapp.dto.RecipeDto;
import com.nhira.abnrecipeapp.dto.RecipeFilterDto;
import com.nhira.abnrecipeapp.exceptions.RecipeNotFoundException;
import com.nhira.abnrecipeapp.service.api.RecipeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.UUID;

import static com.nhira.abnrecipeapp.utils.RecipeTestDataUtil.*;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

        mockMvc.perform(post("/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(recipeDto)))
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString(recipeDto.getName())));

    }

    @Test
    void givenInvalidRecipeDto_whenCreateRecipe_shouldReturnBadRequestResponse() throws Exception {

        RecipeDto invalidRecipeDto = new RecipeDto();

        mockMvc.perform(post("/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(invalidRecipeDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenGetAllRecipes_shouldReturnSuccessfulResponseWithListOfAvailableRecipes() throws Exception {
        RecipeDto testRecipe = getVeganRecipeDto();
        final Page<RecipeDto> recipes = new PageImpl<>(Collections.singletonList(testRecipe));

        when(recipeService.getAllRecipes(anyInt(), anyInt())).thenReturn(recipes);

        mockMvc.perform(get("/recipes/find-all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
//                .andExpect(jsonPath("$",hasSize(1))); todo - how do i verify size of list returned
    }

    @Test
    void givenRecipeFilter_whenGetRecipes_shouldReturnSuccessfulResponseWithListOfRecipesThatMatchFilter() throws Exception {
        RecipeDto testRecipe = getVeganRecipeDto();
        final Page<RecipeDto> recipes = new PageImpl<>(Collections.singletonList(testRecipe));

        when(recipeService.filterRecipes(any(RecipeFilterDto.class),anyInt(), anyInt())).thenReturn(recipes);

        RecipeFilterDto recipeFilter = getFilterDto();
        mockMvc.perform(get("/recipes/find")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(recipeFilter)))
                .andExpect(status().isOk());
//                .andExpect(jsonPath("$",hasSize(1)));
    }

    @Test
    void givenExistingRecipeID_whenGetRecipe_shouldReturnSuccessfulResponseRecipeWithIDSpecified() throws Exception {

        RecipeDto testRecipe = getVeganRecipeDto();
        when(recipeService.getRecipe(anyString())).thenReturn(getRecipeSuccessfulApiResponse(testRecipe));

        mockMvc.perform(get("/recipes/{id}", testRecipe.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(testRecipe.getName())));
    }

    @Test
    void givenNonExistentRecipeID_whenGetRecipe_shouldReturnNotFoundResponse() throws Exception {
        when(recipeService.getRecipe(anyString())).thenThrow(
                new RecipeNotFoundException("Recipe with id " + UUID.randomUUID() + " was not found "));

        mockMvc.perform(get("/recipes/{id}", UUID.randomUUID().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenExistingRecipeID_whenDeleteRecipe_shouldReturnSuccessfulResponseRecipeDeleted() throws Exception {
        RecipeDto testRecipe = getVeganRecipeDto();
        when(recipeService.deleteRecipe(anyString())).thenReturn(getRecipeSuccessfulApiResponse(testRecipe));

        mockMvc.perform(delete("/recipes/{id}", testRecipe.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(testRecipe.getName())));
    }

    @Test
    void givenNonExistentRecipeID_whenDeleteRecipe_shouldReturnNotFoundResponse() throws Exception {
        when(recipeService.deleteRecipe(anyString())).thenThrow(
                new RecipeNotFoundException("Recipe with id " + UUID.randomUUID() + " was not found "));

        mockMvc.perform(delete("/recipes/{id}", UUID.randomUUID().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


    @Test
    void givenRecipeDtoWithID_whenUpdateRecipe_shouldReturnSuccessAndUpdatedRecipe() throws Exception {
        RecipeDto recipeDto = getNonVeganRecipeDto();

        when(recipeService.updateRecipe(any(RecipeDto.class))).thenReturn(getRecipeSuccessfulApiResponse(recipeDto));

        mockMvc.perform(put("/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(recipeDto)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(recipeDto.getName())));
    }

    @Test
    void givenInvalidRecipeDto_whenUpdateRecipe_shouldReturnBadRequestResponse() throws Exception {

        RecipeDto invalidRecipeDto = new RecipeDto();

        mockMvc.perform(put("/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(invalidRecipeDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenRecipeDtoWithNonExistentID_whenUpdateRecipe_shouldReturnNotFoundResponse() throws Exception {
        RecipeDto recipeDto = getNonVeganRecipeDto();
        when(recipeService.updateRecipe(any(RecipeDto.class))).thenThrow(
                new RecipeNotFoundException("Recipe with id " + recipeDto.getId() + " was not found "));

        mockMvc.perform(put("/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(recipeDto)))
                .andExpect(status().isNotFound());
    }
}