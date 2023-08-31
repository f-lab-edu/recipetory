package com.recipetory.utils.es;

import com.recipetory.config.kafka.KafkaTopic;
import com.recipetory.recipe.domain.Recipe;
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

    public void sendRecipeDocument(RecipeDocument document) {
        documentKafkaTemplate.send(KafkaTopic.NEW_RECIPE, document)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.warn("document message sent failed!", ex);
                    }});
    }
}
