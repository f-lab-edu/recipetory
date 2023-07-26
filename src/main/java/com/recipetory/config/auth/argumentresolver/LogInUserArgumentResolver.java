package com.recipetory.config.auth.argumentresolver;

import com.recipetory.config.auth.dto.SessionUser;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class LogInUserArgumentResolver implements HandlerMethodArgumentResolver {
    private final HttpSession httpSession;

    /**
     * 두가지 조건을 검사하여 SessionUser를 method arg에 binding합니다.
     * 1. handler method의 parameter에 @LogInUserrk 붙었는지
     * 2. 해당 parameter type이 String이 맞는지
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean isEmailAnnotation = parameter
                .getParameterAnnotation(LogInUser.class) != null;
        boolean isString = parameter
                .getParameterType().equals(SessionUser.class);

        return isEmailAnnotation && isString;
    }

    // arg에 실제 SessionUser의 email 속성 binding
    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {
        return httpSession.getAttribute("user");
    }
}
