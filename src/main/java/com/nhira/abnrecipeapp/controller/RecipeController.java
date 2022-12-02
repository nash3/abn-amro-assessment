package com.nhira.abnrecipeapp.controller;

import com.nhira.abnrecipeapp.dto.RecipeDto;
import com.nhira.abnrecipeapp.dto.RecipeFilterDto;
import com.nhira.abnrecipeapp.exceptions.RecipeNotFoundException;
import com.nhira.abnrecipeapp.service.api.RecipeService;
import com.nhira.abnrecipeapp.utils.ApiResponse;
import com.nhira.abnrecipeapp.utils.enums.RecipeClassification;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/recipes")
public class RecipeController {

    private final RecipeService recipeService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<RecipeDto> createRecipe(@Valid @RequestBody RecipeDto recipeDto) {
        return recipeService.createRecipe(recipeDto);
    }

    @GetMapping("/find-all")
    @Parameter(in = ParameterIn.DEFAULT, name = "page", schema = @Schema(type = "int", defaultValue = "0", description = "The response is paginated and this field represents the page number"))
    @Parameter(in = ParameterIn.DEFAULT, name = "size", schema = @Schema(type = "int", defaultValue = "10", description = "The response is paginated and this field represents the page size"))
    public Page<RecipeDto> getAllRecipes(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return recipeService.getAllRecipes(page, size);
    }

    @GetMapping("/find")
    @Parameter(in = ParameterIn.DEFAULT, name = "ingredientName", schema = @Schema(type = "String", example = "salmon", description = "Ingredient name"))
    @Parameter(in = ParameterIn.DEFAULT, name = "includeIngredient", schema = @Schema(type = "boolean", defaultValue = "true", description = "Says whether to include or exclude specified ingredient"))
    @Parameter(in = ParameterIn.DEFAULT, name = "numberOfServings", schema = @Schema(type = "long", defaultValue = "1", description = "Number of people recipe can serve"))
    @Parameter(in = ParameterIn.DEFAULT, name = "classification", schema = @Schema(type = "RecipeClassification", defaultValue = "VEGETARIAN", description = "Whether the recipe is vegetarian or not"))
    public Page<RecipeDto> getRecipes(
            @RequestParam(value = "ingredientName", defaultValue = "", required = false) String ingredientName,
            @RequestParam(value = "instructionSearch", defaultValue = "", required = false) String instructionSearch,
            @RequestParam(value = "includeIngredient", defaultValue = "true", required = false) boolean includeIngredient,
            @RequestParam(value = "numberOfServings", defaultValue = "1", required = false) long numberOfServings,
            @RequestParam(value = "classification", defaultValue = "") RecipeClassification classification,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        final RecipeFilterDto recipeFilter = RecipeFilterDto.builder()
                .ingredientName(ingredientName)
                .includeIngredient(includeIngredient)
                .instructionSearch(instructionSearch)
                .numberOfServings(numberOfServings)
                .classification(classification)
                .build();
        return recipeService.filterRecipes(recipeFilter, page, size);
    }

    @GetMapping("/{id}")
    @Parameter(in = ParameterIn.PATH, name = "id", schema = @Schema(type = "string", example = "c8c3cc08-6e19-11ed-a1eb-0242ac120002", description = "Unique recipe ID"))
    public ApiResponse<RecipeDto> getRecipe(@PathVariable("id") String id) {
        try {
            return recipeService.getRecipe(id);
        } catch (RecipeNotFoundException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    exception.getMessage(), exception);
        }
    }

    @DeleteMapping("/{id}")
    @Parameter(in = ParameterIn.PATH, name = "id", schema = @Schema(type = "string", example = "c8c3cc08-6e19-11ed-a1eb-0242ac120002", description = "Unique recipe ID"))
    public ApiResponse<RecipeDto> deleteRecipe(@PathVariable("id") String id) {
        try {
            return recipeService.deleteRecipe(id);
        } catch (RecipeNotFoundException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    exception.getMessage(), exception);
        }
    }

    @PutMapping
    public ApiResponse<RecipeDto> updateRecipe(@Valid @RequestBody RecipeDto recipeDto) {
        try {
            return recipeService.updateRecipe(recipeDto);
        } catch (RecipeNotFoundException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    exception.getMessage(), exception);
        }
    }
}
