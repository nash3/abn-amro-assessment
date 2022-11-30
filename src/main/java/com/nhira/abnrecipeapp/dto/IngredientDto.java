package com.nhira.abnrecipeapp.dto;

import com.nhira.abnrecipeapp.utils.enums.UnitOfMeasure;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class IngredientDto {

    @NotNull
    private String name;

    @NotNull
    private double quantity;

    @NotNull
    private UnitOfMeasure unitOfMeasure;
}
