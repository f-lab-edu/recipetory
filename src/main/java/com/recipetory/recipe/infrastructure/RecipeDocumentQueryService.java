package com.recipetory.recipe.infrastructure;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.*;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.recipetory.recipe.domain.CookingTime;
import com.recipetory.recipe.domain.Difficulty;
import com.recipetory.recipe.domain.Serving;
import com.recipetory.recipe.domain.document.RecipeDocument;
import com.recipetory.tag.domain.TagName;
import com.recipetory.utils.exception.EsClientException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class RecipeDocumentQueryService {
    private final ElasticsearchClient esClient;
    private final RecipeQueryHelper recipeQueryHelper;

    /**
     * 인자로 주어진 tagName List의 tag가 "전부" 포함된 RecipeDocument를 반환한다.
     * @param tagNames
     * @return
     */
    public List<RecipeDocument> findByTagNames(List<TagName> tagNames) {
        List<Query> tagQueries = tagNames.stream().map(tagName ->
            TermQuery.of(q -> q.field("tags")
                            .value(tagName.name()))._toQuery())
                .toList();

        try {
            SearchResponse<RecipeDocument> response = esClient.search(
                    s -> s.index("recipe").query(
                            q -> q.bool(
                                    b -> b.must(tagQueries))),
                    RecipeDocument.class
            );
            return response.hits().hits().stream().map(Hit::source).toList();
        } catch (IOException e) {
            throw new EsClientException("recipe");
        }
    }

    /**
     * 메소드 인자로 주어진 항목과 일치하는 Recipe를 반환한다.
     * title이 빈 문자열이거나, enum type이 UNDEFINED라면 검색 조건에 포함하지 않는다.
     * @param title
     * @param cookingTime
     * @param difficulty
     * @param serving
     * @return
     */
    public List<RecipeDocument> findByRecipeInfo(
            String title, CookingTime cookingTime, Difficulty difficulty, Serving serving
    ) {
        // title이 빈칸이거나, recipeInfo 항목 타입이 UNDEFIEND이면 검색 조건에서 제외
        List<Query> infoQueries = new ArrayList<>();
        infoQueries.add(recipeQueryHelper.buildTitleQuery(title));
        infoQueries.add(recipeQueryHelper.buildRecipeInfoQuery(
                "recipeInfo.cookingTime", cookingTime.name()));
        infoQueries.add(recipeQueryHelper.buildRecipeInfoQuery(
                "recipeInfo.difficulty", difficulty.name()));
        infoQueries.add(recipeQueryHelper.buildRecipeInfoQuery(
                "recipeInfo.serving", serving.name()));

        try {
            SearchResponse<RecipeDocument> response = esClient.search(
                    s -> s.index("recipe")
                            .query(q -> q.bool(
                                    b -> b.must(infoQueries))),
                    RecipeDocument.class);
            return response.hits().hits().stream().map(Hit::source).toList();
        } catch (IOException e) {
            throw new EsClientException("recipe");
        }
    }

    /**
     * view count, ratings를 기준으로 레시피를 내림차순으로 정렬하여
     * 인자로 전달된 갯수만큼의 레시피를 반환한다.
     * @param number
     * @return recipe document
     */
    public List<RecipeDocument> getTopRecipe(int number) {
        try {
            // 평점(ratings), 조회수(viewCount)를 이용해 추천에 사용될 점수를 계산하는 script
            String script = "doc['recipeStatistics.ratings'].value + " +
                    "Math.log(1 + doc['recipeStatistics.viewCount'].value);";

            // nested field의 값을 이용한 결과를 내림차순으로 정렬하는 쿼리 source
            ScriptSort source = SortOptionsBuilders.script()
                    .script(Script.of(sc -> sc.inline(InlineScript.of(i -> i.source(script)))))
                    .type(ScriptSortType.Number)
                    .order(SortOrder.Desc)
                    .nested(NestedSortValue.of(sv -> sv.path("recipeStatistics"))).build();

            SearchResponse<RecipeDocument> response = esClient.search(
                    s -> s.index("recipe").size(number).sort(so -> so.script(source)), RecipeDocument.class);
            return response.hits().hits().stream().map(Hit::source).toList();
        } catch (IOException e) {
            throw new EsClientException("recipe");
        }
    }
}
