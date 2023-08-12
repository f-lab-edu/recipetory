package com.recipetory.tag.presentation;

import com.recipetory.config.auth.argumentresolver.LogInUser;
import com.recipetory.config.auth.dto.SessionUser;
import com.recipetory.recipe.domain.Recipe;
import com.recipetory.recipe.presentation.dto.RecipeListDto;
import com.recipetory.tag.application.TagService;
import com.recipetory.tag.domain.TagName;
import com.recipetory.tag.presentation.dto.TagDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;

    /**
     * recipeId에 해당하는 레시피에 태그를 추가한다
     * @param tagDto
     * @param logInUser
     * @return
     */
    @PostMapping("/tags/{recipeId}")
    public ResponseEntity<TagDto> createTag(
            @Valid @RequestBody TagDto tagDto,
            @PathVariable("recipeId") Long recipeId,
            @LogInUser SessionUser logInUser
    ) {
        TagDto saved = tagService.addTag(
                tagDto, recipeId, logInUser.getEmail());
        return ResponseEntity.ok(saved);
    }

    /**
     * 특정 tag의 레시피를 검색한다
     * @param tagName
     * @return
     */
    @GetMapping("/tags")
    public ResponseEntity<RecipeListDto> getRecipesByTagName(
            @RequestParam(name = "tag", required = true) TagName tagName
    ) {
        List<Recipe> recipes = tagService.getRecipeByTag(tagName);
        return ResponseEntity.ok(RecipeListDto.fromEntityList(recipes));
    }

    /**
     * 특정 레시피의 태그 목록을 불러온다.
     * @param recipeId
     * @return
     */
    @GetMapping("/recipes/{recipeId}/tags")
    public ResponseEntity<List<TagDto>> getTagOfRecipe(
            @PathVariable("recipeId") Long recipeId
    ) {
        List<TagDto> tagDtos = tagService.getTagByRecipeId(recipeId);
        return ResponseEntity.ok(tagDtos);
    }

    /**
     * tagId에 해당하는 태그를 삭제한다
     * @param tagId
     * @param logInUser
     * @return
     */
    @DeleteMapping("/tags/{tagId}")
    public ResponseEntity<Void> deleteTagById(
            @PathVariable(name = "tagId") Long tagId,
            @LogInUser SessionUser logInUser
    ) {
        String logInEmail = logInUser.getEmail();
        tagService.deleteTag(tagId, logInEmail);

        return ResponseEntity.ok().build();
    }
}
