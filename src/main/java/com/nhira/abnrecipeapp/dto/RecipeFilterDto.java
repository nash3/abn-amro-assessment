package com.nhira.abnrecipeapp.dto;

import com.nhira.abnrecipeapp.utils.enums.RecipeClassification;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class RecipeFilterDto {
    private String ingredientName;
    private String instructionSearch;
    private boolean includeIngredient;
    private long numberOfServings;
    private RecipeClassification classification;
}
