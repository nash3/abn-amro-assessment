package com.nhira.abnrecipeapp.repository;

import com.nhira.abnrecipeapp.model.Recipe;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static com.nhira.abnrecipeapp.utils.RecipeTestDataUtil.*;

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
    void filterByIng() {
    }
}