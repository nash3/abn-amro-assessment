package com.nhira.abnrecipeapp.integrationtests;

import com.nhira.abnrecipeapp.dto.RecipeDto;
import com.nhira.abnrecipeapp.dto.RecipeFilterDto;
import com.nhira.abnrecipeapp.exceptions.RecipeNotFoundException;
import com.nhira.abnrecipeapp.mapper.DtoMapper;
import com.nhira.abnrecipeapp.model.Recipe;
import com.nhira.abnrecipeapp.repository.RecipeRepository;
import com.nhira.abnrecipeapp.service.api.RecipeService;
import com.nhira.abnrecipeapp.utils.ApiResponse;
import com.nhira.abnrecipeapp.utils.enums.ResponseCode;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.nhira.abnrecipeapp.utils.RecipeTestDataUtil.*;
import static com.nhira.abnrecipeapp.utils.RecipeTestDataUtil.getFilterDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@SpringBootTest
class RecipeServiceIT {

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private RecipeRepository recipeRepository;

    @AfterEach
    void tearDown() {
        recipeRepository.deleteAll();
    }

    @Test
    void givenRecipeDto_whenCreateRecipe_shouldSaveRecipeAndReturnSavedRecipe() {
        final RecipeDto testRecipe = getVeganRecipeDto();
        ApiResponse<RecipeDto> actualResponse = recipeService.createRecipe(testRecipe);

        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse.isSuccessful()).isTrue();
        assertThat(actualResponse.getResponseCode()).isEqualTo(ResponseCode.SUCCESS);
        assertThat(actualResponse.getNarrative()).isEqualTo(ResponseCode.SUCCESS.getDescription());
        assertThat(actualResponse.getBody()).isNotNull();
        assertThat(actualResponse.getBody()).isEqualTo(testRecipe);
    }

    @Test
    void givenRecipeDto_whenUpdateRecipe_shouldUpdateRecipeAndReturnUpdatedRecipe() {

        RecipeDto expected = recipeService.createRecipe(getVeganRecipeDto()).getBody();
        expected.setName("Updated recipe name");
        ApiResponse<RecipeDto> actualResponse = recipeService.updateRecipe(expected);

        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse.isSuccessful()).isTrue();
        assertThat(actualResponse.getResponseCode()).isEqualTo(ResponseCode.SUCCESS);
        assertThat(actualResponse.getNarrative()).isEqualTo(ResponseCode.SUCCESS.getDescription());
        assertThat(actualResponse.getBody()).isNotNull();
        assertThat(actualResponse.getBody()).isEqualTo(expected);
        assertThat(actualResponse.getBody().getName()).isEqualTo(expected.getName());

    }

    @Test
    void givenRecipeDtoWithIdThatDoesNotExist_whenUpdateRecipe_shouldThrowExceptionAndNotSaveRecipe() {
        assertThrows(RecipeNotFoundException.class, () -> {
            recipeService.updateRecipe(getVeganRecipeDto());
        });
    }

    @Test
    void whenGetAllRecipes_shouldFindAndReturnAllRecipes() {
        ApiResponse<RecipeDto> expected = recipeService.createRecipe(getVeganRecipeDto());

        List<RecipeDto> allRecipes = recipeService.getAllRecipes(0, 10).get().collect(Collectors.toList());
        assertThat(allRecipes).hasSize(1);
        RecipeDto actualRecipeDto = allRecipes.get(0);
        assertThat(actualRecipeDto).isNotNull();
        assertThat(actualRecipeDto).isEqualTo(expected.getBody());
    }

    @Test
    void givenRecipeFilterDto_whenFilterRecipes_shouldReturnListOfRecipesThatMatchFilterCriterion() {

        ApiResponse<RecipeDto> expected = recipeService.createRecipe(getNonVeganRecipeDto());
        RecipeFilterDto filterDto = getFilterDto();

        List<RecipeDto> recipeList = recipeService.filterRecipes(filterDto, 0, 10).get().collect(Collectors.toList());

        assertThat(recipeList).hasSize(1);
        RecipeDto actualRecipeDto = recipeList.get(0);
        assertThat(actualRecipeDto).isNotNull();
        assertThat(actualRecipeDto.getClassification()).isEqualTo(filterDto.getClassification());
        assertThat(actualRecipeDto.getInstructions()).contains(filterDto.getInstructionSearch());
        assertThat(actualRecipeDto).isEqualTo(expected.getBody());

    }

    @Test
    void givenRecipeID_whenGetRecipe_shouldReturnRecipe() {

        ApiResponse<RecipeDto> expected = recipeService.createRecipe(getNonVeganRecipeDto());

        ApiResponse<RecipeDto> actualResponse = recipeService.getRecipe(expected.getBody().getId());

        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse.isSuccessful()).isTrue();
        assertThat(actualResponse.getResponseCode()).isEqualTo(ResponseCode.SUCCESS);
        assertThat(actualResponse.getNarrative()).isEqualTo(ResponseCode.SUCCESS.getDescription());
        assertThat(actualResponse.getBody()).isNotNull();
        assertThat(actualResponse.getBody()).isEqualTo(expected.getBody());


    }

    @Test
    void givenRecipeIDThatDoesNotExist_whenGetRecipe_shouldThrowException() {
        assertThrows(RecipeNotFoundException.class, () -> {
            recipeService.getRecipe(UUID.randomUUID().toString());
        });
    }

    @Test
    void givenRecipeID_whenDeleteRecipe_shouldDeleteOneRecipe() {

        ApiResponse<RecipeDto> expected = recipeService.createRecipe(getVeganRecipeDto());
        ApiResponse<RecipeDto> actualResponse = recipeService.deleteRecipe(expected.getBody().getId());

        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse.isSuccessful()).isTrue();
        assertThat(actualResponse.getResponseCode()).isEqualTo(ResponseCode.SUCCESS);
        assertThat(actualResponse.getNarrative()).isEqualTo(ResponseCode.SUCCESS.getDescription());
        assertThat(actualResponse.getBody()).isNotNull();
        assertThat(actualResponse.getBody()).isEqualTo(expected.getBody());

    }

    @Test
    void givenRecipeIDThatDoesNotExist_whenDeleteRecipe_shouldThrowException() {
        assertThrows(RecipeNotFoundException.class, () -> {
            recipeService.deleteRecipe(UUID.randomUUID().toString());
        });
    }

}
