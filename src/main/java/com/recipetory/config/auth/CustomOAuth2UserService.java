package com.recipetory.config.auth;

import com.recipetory.config.auth.dto.OAuthAttributes;
import com.recipetory.config.auth.dto.SessionUser;
import com.recipetory.user.domain.User;
import com.recipetory.user.domain.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 1. resource server 정보 추출
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        String provider = userRequest.getClientRegistration().getRegistrationId();

        // 2. resource server 에서 provider specific attribute, key field 추출
        OAuth2User oAuth2User = delegate.loadUser(userRequest);
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();

        // 3. general attribute 추출
        OAuthAttributes oAuthAttributes = OAuthAttributes
                .of(provider, userNameAttributeName, oAuth2User.getAttributes());

        // 4. attributes 토대로 회원가입 or 로그인 user 정보 가져오기
        User loggedInUser = saveIfNew(oAuthAttributes);

        // 5. session에 현재 로그인한 유저 정보 저장
        httpSession.setAttribute("user", SessionUser.fromEntity(loggedInUser));

        // 6. security context에 등록할 oauth2 user return
        return new DefaultOAuth2User(
                Collections.singleton(loggedInUser.getAuthority()),
                oAuthAttributes.getAttributes(),
                oAuthAttributes.getUserNameAttributeName());
    }

    @Transactional
    private User saveIfNew(OAuthAttributes oAuthAttributes) {
        return userRepository.findByEmail(oAuthAttributes.getEmail())
                .orElseGet(() -> userRepository.save(oAuthAttributes.toEntity()));
    }

    public void updateContextUser(User user) {
        // 1. 현재 security context의 oauth user 가져오기
        OAuth2AuthenticationToken oAuth = (OAuth2AuthenticationToken)
                SecurityContextHolder.getContext().getAuthentication();
        OAuth2User principal = oAuth.getPrincipal();

        // 2. new auth token 생성 (role만 업데이트)
        Authentication newAuth = new OAuth2AuthenticationToken(
                principal,
                Collections.singleton(user.getAuthority()),
                oAuth.getAuthorizedClientRegistrationId());
        SecurityContextHolder.getContext().setAuthentication(newAuth);

        // 3. 세션 설정
        httpSession.setAttribute("user", SessionUser.fromEntity(user));
    }
}
