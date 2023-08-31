package com.recipetory.utils.es;

import com.recipetory.notification.domain.event.*;
import com.recipetory.recipe.domain.document.RecipeDocument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * entity가 작성, 혹은 수정될 때 kafka에 생성될 es document를 message로 send
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class CreateDocumentListener {
    private final KafkaDocumentMessageSender documentMessageSender;

    /**
     * 생성된 recipe Entity를 ES에 반영하기 위해 kafka message를 보낸다.
     * @param createRecipeEvent
     */
    @TransactionalEventListener
    @Async
    public void createRecipeDocument(CreateRecipeEvent createRecipeEvent) {
        RecipeDocument document = RecipeDocument.fromEntity(
                createRecipeEvent.getRecipe());

        documentMessageSender.sendRecipeDocument(document);
    }
}
