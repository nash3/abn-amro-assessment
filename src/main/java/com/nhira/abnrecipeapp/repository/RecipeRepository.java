package com.nhira.abnrecipeapp.repository;

import com.nhira.abnrecipeapp.model.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RecipeRepository extends JpaRepository<Recipe, String> {

    @Query("SELECT r FROM Recipe r  WHERE " +
            "(:classification IS NULL OR :classification = '' OR r.classification = :classification) " +
            "AND (:instructionSearch IS NULL OR :instructionSearch = '' OR LOWER(r.instructions) LIKE %:instructionSearch%) " +
            "AND (:ingredientName IS NULL OR :ingredientName = ''  OR (:includeIngredient IS TRUE AND r.ingredients LIKE %:ingredientName%)" +
            " OR (:includeIngredient IS FALSE AND r.ingredients NOT LIKE %:ingredientName%)) " +
            "AND (:numberOfServings = 0L) OR ( r.numberOfServings = :numberOfServings) "
    )
    Page<Recipe> filter(@Param("classification") String classification,
                        @Param("numberOfServings") long numberOfServings,
                        @Param("instructionSearch") String instructionSearch,
                        @Param("ingredientName") String ingredientName,
                        @Param("includeIngredient") boolean includeIngredient,
                        Pageable pageable);
}
