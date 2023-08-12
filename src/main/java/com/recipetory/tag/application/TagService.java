package com.recipetory.tag.application;

import com.recipetory.recipe.application.RecipeService;
import com.recipetory.recipe.domain.Recipe;
import com.recipetory.tag.domain.Tag;
import com.recipetory.tag.domain.TagName;
import com.recipetory.tag.domain.TagRepository;
import com.recipetory.tag.presentation.dto.TagDto;
import com.recipetory.user.application.UserService;
import com.recipetory.user.domain.User;
import com.recipetory.user.domain.exception.NotOwnerException;
import com.recipetory.utils.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;
    private final RecipeService recipeService;
    private final UserService userService;

    /**
     * 인자로 들어온 TagDto의 연관 관계를 설정한 뒤 save한다.
     * @param tagDto
     * @param logInEmail 현재 로그인한 유저
     * @return 생성된 {@link TagDto}
     */
    @Transactional
    public TagDto addTag(TagDto tagDto, Long recipeId, String logInEmail) {
        User author = userService.getUserByEmail(logInEmail);
        Recipe recipe = recipeService.getRecipeById(recipeId);

        // 레시피 주인 검사
        validateRecipeAuthor(recipe,author);

        // 태그는 한 번만 반영 (idempotent)
        Tag saved = tagRepository.findByRecipeAndTagName(recipe, tagDto.getTagName())
                .orElseGet(() -> {
                    Tag tag = tagDto.toEntity();
                    tag.setRecipe(recipe);
                    return tagRepository.save(tag);
                });

        return TagDto.fromEntity(saved);
    }

    /**
     * 특정 tagName을 가진 recipe들을 찾는다.
     * @param tagName 찾고자 하는 tag
     * @return 해당 tagName을 가진 레시피 리스트
     */
    @Transactional(readOnly = true)
    public List<Recipe> getRecipeByTag(TagName tagName) {
        List<Tag> foundTags = tagRepository.findByTagName(tagName);
        return foundTags.stream()
                .map(Tag::getRecipe).toList();
    }

    /**
     * recipeId의 레시피가 가진 tag 리스트를 dto로 반환한다.
     * @param recipeId
     * @return
     */
    @Transactional(readOnly = true)
    public List<TagDto> getTagByRecipeId(Long recipeId) {
        Recipe recipe = recipeService.getRecipeById(recipeId);
        List<Tag> found = tagRepository.findByRecipe(recipe);

        return found.stream()
                .map(TagDto::fromEntity).toList();
    }

    /**
     * 특정 id를 가진 tag를 삭제한다.
     * @param tagId
     * @param logInEmail 현재 로그인하여 세션에 저장된 유저 email
     */
    @Transactional
    public void deleteTag(Long tagId, String logInEmail) {
        User author = userService.getUserByEmail(logInEmail);
        Tag foundTag = tagRepository.findById(tagId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Tag",String.valueOf(tagId)));
        Recipe recipe = foundTag.getRecipe();

        // 레시피 주인만이 태그 삭제 가능
        validateRecipeAuthor(recipe,author);

        tagRepository.delete(foundTag);
    }

    /**
     * 레시피의 작성자가 맞는지 확인한다.
     * @param recipe target recipe
     * @param author expected author
     * @throws NotOwnerException author 아닐 경우
     */
    @Transactional(readOnly = true)
    private void validateRecipeAuthor(Recipe recipe, User author) {
        if (!recipe.isSameAuthor(author)) {
            throw new NotOwnerException(author.getId(), recipe.getAuthor().getId(),
                    "Recipe", String.valueOf(recipe.getId()));
        }
    }
}
