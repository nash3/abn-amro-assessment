package com.nhira.abnrecipeapp.integrationtests;

import com.nhira.abnrecipeapp.dto.RecipeDto;
import com.nhira.abnrecipeapp.mapper.DtoMapper;
import com.nhira.abnrecipeapp.model.Recipe;
import com.nhira.abnrecipeapp.service.api.RecipeService;
import com.nhira.abnrecipeapp.utils.ApiResponse;
import com.nhira.abnrecipeapp.utils.enums.ResponseCode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static com.nhira.abnrecipeapp.utils.RecipeTestDataUtil.getRecipe;
import static com.nhira.abnrecipeapp.utils.RecipeTestDataUtil.getVeganRecipeDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@SpringBootTest
class RecipeServiceIT {

    @Autowired
    private RecipeService recipeService;

    @Test
    void serviceWorks() {
        final Recipe testRecipe = getRecipe(getVeganRecipeDto());

        ApiResponse<RecipeDto> actualResponse = recipeService.updateRecipe(DtoMapper.MAPPER.toRecipeDto(testRecipe));

        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse.isSuccessful()).isTrue();
        assertThat(actualResponse.getResponseCode()).isEqualTo(ResponseCode.SUCCESS);
        assertThat(actualResponse.getNarrative()).isEqualTo(ResponseCode.SUCCESS.getDescription());
        assertThat(actualResponse.getBody()).isNotNull();
        assertThat(actualResponse.getBody()).isEqualTo(DtoMapper.MAPPER.toRecipeDto(testRecipe));
    }

}
