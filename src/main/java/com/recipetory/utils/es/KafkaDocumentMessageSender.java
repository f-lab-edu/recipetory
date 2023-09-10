package com.recipetory.utils.es;

import com.recipetory.config.kafka.KafkaTopic;
import com.recipetory.recipe.domain.document.RecipeDocument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaDocumentMessageSender {
    private final KafkaTemplate<String, RecipeDocument> documentKafkaTemplate;
    private final KafkaTemplate<String, Long> deleteDocumentKafkaTemplate;

    public void sendRecipeDocument(RecipeDocument document) {
        documentKafkaTemplate.send(KafkaTopic.NEW_RECIPE, document)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.warn("document message sent failed!", ex);
                    }});
    }

    public void sendDeleteRecipeDocument(Long recipeId) {
        deleteDocumentKafkaTemplate.send(KafkaTopic.DELETE_RECIPE,recipeId)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.warn("delete document message sent failed!", ex);
                    }});
    }
}
