package com.recipetory.config.auth;

import com.recipetory.user.domain.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    // since spring security 6.1.2 : MvcRequestMatcher.Builder
    // https://github.com/spring-projects/spring-security-samples/tree/main/servlet/java-configuration/authentication/preauth
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http, MvcRequestMatcher.Builder mvc) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(mvc.pattern("/check")).authenticated()
                        .requestMatchers(mvc.pattern("/profile")).authenticated()
                        .requestMatchers(mvc.pattern(HttpMethod.POST,"/recipes")).hasAuthority(Role.USER.getKey())
                        .requestMatchers(mvc.pattern(HttpMethod.POST,"/bookmarks/**")).authenticated()
                        .requestMatchers(mvc.pattern(HttpMethod.DELETE,"/bookmarks/**")).authenticated()
                        .requestMatchers(mvc.pattern("/follow/**")).authenticated()
                        .requestMatchers(mvc.pattern(HttpMethod.POST,"/tags/**")).hasAuthority(Role.USER.getKey())
                        .requestMatchers(mvc.pattern(HttpMethod.DELETE,"/tags/**")).hasAuthority(Role.USER.getKey())
                        // 조회(GET)는 permitAll, 나머지는 authenticated()
                        .requestMatchers(mvc.pattern(HttpMethod.GET,"/comments/**")).permitAll()
                        .requestMatchers(mvc.pattern("/comments/**")).authenticated()
                        .requestMatchers(mvc.pattern(HttpMethod.GET,"/reviews/**")).permitAll()
                        .requestMatchers(mvc.pattern("/reviews/**")).authenticated()
                        .requestMatchers(mvc.pattern("/notifications")).authenticated()
                        .anyRequest().permitAll())
                // for h2-console
                .headers(headers -> headers.frameOptions(option -> option.disable()))
                .csrf(csrf -> csrf.disable())
                .oauth2Login(oauth -> oauth
                        .userInfoEndpoint(userInfo ->
                                userInfo.userService(customOAuth2UserService)))
                .exceptionHandling(e -> e.authenticationEntryPoint(customAuthenticationEntryPoint))
                .logout(logout -> logout
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true));

        return http.build();
    }

    @Bean
    MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
        return new MvcRequestMatcher.Builder(introspector);
    }
}
