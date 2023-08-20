package com.recipetory.ingredient.application;

import com.recipetory.ingredient.domain.Ingredient;
import com.recipetory.ingredient.domain.IngredientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class IngredientServiceTest {
    private IngredientService ingredientService;
    private IngredientRepository ingredientRepository;
    @BeforeEach
    public void setUp() {
        ingredientRepository = new TestIngredientRepository();
        ingredientService = new IngredientService(ingredientRepository);
    }

    @Test
    @DisplayName("존재하지 않는 재료는 새로 생성된다.")
    void noExistIngredient() {
        // given : "신재료" 이름을 가진 new ingredient는 존재하지 않는다.
        String newIngredientName = "신재료";
        Ingredient newIngredient = Ingredient.builder().name(newIngredientName).build();
        assertTrue(ingredientRepository.findByName(newIngredientName).isEmpty());

        // when : newIngredientName을 saveOrFind() 하면,
        ingredientService.saveOrFind(newIngredientName);

        // then : 새로운 객체가 생성된다.
        assertTrue(ingredientRepository.findByName(newIngredientName).isPresent());
    }

    @Test
    @DisplayName("이미 있는 재료는 생성되지 않고 바로 return 된다.")
    void existIngredient() {
        // given : "재료" 이름을 가진 ingredient 저장
        String ingredientName = "재료";
        Ingredient existing = ingredientRepository.save(
                Ingredient.builder().name(ingredientName).build());

        // when, then : "재료" 이름으로 ingredient 찾았을 때 same
        assertEquals(existing.getId(),
                ingredientService.saveOrFind(ingredientName).getId());
    }
}
