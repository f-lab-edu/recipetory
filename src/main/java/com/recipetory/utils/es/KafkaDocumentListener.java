package com.recipetory.utils.es;

import com.recipetory.config.kafka.KafkaTopic;
import com.recipetory.recipe.domain.document.RecipeDocument;
import com.recipetory.recipe.infrastructure.RecipeDocumentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaDocumentListener {
    private final RecipeDocumentRepository recipeDocumentRepository;

    /**
     * Kafka message로 도착한 RecipeDocument를 indexing한다.
     * @param document kafkaMessgae
     */
    @KafkaListener(topics = KafkaTopic.NEW_RECIPE,
            containerFactory = "recipeDocumentKafkaListenerContainerFactory",
            groupId = "${spring.kafka.group-id}")
    public void documentListener(RecipeDocument document) {
        log.info("recipe document id {} 저장", document.getId());
        recipeDocumentRepository.save(document);
    }

    /**
     * Kafka message로 도착한 RecipeDocument id를 삭제한다.
     * @param recipeId
     */
    @KafkaListener(topics = KafkaTopic.DELETE_RECIPE,
            containerFactory = "deleteDocumentKafkaListenerContainerFactory",
            groupId = "${spring.kafka.group-id}")
    public void deleteDocumentListener(Long recipeId) {
        log.info("recipe document id {} 삭제",recipeId);
        recipeDocumentRepository.deleteById(recipeId);
    }
}
