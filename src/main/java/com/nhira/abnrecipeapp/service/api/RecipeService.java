package com.nhira.abnrecipeapp.service.api;

import com.nhira.abnrecipeapp.dto.RecipeDto;
import com.nhira.abnrecipeapp.dto.RecipeFilterDto;
import com.nhira.abnrecipeapp.utils.ApiResponse;
import org.springframework.data.domain.Page;

public interface RecipeService {
    Page<RecipeDto> getAllRecipes(int page, int size);
    Page<RecipeDto> filterRecipes(RecipeFilterDto filter, int page, int size);
    ApiResponse<RecipeDto> createRecipe(RecipeDto recipeDto);
    ApiResponse<RecipeDto> updateRecipe(RecipeDto recipeDto);
    ApiResponse<RecipeDto> getRecipe(String id);
    ApiResponse<RecipeDto> deleteRecipe(String id);
}
