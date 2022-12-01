package com.nhira.abnrecipeapp.dto;

import com.nhira.abnrecipeapp.utils.enums.RecipeClassification;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {
        "id",
        "name",
        "numberOfServings",
        "classification",
        "instructions",
})
public class RecipeDto {

    private String id;

    @NotNull(message = "name is mandatory")
    private String name;

    @NotNull(message = "number of servings should be specified")
    private long numberOfServings;

    @NotNull(message = "classification is mandatory")
    private RecipeClassification classification;

    @NotEmpty(message = "ingredients should be specified")
    private List<IngredientDto> ingredientList;

    @NotNull(message = "instructions are mandatory")
    private String instructions;
}
