package com.recipetory.config.auth;

import com.recipetory.user.domain.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

    // since spring security 6.1.2 : MvcRequestMatcher.Builder
    // https://github.com/spring-projects/spring-security-samples/tree/main/servlet/java-configuration/authentication/preauth
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http, MvcRequestMatcher.Builder mvc) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(mvc.pattern("/check")).authenticated()
                        .requestMatchers(mvc.pattern("/profile")).authenticated()
                        .requestMatchers(mvc.pattern(HttpMethod.POST,"/recipe")).hasAuthority(Role.USER.getKey())
                        .requestMatchers(mvc.pattern(HttpMethod.POST,"/bookmark/**")).authenticated()
                        .requestMatchers(mvc.pattern(HttpMethod.DELETE,"/bookmark/**")).authenticated()
                        .anyRequest().permitAll())
                // for h2-console
                .headers(headers -> headers.frameOptions(option -> option.disable()))
                .csrf(csrf -> csrf.disable())
                .oauth2Login(oauth -> oauth
                        .userInfoEndpoint(userInfo ->
                                userInfo.userService(customOAuth2UserService)))
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
