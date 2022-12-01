package com.nhira.abnrecipeapp.service.impl;

import com.nhira.abnrecipeapp.dto.RecipeDto;
import com.nhira.abnrecipeapp.dto.RecipeFilterDto;
import com.nhira.abnrecipeapp.exceptions.RecipeNotFoundException;
import com.nhira.abnrecipeapp.mapper.DtoMapper;
import com.nhira.abnrecipeapp.model.Recipe;
import com.nhira.abnrecipeapp.repository.RecipeRepository;
import com.nhira.abnrecipeapp.utils.ApiResponse;
import com.nhira.abnrecipeapp.utils.enums.ResponseCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.nhira.abnrecipeapp.utils.RecipeTestDataUtil.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecipeServiceImplTest {

    @Mock
    private RecipeRepository recipeRepository;

    @InjectMocks
    private RecipeServiceImpl recipeService;

    @Test
    void givenRecipeDto_whenCreateRecipe_shouldSaveRecipeAndReturnSavedRecipe() {
        final Recipe testRecipe = getRecipe(getVeganRecipeDto());
        ApiResponse<RecipeDto> expectedRecipeApiResponse = getSuccessfulResponse(testRecipe);
        when(recipeRepository.save(any(Recipe.class))).thenReturn(testRecipe);

        ApiResponse<RecipeDto> actualRecipeResponse = recipeService.createRecipe(getVeganRecipeDto());

        assertThat(actualRecipeResponse).isEqualTo(expectedRecipeApiResponse);
        verify(recipeRepository, times(1)).save(any(Recipe.class));
        verifyNoMoreInteractions(recipeRepository);
    }

    @Test
    void givenRecipeDto_whenUpdateRecipe_shouldUpdateRecipeAndReturnUpdatedRecipe() {
        final Recipe testRecipe = getRecipe(getVeganRecipeDto());
        when(recipeRepository.findById(anyString())).thenReturn(Optional.of(testRecipe));
        when(recipeRepository.save(any(Recipe.class))).thenReturn(testRecipe);

        ApiResponse<RecipeDto> actualResponse = recipeService.updateRecipe(DtoMapper.MAPPER.toRecipeDto(testRecipe));

        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse.isSuccessful()).isTrue();
        assertThat(actualResponse.getResponseCode()).isEqualTo(ResponseCode.SUCCESS);
        assertThat(actualResponse.getNarrative()).isEqualTo(ResponseCode.SUCCESS.getDescription());
        assertThat(actualResponse.getBody()).isNotNull();
        assertThat(actualResponse.getBody()).isEqualTo(DtoMapper.MAPPER.toRecipeDto(testRecipe));

        verify(recipeRepository, times(1)).findById(testRecipe.getId());
        verify(recipeRepository, times(1)).save(any(Recipe.class));
        verifyNoMoreInteractions(recipeRepository);
    }

    @Test
    void givenRecipeDtoWithIdThatDoesNotExist_whenUpdateRecipe_shouldThrowExceptionAndNotSaveRecipe() {
        final Recipe testRecipe = getRecipe(getVeganRecipeDto());
        when(recipeRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(RecipeNotFoundException.class, () -> {
            recipeService.updateRecipe(DtoMapper.MAPPER.toRecipeDto(testRecipe));
        });

        verify(recipeRepository, times(1)).findById(testRecipe.getId());
        verify(recipeRepository, times(0)).save(any(Recipe.class));
        verifyNoMoreInteractions(recipeRepository);
    }

    @Test
    void whenGetAllRecipes_shouldFindAndReturnAllRecipes() {
        Recipe testRecipe = getRecipe(getVeganRecipeDto());
        final Page<Recipe> recipes = new PageImpl<>(Arrays.asList(testRecipe));
        when(recipeRepository.findAll(any(Pageable.class))).thenReturn(recipes);
        List<RecipeDto> allRecipes = recipeService.getAllRecipes(0, 10).get().collect(Collectors.toList());
        assertThat(allRecipes).hasSize(1);
        RecipeDto actualRecipeDto = allRecipes.get(0);
        assertThat(actualRecipeDto).isNotNull();
        assertThat(actualRecipeDto).isEqualTo(DtoMapper.MAPPER.toRecipeDto(testRecipe));
        verify(recipeRepository, times(1)).findAll(any(Pageable.class));
        verifyNoMoreInteractions(recipeRepository);
    }

    @Test
    void givenRecipeFilterDto_whenFilterRecipes_shouldReturnListOfRecipesThatMatchFilterCriterion() {
        Recipe testRecipe = getRecipe(getVeganRecipeDto());
        final Page<Recipe> recipes = new PageImpl<>(Arrays.asList(testRecipe));

        when(recipeRepository.filter(anyString(),
                anyLong(), anyString(),
                anyString(),anyBoolean(),
                any(Pageable.class))).thenReturn(recipes);
        RecipeFilterDto filterDto = getFilterDto();

        List<RecipeDto> recipeList = recipeService.filterRecipes(filterDto, 0, 10).get().collect(Collectors.toList());
        assertThat(recipeList).hasSize(1);
        RecipeDto actualRecipeDto = recipeList.get(0);
        assertThat(actualRecipeDto).isNotNull();
        assertThat(actualRecipeDto).isEqualTo(DtoMapper.MAPPER.toRecipeDto(testRecipe));
        verify(recipeRepository, times(1)).filter(anyString(),
                anyLong(), anyString(),
                anyString(),anyBoolean(),
                any(Pageable.class));
        verifyNoMoreInteractions(recipeRepository);
    }

    @Test
    void givenRecipeID_whenGetRecipe_shouldReturnRecipe() {
        Recipe testRecipe = getRecipe(getVeganRecipeDto());
        when(recipeRepository.findById(anyString())).thenReturn(Optional.of(testRecipe));

        ApiResponse<RecipeDto> actualResponse = recipeService.getRecipe(testRecipe.getId());

        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse.isSuccessful()).isTrue();
        assertThat(actualResponse.getResponseCode()).isEqualTo(ResponseCode.SUCCESS);
        assertThat(actualResponse.getNarrative()).isEqualTo(ResponseCode.SUCCESS.getDescription());
        assertThat(actualResponse.getBody()).isNotNull();
        assertThat(actualResponse.getBody()).isEqualTo(DtoMapper.MAPPER.toRecipeDto(testRecipe));

        verify(recipeRepository, times(1)).findById(testRecipe.getId());
        verifyNoMoreInteractions(recipeRepository);
    }

    @Test
    void givenRecipeIDThatDoesNotExist_whenGetRecipe_shouldThrowException() {
        Recipe testRecipe = getRecipe(getVeganRecipeDto());

        when(recipeRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(RecipeNotFoundException.class, () -> {
            recipeService.getRecipe(testRecipe.getId());
        });

        verify(recipeRepository, times(1)).findById(testRecipe.getId());
        verifyNoMoreInteractions(recipeRepository);
    }

    @Test
    void givenRecipeID_whenDeleteRecipe_shouldDeleteOneRecipe() {
        Recipe testRecipe = getRecipe(getVeganRecipeDto());
        when(recipeRepository.findById(anyString())).thenReturn(Optional.of(testRecipe));
        doNothing().when(recipeRepository).delete(any(Recipe.class));

        ApiResponse<RecipeDto> actualResponse = recipeService.deleteRecipe(testRecipe.getId());

        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse.isSuccessful()).isTrue();
        assertThat(actualResponse.getResponseCode()).isEqualTo(ResponseCode.SUCCESS);
        assertThat(actualResponse.getNarrative()).isEqualTo(ResponseCode.SUCCESS.getDescription());
        assertThat(actualResponse.getBody()).isNotNull();
        assertThat(actualResponse.getBody()).isEqualTo(DtoMapper.MAPPER.toRecipeDto(testRecipe));

        verify(recipeRepository, times(1)).findById(anyString());
        verify(recipeRepository, times(1)).delete(any(Recipe.class));
        verifyNoMoreInteractions(recipeRepository);
    }

    @Test
    void givenRecipeIDThatDoesNotExist_whenDeleteRecipe_shouldThrowException() {
        Recipe testRecipe = getRecipe(getVeganRecipeDto());

        when(recipeRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(RecipeNotFoundException.class, () -> {
            recipeService.deleteRecipe(testRecipe.getId());
        });

        verify(recipeRepository, times(1)).findById(anyString());
        verify(recipeRepository, times(0)).delete(any(Recipe.class));
        verifyNoMoreInteractions(recipeRepository);
    }
}