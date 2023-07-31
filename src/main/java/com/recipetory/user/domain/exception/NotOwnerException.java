package com.recipetory.user.domain.exception;

import lombok.AllArgsConstructor;

/**
 * resource owner 아닌 유저가 Update or Delete 할 때 발생하는 Exception
 * */
@AllArgsConstructor
public class NotOwnerException extends RuntimeException {
    private Long currentId;
    private Long requiredId;
    private String resourceType;
    private String resourceId;

    @Override
    public String getMessage() {
        return resourceType + " id: " + resourceId +
                ", ownerId: " + requiredId + ", currentId: " + currentId;
    }
}
