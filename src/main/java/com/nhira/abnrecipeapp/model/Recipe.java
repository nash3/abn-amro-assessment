package com.nhira.abnrecipeapp.model;

import com.nhira.myrecipeapp.utils.RecipeClassification;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Inheritance
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Recipe extends BaseEntity{
    private String name;
    private long numberOfServings;

    @Enumerated(EnumType.STRING)
    private RecipeClassification classification;

    @ManyToOne(targetEntity = User.class)
    private User user;

    private String ingredients;
    private String instructions;
}
