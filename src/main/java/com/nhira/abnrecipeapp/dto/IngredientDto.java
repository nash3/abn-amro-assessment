package com.nhira.abnrecipeapp.dto;

import com.nhira.abnrecipeapp.utils.enums.UnitOfMeasure;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class IngredientDto {
    private String name;
    private double quantity;
    private UnitOfMeasure unitOfMeasure;
}
