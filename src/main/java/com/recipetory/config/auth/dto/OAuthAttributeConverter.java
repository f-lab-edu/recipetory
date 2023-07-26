package com.recipetory.config.auth.dto;

import com.recipetory.config.auth.dto.OAuthAttributes;

import java.util.Map;

/**
 * provider server에서 제공한 user attributes를
 * recipetory에서 이용 가능한 OAuthAttributes로 convert합니다.
 */
@FunctionalInterface
public interface OAuthAttributeConverter {
    /**
     * @param userNameAttributeName provider server에서 이용하는 id field
     * @param attributes provider server에서 제공한 user attributes
     * @return : recipetory에서 사용하는 {@link OAuthAttributes} 타입의 DTO
     */
    OAuthAttributes convert(String userNameAttributeName, Map<String,Object> attributes);
}
