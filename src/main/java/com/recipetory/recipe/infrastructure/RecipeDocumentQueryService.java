package com.recipetory.recipe.infrastructure;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.NestedQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.recipetory.recipe.domain.CookingTime;
import com.recipetory.recipe.domain.Difficulty;
import com.recipetory.recipe.domain.document.RecipeDocument;
import com.recipetory.recipe.domain.Serving;
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
        if (title != "") {
            infoQueries.add(MatchQuery.of(q ->
                    q.field("title").query(title))._toQuery());
        }
        if (cookingTime != CookingTime.UNDEFINED) {
            System.out.println("undefined가 아니빈다!");
            infoQueries.add(NestedQuery.of(
                    n -> n.path("recipeInfo").query(
                            q -> q.match(
                                    s -> s.field("recipeInfo.cookingTime")
                                            .query(cookingTime.name()))))._toQuery());
        }
        if (difficulty != Difficulty.UNDEFINED) {
            infoQueries.add(NestedQuery.of(n ->
                    n.path("recipeInfo").query(
                            q -> q.match(
                                    s -> s.field("recipeInfo.difficulty")
                                            .query(difficulty.name()))))._toQuery());
        }
        if (serving != Serving.UNDEFINED) {
            infoQueries.add(NestedQuery.of(n ->
                    n.path("recipeInfo").query(
                            q -> q.match(
                                    s -> s.field("recipeInfo.serving")
                                            .query(serving.name()))))._toQuery());
        }

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
}
