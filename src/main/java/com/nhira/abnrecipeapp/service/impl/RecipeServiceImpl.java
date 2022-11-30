package com.nhira.abnrecipeapp.service.impl;

import com.nhira.abnrecipeapp.dto.RecipeDto;
import com.nhira.abnrecipeapp.dto.RecipeFilterDto;
import com.nhira.abnrecipeapp.mapper.DtoMapper;
import com.nhira.abnrecipeapp.model.Recipe;
import com.nhira.abnrecipeapp.repository.RecipeRepository;
import com.nhira.abnrecipeapp.service.api.RecipeService;
import com.nhira.abnrecipeapp.utils.ApiResponse;
import com.nhira.abnrecipeapp.utils.Utils;
import com.nhira.abnrecipeapp.utils.enums.ResponseCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecipeServiceImpl implements RecipeService {
    private final RecipeRepository recipeRepository;

    @Override
    public Page<RecipeDto> getAllRecipes(int page, int size) {
        log.debug("Get all recipes with");
        Pageable pageable = PageRequest.of(page, size, Sort.by("dateCreated").descending());
        return recipeRepository.findAll(pageable).map(DtoMapper.MAPPER::toRecipeDto);
    }

    @Override
    public Page<RecipeDto> filterRecipes(RecipeFilterDto filter, int page, int size) {
        log.debug("Filter recipes with criteria: {}", filter);
        Pageable pageable = PageRequest.of(page, size, Sort.by("dateCreated").descending());
        return recipeRepository.filter(
                filter.getClassification() != null ? filter.getClassification().name() : null,
                filter.getNumberOfServings(),
                filter.getInstructionSearch(),
                filter.getIngredientName(),
                filter.isIncludeIngredient(),
                pageable
        ).map(DtoMapper.MAPPER::toRecipeDto);
    }

    @Override
    public ApiResponse<RecipeDto> createRecipe(RecipeDto recipeDto) {

        Recipe savedRecipe = recipeRepository.save(DtoMapper.MAPPER.toRecipeEntity(recipeDto));
        return Utils.createResponse(DtoMapper.MAPPER.toRecipeDto(savedRecipe), true, ResponseCode.SUCCESS);
    }

    @Override
    public ApiResponse<RecipeDto> updateRecipe(RecipeDto recipeDto) {
        return recipeRepository.findById(recipeDto.getId()).map(recipe -> {
            final Recipe savedRecipe = recipeRepository.save(DtoMapper.MAPPER.toRecipeEntity(recipeDto));
            return Utils.createResponse(DtoMapper.MAPPER.toRecipeDto(savedRecipe),
                    true, ResponseCode.SUCCESS);
        }).orElse(Utils.createResponse(null, true, ResponseCode.SUCCESS));
    }

    @Override
    public ApiResponse<RecipeDto> getRecipe(String id) {
        return recipeRepository.findById(id)
                .map(DtoMapper.MAPPER::toRecipeDto)
                .map(recipeDto -> Utils.createResponse(recipeDto, true, ResponseCode.SUCCESS))
                .orElse(Utils.createResponse(null, false, ResponseCode.NOT_FOUND));
    }

    @Override
    public ApiResponse<RecipeDto> deleteRecipe(String id) {
        return recipeRepository.findById(id).map(recipe -> {
            recipeRepository.delete(recipe);
            return Utils.createResponse(DtoMapper.MAPPER.toRecipeDto(recipe), true, ResponseCode.SUCCESS);
        }).orElse(Utils.createResponse(null, false, ResponseCode.ERROR));
    }
}
