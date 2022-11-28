package com.nhira.abnrecipeapp.mapper;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhira.abnrecipeapp.dto.IngredientDto;
import com.nhira.abnrecipeapp.dto.RecipeDto;
import com.nhira.abnrecipeapp.model.Recipe;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.Arrays;
import java.util.List;

@Mapper
public interface DtoMapper {

    // TODO - FIX MAPPER TO REFLECT JSON OBJECT INGREDIENTS - work on the remaining list to list

    DtoMapper MAPPER = Mappers.getMapper(DtoMapper.class);

    ObjectMapper objectMapper = new ObjectMapper();

    @Mapping(source = "ingredients",
            target = "ingredientList",
            qualifiedByName = "stringToIngredientList")
    RecipeDto toRecipeDto(Recipe entity);
    List<RecipeDto> toRecipeDtoList(List<Recipe> entities);

    @Mapping(source = "ingredientList",
            target = "ingredients",
            qualifiedByName = "IngredientListToString")
    Recipe toRecipeEntity(RecipeDto dto);
    List<Recipe> toRecipeEntityList(List<RecipeDto> dtos);

    @Named("IngredientListToString")
    static String IngredientDtoListToString(List<IngredientDto> ingredients) throws JsonProcessingException {
        return objectMapper.writeValueAsString(ingredients);
    }

    @Named("stringToIngredientList")
    static List<IngredientDto> stringToIngredientList(String ingredients) throws JsonProcessingException {
        return Arrays.asList(objectMapper.readValue(ingredients, IngredientDto[].class));
    }

}
