package com.recipetory.recipe.infrastructure;

import com.recipetory.recipe.domain.CookingTime;
import com.recipetory.recipe.domain.Difficulty;
import com.recipetory.recipe.domain.Recipe;
import com.recipetory.recipe.domain.Serving;
import com.recipetory.tag.domain.TagName;
import com.recipetory.user.domain.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RecipeJpaRepository extends JpaRepository<Recipe,Long> {
    Optional<Recipe> findByTitle(String title);

    List<Recipe> findByTitleContains(String title);

    List<Recipe> findByAuthor(User author);

    /**
     * tagNames의 tagName을 전부 포함하는 레시피들을 검색한다.
     * @param tagNames
     * @param tagSize
     * @return
     */
    @Query("SELECT DISTINCT r FROM Recipe r " +
            "JOIN FETCH r.author WHERE " +
            ":tagSize > 0 AND :tagSize = " +
            "(SELECT COUNT(t) FROM Tag t WHERE " +
            "t.recipe = r AND t.tagName IN :tagNames)")
    List<Recipe> findByTags(
            @Param("tagNames") List<TagName> tagNames,
            @Param("tagSize") int tagSize,
            Pageable pageable);

    @Query("SELECT DISTINCT r FROM Recipe r " +
            "JOIN FETCH r.author " +
            "WHERE r.title LIKE %:title% " +
            "AND (r.recipeInfo.cookingTime = :cookingTime OR :cookingTime = 'UNDEFINED')" +
            "AND (r.recipeInfo.difficulty = :difficulty OR :difficulty = 'UNDEFINED')" +
            "AND (r.recipeInfo.serving = :serving OR :serving = 'UNDEFINED')")
    List<Recipe> findByRecipeInfo(
            @Param("title") String title,
            @Param("cookingTime") CookingTime cookingTime,
            @Param("difficulty") Difficulty difficulty,
            @Param("serving") Serving serving);
}
