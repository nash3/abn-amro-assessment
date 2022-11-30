package com.nhira.abnrecipeapp.repository;

import com.nhira.abnrecipeapp.model.Recipe;
import com.nhira.abnrecipeapp.utils.enums.RecipeClassification;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;

import static com.nhira.abnrecipeapp.utils.RecipeTestDataUtil.*;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class RecipeRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RecipeRepository recipeRepository;

    @BeforeEach
    void setUp() {
        Recipe recipe1 = getRecipe(getNonVeganRecipeDto());
        Recipe recipe2 = getRecipe(getVeganRecipeDto());
        entityManager.persist(recipe1);
        entityManager.persist(recipe2);
        entityManager.flush();
    }



    @Test
    void shouldFilterRecipesByClassification() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("dateCreated").descending());
        Page<Recipe> recipes = recipeRepository.filter(RecipeClassification.VEGETARIAN.name(),
                0,
                null,
                null,
                false, pageable);


        assertThat(recipes).hasSize(1);
        List<Recipe> recipeList = recipes.get().collect(Collectors.toList());
        assertThat(recipeList.get(0).getClassification()).isEqualTo(RecipeClassification.VEGETARIAN);
    }
    @Test
    void shouldFilterRecipesByNumberOfServings() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("dateCreated").descending());
        Page<Recipe> recipes = recipeRepository.filter(null,
                2,
                null,
                null,
                false, pageable);


        assertThat(recipes).hasSize(1);
        List<Recipe> recipeList = recipes.get().collect(Collectors.toList());
        assertThat(recipeList.get(0).getNumberOfServings()).isEqualTo(2);
    }
    @Test
    void shouldReturnRecipesThatIncludeACertainIngredient() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("dateCreated").descending());
        Page<Recipe> recipes = recipeRepository.filter(null,
                0,
                null,
                "egg",
                true, pageable);


        assertThat(recipes).hasSize(1);
        List<Recipe> recipeList = recipes.get().collect(Collectors.toList());
        assertThat(recipeList.get(0).getName()).isEqualTo("Boil egg");
    }
    @Test
    void shouldReturnRecipesThatExcludeACertainIngredient() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("dateCreated").descending());
        Page<Recipe> recipes = recipeRepository.filter(null,
                0,
                null,
                "egg",
                false, pageable);


        assertThat(recipes).hasSize(1);
        List<Recipe> recipeList = recipes.get().collect(Collectors.toList());
        assertThat(recipeList.get(0).getName()).isNotEqualTo("Boil egg");
    }
    @Test
    void shouldReturnRecipesThatHaveSearchTermInInstructions() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("dateCreated").descending());
        Page<Recipe> recipes = recipeRepository.filter(null,
                0,
                "blender",
                null,
                false, pageable);


        assertThat(recipes).hasSize(1);
        List<Recipe> recipeList = recipes.get().collect(Collectors.toList());
        assertThat(recipeList.get(0).getName()).isEqualTo("Green smoothie");
    }
    @Test
    void shouldIgnoreNullFiltersAndReturnAllRecipes() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("dateCreated").descending());
        Page<Recipe> recipes = recipeRepository.filter(null,
                0,
                null,
                null,
                false, pageable);


        assertThat(recipes).hasSize(2);
    }
}