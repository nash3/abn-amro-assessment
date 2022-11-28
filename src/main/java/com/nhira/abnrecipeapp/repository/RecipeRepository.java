package com.nhira.abnrecipeapp.repository;

import com.nhira.abnrecipeapp.model.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RecipeRepository extends JpaRepository<Recipe, String> {

    // TODO - FIX  INGREDIENTS
    @Query("SELECT r FROM Recipe r WHERE " +
            "(:search IS NULL OR :search = '' " +
            "OR LOWER(r.name) LIKE %:search% " +
            "OR LOWER(r.classification) LIKE %:search%) "
    )
    Page<Recipe> search(@Param("search") String search, Pageable pageable);

    @Query("SELECT r FROM Recipe r LEFT OUTER JOIN Ingredient i ON i.recipe.id = r.id LEFT OUTER JOIN Instruction inr ON inr.recipe.id = r.id WHERE " +
            "(:classification IS NULL OR :classification = '' OR r.classification = :classification) " +
            "AND (:instructionSearch IS NULL OR :instructionSearch = '' OR LOWER(inr.description) LIKE %:instructionSearch%) " +
            "AND (:ingredientName IS NULL OR :ingredientName = ''  OR (:includeIngredient IS TRUE AND i.name = :ingredientName) OR (:includeIngredient IS FALSE AND i.name <> :ingredientName)) " +
            "AND (r.numberOfServings = :numberOfServings) "
    )
    Page<Recipe> filter(@Param("classification") String classification,
                        @Param("numberOfServings") long numberOfServings,
                        @Param("instructionSearch") String instructionSearch,
                        @Param("ingredientName") String ingredientName,
                        @Param("includeIngredient") boolean includeIngredient,
                        Pageable pageable);
}
