package com.recipetory.config.auth.dto;

import com.recipetory.user.domain.Role;
import com.recipetory.user.domain.User;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Builder
@Getter
public class OAuthAttributes {
    private String email;
    private String name;
    private String provider;
    private String userNameAttributeName;
    private Map<String,Object> attributes;

    /**
     * @param provider name of provider
     * @param attributes provider specific attributes
     * @return general attributes
     * */
    public static OAuthAttributes of(String provider,
                                     String userNameAttributeName,
                                     Map<String,Object> attributes) {
        OAuthProvider foundProvider = OAuthProvider.findProvider(provider);
        return foundProvider.convert(userNameAttributeName, attributes);
    }

    /**
     * 회원가입 user entity
     * default {@link Role} : GUEST
     * */
    public User toEntity() {
        return User.builder()
                .email(this.email)
                .name(this.name)
                .provider(this.provider)
                .role(Role.GUEST)
                .build();
    }
}
