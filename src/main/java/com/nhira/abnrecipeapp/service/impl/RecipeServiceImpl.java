package com.nhira.abnrecipeapp.service.impl;

import com.nhira.abnrecipeapp.dto.RecipeDto;
import com.nhira.abnrecipeapp.dto.RecipeFilterDto;
import com.nhira.abnrecipeapp.repository.RecipeRepository;
import com.nhira.abnrecipeapp.service.api.RecipeService;
import com.nhira.abnrecipeapp.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;


    @Override
    public Page<RecipeDto> getAllRecipes(String search, int page, int size) {
        return null;
    }

    @Override
    public Page<RecipeDto> filterRecipes(RecipeFilterDto filter, int page, int size) {
        return null;
    }

    @Override
    public ApiResponse<RecipeDto> createRecipe(RecipeDto recipeDto) {
        return null;
    }

    @Override
    public ApiResponse<RecipeDto> updateRecipe(RecipeDto recipeDto) {
        return null;
    }

    @Override
    public ApiResponse<RecipeDto> getRecipe(String id) {
        return null;
    }

    @Override
    public ApiResponse<RecipeDto> deleteRecipe(String id) {
        return null;
    }
}
