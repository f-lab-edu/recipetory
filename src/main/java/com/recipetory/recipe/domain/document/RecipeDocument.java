package com.recipetory.recipe.domain.document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.recipetory.recipe.domain.Recipe;
import com.recipetory.recipe.domain.RecipeInfo;
import com.recipetory.recipe.domain.RecipeStatistics;
import com.recipetory.tag.domain.Tag;
import com.recipetory.tag.domain.TagName;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Setting(settingPath = "/elasticsearch/settings/settings.json")
@Document(indexName = "recipe")
public class RecipeDocument {
    @Id @Field(type = FieldType.Long)
    private Long id;

    @Field(type = FieldType.Long)
    private Long authorId;

    @Field(type = FieldType.Text,
            analyzer = "korean")
    private String title;

    @Field(type = FieldType.Nested)
    private RecipeInfo recipeInfo;

    @Field(type = FieldType.Nested)
    private RecipeStatistics recipeStatistics;

    @Field(type = FieldType.Nested)
    private List<StepDocument> steps;

    @Field(type = FieldType.Nested)
    private List<IngredientDocument> ingredients;

    @Field(type = FieldType.Keyword)
    private List<TagName> tags;

    @Field(type = FieldType.Date,
            format = {DateFormat.date_hour_minute_second_millis,
                    DateFormat.epoch_millis})
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    LocalDateTime createdAt;

    public static RecipeDocument fromEntity(Recipe recipe) {
        return RecipeDocument.builder()
                .id(recipe.getId()).authorId(recipe.getAuthor().getId())
                .title(recipe.getTitle())
                .recipeInfo(recipe.getRecipeInfo())
                .recipeStatistics(recipe.getRecipeStatistics())
                .steps(StepDocument.fromEntityList(recipe.getSteps()))
                .ingredients(IngredientDocument.fromEntityList(recipe.getIngredients()))
                .tags(recipe.getTags().stream().map(Tag::getTagName).toList())
                .createdAt(recipe.getCreatedAt()).build();
    }
}
