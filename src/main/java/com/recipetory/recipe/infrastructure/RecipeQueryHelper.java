package com.recipetory.recipe.infrastructure;

import co.elastic.clients.elasticsearch._types.query_dsl.*;
import org.springframework.stereotype.Component;

@Component
public class RecipeQueryHelper {
    /**
     * title 문자열에 대한 match query를 반환한다.
     * 인자로 주어진 title이 빈 문자열일 경우 조건에 포함하지 않는다.
     * @param title
     * @return
     */
    public Query buildTitleQuery(String title) {
        BoolQuery.Builder boolQuery = QueryBuilders.bool();
        if (title != "") {
            return boolQuery.must(MatchQuery.of(q ->
                    q.field("title").query(title))._toQuery()).build()._toQuery();
        } else {
            return boolQuery.build()._toQuery();
        }
    }

    /**
     * RecipeInfo 항목에 대한 match query를 반환한다.
     * 검색하고자 하는 nested fieldName과 enum name을 인자로 받는다.
     * 인자로 들어온 enum value name이 UNDEFINED일 경우 조건에 포함하지 않는다.
     * @param fieldName nested field name
     * @param name enum value name
     * @return
     */
    public Query buildRecipeInfoQuery(String fieldName, String name) {
        BoolQuery.Builder boolQuery = QueryBuilders.bool();
        if (name != "UNDEFINED") {
            NestedQuery.Builder nestedQuery =
                    QueryBuilders.nested().path("recipeInfo").query(
                            q -> q.match(
                                    s -> s.field(fieldName).query(name)));
            boolQuery.must(b -> b.nested(nestedQuery.build()));
        }
        return boolQuery.build()._toQuery();
    }
}
