package com.nhira.abnrecipeapp.utils;

import com.nhira.abnrecipeapp.dto.IngredientDto;
import com.nhira.abnrecipeapp.dto.RecipeDto;
import com.nhira.abnrecipeapp.dto.RecipeFilterDto;
import com.nhira.abnrecipeapp.mapper.DtoMapper;
import com.nhira.abnrecipeapp.model.Recipe;
import com.nhira.abnrecipeapp.utils.enums.RecipeClassification;
import com.nhira.abnrecipeapp.utils.enums.ResponseCode;
import com.nhira.abnrecipeapp.utils.enums.UnitOfMeasure;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RecipeTestDataUtil {

    public static RecipeDto getNonVeganRecipeDto() {
        return RecipeDto.builder()
                .id(UUID.randomUUID().toString())
                .name("Boil egg")
                .classification(RecipeClassification.NON_VEGETARIAN)
                .instructions("Add water and egg to pot and bring to boil")
                .ingredientList(getNonVeganIngredients())
                .numberOfServings(1)
                .build();
    }
    public static RecipeDto getVeganRecipeDto() {
        return RecipeDto.builder()
                .id(UUID.randomUUID().toString())
                .name("Green smoothie")
                .classification(RecipeClassification.VEGETARIAN)
                .instructions("Add lettuce and water to blender and crush")
                .ingredientList(getVeganIngredients())
                .numberOfServings(1)
                .build();
    }

    public static RecipeFilterDto getFilterDto() {
        return RecipeFilterDto.builder()
                .ingredientName("egg")
                .includeIngredient(true)
                .instructionSearch("boil")
                .numberOfServings(1)
                .classification(RecipeClassification.NON_VEGETARIAN)
                .build();
    }

    public static Recipe getRecipe(RecipeDto recipeDto) {
        return DtoMapper.MAPPER.toRecipeEntity(recipeDto);
    }

    public static ApiResponse<RecipeDto> getSuccessfulResponse(Recipe recipe) {
       return  Utils.createResponse(DtoMapper.MAPPER.toRecipeDto(recipe), true, ResponseCode.SUCCESS);
    }

    public static ApiResponse<RecipeDto> getErrorResponse () {
       return  Utils.createResponse(null, false, ResponseCode.ERROR);
    }

    public static ApiResponse<RecipeDto> getNotFoundResponse () {
       return  Utils.createResponse(null, false, ResponseCode.NOT_FOUND);
    }

    public static List<IngredientDto> getNonVeganIngredients() {
        List<IngredientDto> ingredients = new ArrayList<>();
        IngredientDto ingredientDto = IngredientDto.builder()
                .name("egg")
                .quantity(1)
                .unitOfMeasure(UnitOfMeasure.UNIT)
                .build();
        ingredients.add(ingredientDto);
        return ingredients;
    }
    public static List<IngredientDto> getVeganIngredients() {
        List<IngredientDto> ingredients = new ArrayList<>();
        IngredientDto lettuce = IngredientDto.builder()
                .name("lettuce")
                .quantity(150)
                .unitOfMeasure(UnitOfMeasure.GRAM)
                .build();
        IngredientDto water = IngredientDto.builder()
                .name("water")
                .quantity(500)
                .unitOfMeasure(UnitOfMeasure.MILLILITRE)
                .build();
        ingredients.add(lettuce);
        ingredients.add(water);
        return ingredients;
    }
}
