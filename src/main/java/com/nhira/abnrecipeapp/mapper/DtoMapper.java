package com.nhira.abnrecipeapp.mapper;


import com.nhira.abnrecipeapp.dto.RecipeDto;
import com.nhira.abnrecipeapp.model.Recipe;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface DtoMapper {

    // TODO - FIX MAPPER TO REFLECT JSON OBJECT INGREDIENTS

    DtoMapper MAPPER = Mappers.getMapper(DtoMapper.class);

    RecipeDto toRecipeDto(Recipe entity);
    List<RecipeDto> toRecipeDtoList(List<Recipe> entities);
    Recipe toRecipeEntity(RecipeDto dto);
    List<Recipe> toRecipeEntityList(List<RecipeDto> dtos);

}
