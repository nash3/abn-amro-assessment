package com.nhira.abnrecipeapp.dto;

import com.nhira.abnrecipeapp.utils.enums.RecipeClassification;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class RecipeDto {
    private String id;
    private String name;
    private long numberOfServings;
    private RecipeClassification classification;
    private List<IngredientDto> ingredientList;
    private String instructions;
}
