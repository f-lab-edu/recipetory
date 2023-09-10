package com.recipetory.recipe.infrastructure;

import com.recipetory.recipe.domain.document.RecipeDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeDocumentRepository extends ElasticsearchRepository<RecipeDocument, Long> {
    List<RecipeDocument> findByAuthorId(Long authorId);
}
