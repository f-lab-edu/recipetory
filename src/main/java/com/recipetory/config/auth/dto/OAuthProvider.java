package com.recipetory.config.auth.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;

import java.util.Arrays;
import java.util.Map;

@RequiredArgsConstructor
public enum OAuthProvider {
    GOOGLE("google", (userNameAttributeName, attributes) -> {
        return OAuthAttributes.builder()
                .email((String) attributes.get("email"))
                .name((String) attributes.get("name"))
                .userNameAttributeName(userNameAttributeName)
                .provider("google")
                .attributes(attributes)
                .build();
    });

    private final String name;
    private final OAuthAttributeConverter converter;

    public static OAuthProvider findProvider(String name) {
        return Arrays.stream(OAuthProvider.values())
                .filter(provider -> provider.hasEqualName(name)).findAny()
                .orElseThrow(() -> new OAuth2AuthenticationException("유효하지 않은 provider : " + name));
    }

    public OAuthAttributes convert(String userNameAttributeName, Map<String, Object> attributes) {
        return converter.convert(userNameAttributeName,attributes);
    }

    private boolean hasEqualName(String name) {
        return this.name.equals(name);
    }
}
